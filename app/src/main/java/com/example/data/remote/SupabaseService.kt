package com.example.data.remote

import android.content.Context
import android.content.SharedPreferences
import com.example.BuildConfig
import com.example.data.model.GratitudeItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit

data class SupabaseUser(
  val id: String,
  val email: String
)

data class SupabaseCloudState(
  val projectUrl: String = "",
  val anonKey: String = "",
  val isConfigured: Boolean = false,
  val isAuthenticated: Boolean = false,
  val user: SupabaseUser? = null,
  val isSyncing: Boolean = false,
  val statusLabel: String = "Cloud Sync",
  val lastSyncMessage: String? = null
)

data class CloudSyncPayload(
  val streakDays: Int? = null,
  val completedDates: List<String> = emptyList(),
  val waterIntent: String? = null,
  val waterSips: Int? = null,
  val gratitudeItems: List<GratitudeItem>? = null,
  val nightReflection: String? = null
)

/**
 * Native Kotlin Supabase REST + GoTrue Auth client compatible 1:1 with web/supabase.js
 * and supabase_schema.sql. Uses OkHttp + org.json + SharedPreferences with offline fallback.
 */
class SupabaseService(context: Context? = null) {

  private val prefs: SharedPreferences? = context?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
  private val http = OkHttpClient.Builder()
    .connectTimeout(15, TimeUnit.SECONDS)
    .readTimeout(20, TimeUnit.SECONDS)
    .writeTimeout(20, TimeUnit.SECONDS)
    .build()

  private var accessToken: String = prefs?.getString(KEY_ACCESS_TOKEN, "") ?: ""
  private var refreshToken: String = prefs?.getString(KEY_REFRESH_TOKEN, "") ?: ""

  private val _state = MutableStateFlow(loadInitialState())
  val state: StateFlow<SupabaseCloudState> = _state.asStateFlow()

  private fun loadInitialState(): SupabaseCloudState {
    val savedUrl = prefs?.getString(KEY_URL, "")?.trim().orEmpty()
    val savedKey = prefs?.getString(KEY_ANON_KEY, "")?.trim().orEmpty()

    val envUrl = BuildConfig.SUPABASE_URL.trim()
      .takeIf { it.isNotBlank() && !it.contains("your-project.supabase.co") }.orEmpty()
    val envKey = BuildConfig.SUPABASE_ANON_KEY.trim()
      .takeIf { it.isNotBlank() && it != "your-anon-key" }.orEmpty()

    val effectiveUrl = normalizeSupabaseUrl(savedUrl.ifBlank { envUrl })
    val effectiveKey = savedKey.ifBlank { envKey }
    val configured = effectiveUrl.startsWith("http") && effectiveKey.isNotBlank()

    val uid = prefs?.getString(KEY_USER_ID, "")?.trim().orEmpty()
    val email = prefs?.getString(KEY_USER_EMAIL, "")?.trim().orEmpty()
    val authed = configured && accessToken.isNotBlank() && uid.isNotBlank()

    return SupabaseCloudState(
      projectUrl = effectiveUrl,
      anonKey = effectiveKey,
      isConfigured = configured,
      isAuthenticated = authed,
      user = if (authed) SupabaseUser(uid, email) else null,
      statusLabel = when {
        authed -> email.substringBefore("@").ifBlank { "Synced" }
        configured -> "Sign In"
        else -> "Cloud Sync"
      }
    )
  }

  fun configure(url: String, anonKey: String) {
    val cleanUrl = normalizeSupabaseUrl(url)
    val cleanKey = anonKey.trim()
    prefs?.edit()
      ?.putString(KEY_URL, cleanUrl)
      ?.putString(KEY_ANON_KEY, cleanKey)
      ?.apply()

    val configured = cleanUrl.startsWith("http") && cleanKey.isNotBlank()
    _state.update { current ->
      val keepAuth = configured && current.user != null && accessToken.isNotBlank()
      current.copy(
        projectUrl = cleanUrl,
        anonKey = cleanKey,
        isConfigured = configured,
        isAuthenticated = keepAuth,
        statusLabel = when {
          keepAuth -> current.user?.email?.substringBefore("@") ?: "Synced"
          configured -> "Sign In"
          else -> "Cloud Sync"
        },
        lastSyncMessage = if (configured) "Supabase project connected" else "Invalid credentials"
      )
    }
  }

  fun clearConfiguration() {
    accessToken = ""
    refreshToken = ""
    prefs?.edit()
      ?.remove(KEY_URL)
      ?.remove(KEY_ANON_KEY)
      ?.remove(KEY_ACCESS_TOKEN)
      ?.remove(KEY_REFRESH_TOKEN)
      ?.remove(KEY_USER_ID)
      ?.remove(KEY_USER_EMAIL)
      ?.apply()

    _state.value = SupabaseCloudState(
      statusLabel = "Cloud Sync",
      lastSyncMessage = "Disconnected from Supabase"
    )
  }

  // --- AUTHENTICATION (GoTrue REST API) ---

  suspend fun signUp(email: String, password: String): Result<SupabaseUser> = withContext(Dispatchers.IO) {
    val st = _state.value
    if (!st.isConfigured) return@withContext Result.failure(IllegalStateException("Supabase is not configured."))

    try {
      val bodyJson = JSONObject().apply {
        put("email", email.trim())
        put("password", password)
      }
      val req = Request.Builder()
        .url("${st.projectUrl}/auth/v1/signup")
        .addHeader("apikey", st.anonKey)
        .addHeader("Content-Type", "application/json")
        .post(bodyJson.toString().toRequestBody(JSON_MEDIA))
        .build()

      http.newCall(req).execute().use { resp ->
        val raw = resp.body?.string().orEmpty()
        val json = runCatching { JSONObject(raw) }.getOrNull()
        if (!resp.isSuccessful) {
          val msg = json?.optString("msg").orEmpty()
            .ifBlank { json?.optString("error_description").orEmpty() }
            .ifBlank { json?.optString("message").orEmpty() }
            .ifBlank { "Sign up failed (${resp.code})" }
          return@withContext Result.failure(Exception(msg))
        }

        val token = json?.optString("access_token").orEmpty()
        val refresh = json?.optString("refresh_token").orEmpty()
        val userObj = json?.optJSONObject("user") ?: json
        val uid = userObj?.optString("id").orEmpty()
        val userEmail = userObj?.optString("email").orEmpty().ifBlank { email.trim() }

        if (token.isNotBlank() && uid.isNotBlank()) {
          saveSession(token, refresh, uid, userEmail)
        }
        Result.success(SupabaseUser(uid.ifBlank { "pending" }, userEmail))
      }
    } catch (e: Exception) {
      Result.failure(e)
    }
  }

  suspend fun signIn(email: String, password: String): Result<SupabaseUser> = withContext(Dispatchers.IO) {
    val st = _state.value
    if (!st.isConfigured) return@withContext Result.failure(IllegalStateException("Supabase is not configured."))

    try {
      val bodyJson = JSONObject().apply {
        put("email", email.trim())
        put("password", password)
      }
      val req = Request.Builder()
        .url("${st.projectUrl}/auth/v1/token?grant_type=password")
        .addHeader("apikey", st.anonKey)
        .addHeader("Content-Type", "application/json")
        .post(bodyJson.toString().toRequestBody(JSON_MEDIA))
        .build()

      http.newCall(req).execute().use { resp ->
        val raw = resp.body?.string().orEmpty()
        val json = runCatching { JSONObject(raw) }.getOrNull()
        if (!resp.isSuccessful) {
          val msg = json?.optString("error_description").orEmpty()
            .ifBlank { json?.optString("msg").orEmpty() }
            .ifBlank { json?.optString("message").orEmpty() }
            .ifBlank { "Sign in failed (${resp.code})" }
          return@withContext Result.failure(Exception(msg))
        }

        val token = json?.optString("access_token").orEmpty()
        val refresh = json?.optString("refresh_token").orEmpty()
        val userObj = json?.optJSONObject("user")
        val uid = userObj?.optString("id").orEmpty()
        val userEmail = userObj?.optString("email").orEmpty().ifBlank { email.trim() }

        if (token.isBlank() || uid.isBlank()) {
          return@withContext Result.failure(Exception("Invalid auth response from Supabase."))
        }

        saveSession(token, refresh, uid, userEmail)
        Result.success(SupabaseUser(uid, userEmail))
      }
    } catch (e: Exception) {
      Result.failure(e)
    }
  }

  suspend fun recoverPassword(email: String): Result<Unit> = withContext(Dispatchers.IO) {
    val st = _state.value
    if (!st.isConfigured) return@withContext Result.failure(IllegalStateException("Supabase is not configured."))

    try {
      val bodyJson = JSONObject().apply { put("email", email.trim()) }
      val req = Request.Builder()
        .url("${st.projectUrl}/auth/v1/recover")
        .addHeader("apikey", st.anonKey)
        .addHeader("Content-Type", "application/json")
        .post(bodyJson.toString().toRequestBody(JSON_MEDIA))
        .build()

      http.newCall(req).execute().use { resp ->
        if (!resp.isSuccessful) {
          val raw = resp.body?.string().orEmpty()
          val json = runCatching { JSONObject(raw) }.getOrNull()
          val msg = json?.optString("msg").orEmpty().ifBlank { "Password reset failed (${resp.code})" }
          return@withContext Result.failure(Exception(msg))
        }
        Result.success(Unit)
      }
    } catch (e: Exception) {
      Result.failure(e)
    }
  }

  suspend fun signOut() = withContext(Dispatchers.IO) {
    val st = _state.value
    if (st.isConfigured && accessToken.isNotBlank()) {
      runCatching {
        val req = Request.Builder()
          .url("${st.projectUrl}/auth/v1/logout")
          .addHeader("apikey", st.anonKey)
          .addHeader("Authorization", "Bearer $accessToken")
          .post("{}".toRequestBody(JSON_MEDIA))
          .build()
        http.newCall(req).execute().close()
      }
    }

    accessToken = ""
    refreshToken = ""
    prefs?.edit()
      ?.remove(KEY_ACCESS_TOKEN)
      ?.remove(KEY_REFRESH_TOKEN)
      ?.remove(KEY_USER_ID)
      ?.remove(KEY_USER_EMAIL)
      ?.apply()

    _state.update {
      it.copy(
        isAuthenticated = false,
        user = null,
        isSyncing = false,
        statusLabel = if (it.isConfigured) "Sign In" else "Cloud Sync",
        lastSyncMessage = "Signed out"
      )
    }
  }

  private fun saveSession(token: String, refresh: String, uid: String, email: String) {
    accessToken = token
    refreshToken = refresh
    prefs?.edit()
      ?.putString(KEY_ACCESS_TOKEN, token)
      ?.putString(KEY_REFRESH_TOKEN, refresh)
      ?.putString(KEY_USER_ID, uid)
      ?.putString(KEY_USER_EMAIL, email)
      ?.apply()

    _state.update {
      it.copy(
        isAuthenticated = true,
        user = SupabaseUser(uid, email),
        statusLabel = email.substringBefore("@").ifBlank { "Synced" },
        lastSyncMessage = "Authenticated"
      )
    }
  }

  private fun refreshSessionBlocking(): Boolean {
    val st = _state.value
    if (!st.isConfigured || refreshToken.isBlank()) return false
    return runCatching {
      val body = JSONObject().apply { put("refresh_token", refreshToken) }
      val req = Request.Builder()
        .url("${st.projectUrl}/auth/v1/token?grant_type=refresh_token")
        .addHeader("apikey", st.anonKey)
        .addHeader("Content-Type", "application/json")
        .post(body.toString().toRequestBody(JSON_MEDIA))
        .build()

      http.newCall(req).execute().use { resp ->
        if (!resp.isSuccessful) return@use false
        val json = JSONObject(resp.body?.string().orEmpty())
        val token = json.optString("access_token")
        val refresh = json.optString("refresh_token").ifBlank { refreshToken }
        val userObj = json.optJSONObject("user")
        val uid = userObj?.optString("id").orEmpty().ifBlank { st.user?.id.orEmpty() }
        val email = userObj?.optString("email").orEmpty().ifBlank { st.user?.email.orEmpty() }
        if (token.isNotBlank() && uid.isNotBlank()) {
          saveSession(token, refresh, uid, email)
          true
        } else false
      }
    }.getOrDefault(false)
  }

  // --- SYNC METHODS (PostgREST API) ---

  suspend fun syncDown(): CloudSyncPayload? = withContext(Dispatchers.IO) {
    val st = _state.value
    val user = st.user
    if (!st.isConfigured || !st.isAuthenticated || user == null) return@withContext null

    _state.update { it.copy(isSyncing = true) }
    try {
      val today = todayUtcString()

      // 1. Fetch Profile
      val profileArr = getJsonArray("${st.projectUrl}/rest/v1/profiles?id=eq.${user.id}&select=*&limit=1")
      val profile = profileArr?.optJSONObject(0)
      val streakDays = if (profile != null && !profile.isNull("streak_days")) profile.optInt("streak_days") else null
      val completedDates = mutableListOf<String>()
      profile?.optJSONArray("completed_dates")?.let { arr ->
        for (i in 0 until arr.length()) {
          val d = arr.optString(i).trim()
          if (d.isNotBlank()) completedDates.add(d)
        }
      }
      var waterIntent = profile?.optString("water_intent")?.takeIf { it.isNotBlank() && it != "null" }
      var waterSips = if (profile != null && !profile.isNull("water_sips")) profile.optInt("water_sips") else null

      // 2. Fetch Today's Gratitude Entries
      val gratitudeArr = getJsonArray(
        "${st.projectUrl}/rest/v1/gratitude_entries?user_id=eq.${user.id}&entry_date=eq.$today&select=*&limit=1"
      )
      val gratitudeRow = gratitudeArr?.optJSONObject(0)
      val parsedGratitudes = gratitudeRow?.optJSONArray("items")?.let { itemsArr ->
        val list = mutableListOf<GratitudeItem>()
        for (i in 0 until itemsArr.length()) {
          val obj = itemsArr.optJSONObject(i) ?: continue
          val id = obj.optInt("id", i + 1)
          val blessing = obj.optString("blessing").trim()
          if (blessing.isBlank()) continue
          val reason = obj.optString("reason").takeIf { it.isNotBlank() && it != "null" }
            ?: "it fills my heart with deep gratitude and harmony"
          val done = if (obj.has("done")) obj.optBoolean("done", false) else obj.optBoolean("isCompleted", false)
          list.add(GratitudeItem(id = id, blessing = blessing, reason = reason, isCompleted = done))
        }
        list.takeIf { it.isNotEmpty() }
      }

      // 3. Fetch Today's Water Intention (override profile if today's row exists)
      val waterArr = getJsonArray(
        "${st.projectUrl}/rest/v1/water_intentions?user_id=eq.${user.id}&entry_date=eq.$today&select=*&limit=1"
      )
      waterArr?.optJSONObject(0)?.let { wObj ->
        wObj.optString("intention").takeIf { it.isNotBlank() && it != "null" }?.let { waterIntent = it }
        if (!wObj.isNull("sips")) waterSips = wObj.optInt("sips")
      }

      // 4. Fetch Today's Night Reflection
      val nightArr = getJsonArray(
        "${st.projectUrl}/rest/v1/night_reflections?user_id=eq.${user.id}&entry_date=eq.$today&select=*&limit=1"
      )
      val nightReflection = nightArr?.optJSONObject(0)
        ?.optString("reflection")
        ?.takeIf { it.isNotBlank() && it != "null" }

      _state.update {
        it.copy(
          isSyncing = false,
          lastSyncMessage = "Cloud sync complete"
        )
      }

      CloudSyncPayload(
        streakDays = streakDays,
        completedDates = completedDates,
        waterIntent = waterIntent,
        waterSips = waterSips,
        gratitudeItems = parsedGratitudes,
        nightReflection = nightReflection
      )
    } catch (e: Exception) {
      _state.update {
        it.copy(
          isSyncing = false,
          lastSyncMessage = "Offline / sync skipped"
        )
      }
      null
    }
  }

  suspend fun syncUpProfile(
    streakDays: Int,
    completedDates: Collection<String>,
    waterIntent: String,
    waterSips: Int
  ) = withContext(Dispatchers.IO) {
    val st = _state.value
    val user = st.user ?: return@withContext
    if (!st.isConfigured || !st.isAuthenticated) return@withContext

    val datesArr = JSONArray().apply { completedDates.forEach { put(it) } }
    val body = JSONObject().apply {
      put("id", user.id)
      put("email", user.email)
      put("streak_days", streakDays)
      put("completed_dates", datesArr)
      put("water_intent", waterIntent)
      put("water_sips", waterSips)
      put("updated_at", nowIsoUtc())
    }
    postJson("${st.projectUrl}/rest/v1/profiles?on_conflict=id", body, upsert = true)
  }

  suspend fun syncGratitude(items: List<GratitudeItem>) = withContext(Dispatchers.IO) {
    val st = _state.value
    val user = st.user ?: return@withContext
    if (!st.isConfigured || !st.isAuthenticated) return@withContext

    val itemsArr = JSONArray().apply {
      items.forEach { item ->
        put(JSONObject().apply {
          put("id", item.id)
          put("blessing", item.blessing)
          put("reason", item.reason)
          put("done", item.isCompleted)
        })
      }
    }
    val body = JSONObject().apply {
      put("user_id", user.id)
      put("entry_date", todayUtcString())
      put("items", itemsArr)
      put("updated_at", nowIsoUtc())
    }
    postJson("${st.projectUrl}/rest/v1/gratitude_entries?on_conflict=user_id,entry_date", body, upsert = true)
  }

  suspend fun syncWater(intent: String, sips: Int) = withContext(Dispatchers.IO) {
    val st = _state.value
    val user = st.user ?: return@withContext
    if (!st.isConfigured || !st.isAuthenticated) return@withContext

    val body = JSONObject().apply {
      put("user_id", user.id)
      put("entry_date", todayUtcString())
      put("intention", intent)
      put("sips", sips)
      put("updated_at", nowIsoUtc())
    }
    postJson("${st.projectUrl}/rest/v1/water_intentions?on_conflict=user_id,entry_date", body, upsert = true)
  }

  suspend fun syncNightReflection(reflection: String) = withContext(Dispatchers.IO) {
    val st = _state.value
    val user = st.user ?: return@withContext
    if (!st.isConfigured || !st.isAuthenticated) return@withContext

    val body = JSONObject().apply {
      put("user_id", user.id)
      put("entry_date", todayUtcString())
      put("reflection", reflection)
      put("updated_at", nowIsoUtc())
    }
    postJson("${st.projectUrl}/rest/v1/night_reflections?on_conflict=user_id,entry_date", body, upsert = true)
  }

  suspend fun logCompletedSession(sessionType: String) = withContext(Dispatchers.IO) {
    val st = _state.value
    val user = st.user ?: return@withContext
    if (!st.isConfigured || !st.isAuthenticated) return@withContext

    val body = JSONObject().apply {
      put("user_id", user.id)
      put("session_type", sessionType)
      put("entry_date", todayUtcString())
      put("completed_at", nowIsoUtc())
    }
    postJson("${st.projectUrl}/rest/v1/completed_sessions", body, upsert = false)
  }

  // --- HTTP HELPERS ---

  private fun getJsonArray(url: String, retryOn401: Boolean = true): JSONArray? {
    val st = _state.value
    val req = Request.Builder()
      .url(url)
      .addHeader("apikey", st.anonKey)
      .addHeader("Authorization", "Bearer $accessToken")
      .get()
      .build()

    http.newCall(req).execute().use { resp ->
      if (resp.code == 401 && retryOn401 && refreshSessionBlocking()) {
        return getJsonArray(url, retryOn401 = false)
      }
      if (!resp.isSuccessful) return null
      val raw = resp.body?.string().orEmpty().trim()
      return if (raw.startsWith("[")) JSONArray(raw) else null
    }
  }

  private fun postJson(url: String, body: JSONObject, upsert: Boolean, retryOn401: Boolean = true): Boolean {
    val st = _state.value
    val builder = Request.Builder()
      .url(url)
      .addHeader("apikey", st.anonKey)
      .addHeader("Authorization", "Bearer $accessToken")
      .addHeader("Content-Type", "application/json")
      .post(body.toString().toRequestBody(JSON_MEDIA))

    if (upsert) {
      builder.addHeader("Prefer", "resolution=merge-duplicates,return=minimal")
    }

    return runCatching {
      http.newCall(builder.build()).execute().use { resp ->
        if (resp.code == 401 && retryOn401 && refreshSessionBlocking()) {
          return@use postJson(url, body, upsert, retryOn401 = false)
        }
        resp.isSuccessful
      }
    }.getOrDefault(false)
  }

  companion object {
    private const val PREFS_NAME = "miracle_mornings_supabase_prefs"
    private const val KEY_URL = "mm_supabase_url"
    private const val KEY_ANON_KEY = "mm_supabase_key"
    private const val KEY_ACCESS_TOKEN = "mm_sb_access_token"
    private const val KEY_REFRESH_TOKEN = "mm_sb_refresh_token"
    private const val KEY_USER_ID = "mm_sb_user_id"
    private const val KEY_USER_EMAIL = "mm_sb_user_email"
    private val JSON_MEDIA = "application/json; charset=utf-8".toMediaType()

    fun normalizeSupabaseUrl(raw: String): String {
      var clean = raw.trim().trimEnd('/')
      for (suffix in listOf("/rest/v1", "/auth/v1")) {
        if (clean.endsWith(suffix)) {
          clean = clean.removeSuffix(suffix).trimEnd('/')
        }
      }
      return clean
    }

    fun todayUtcString(): String {
      val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
      sdf.timeZone = TimeZone.getTimeZone("UTC")
      return sdf.format(Date())
    }

    fun nowIsoUtc(): String {
      val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
      sdf.timeZone = TimeZone.getTimeZone("UTC")
      return sdf.format(Date())
    }
  }
}
