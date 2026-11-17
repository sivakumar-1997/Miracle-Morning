package com.example.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.Locale
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.concurrent.thread
import kotlin.math.PI
import kotlin.math.sin

class SacredSoundEngine(context: Context) : TextToSpeech.OnInitListener {

  private val mainHandler = Handler(Looper.getMainLooper())

  // Ambient 432 Hz Engine State
  private val isAmbientRunning = AtomicBoolean(false)
  private var ambientThread: Thread? = null
  private var ambientTrack: AudioTrack? = null
  private val ambientLock = Any()

  // Chakra Sound Engine State
  private val isChakraRunning = AtomicBoolean(false)
  private var chakraThread: Thread? = null
  private var chakraTrack: AudioTrack? = null
  private val chakraLock = Any()

  // Text-To-Speech State
  private var tts: TextToSpeech? = null
  private var isTtsReady = false
  private var pendingMantraRunnable: Runnable? = null

  // Ho'oponopono Voice State
  private var hooponoponoRunnable: Runnable? = null
  private var isHooponoponoActive = false

  init {
    try {
      tts = TextToSpeech(context.applicationContext, this)
    } catch (t: Throwable) {
      Log.e("SacredSoundEngine", "Failed to initialize TTS", t)
    }
  }

  override fun onInit(status: Int) {
    if (status == TextToSpeech.SUCCESS) {
      try {
        val result = tts?.setLanguage(Locale.US)
        if (result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED) {
          selectNaturalFemaleVoice()
          tts?.setSpeechRate(0.84f) // Soothing, clear human cadence
          tts?.setPitch(1.10f)     // Warm, natural female pitch
          isTtsReady = true
        }
      } catch (t: Throwable) {
        Log.e("SacredSoundEngine", "Error configuring TTS", t)
      }
    }
  }

  /**
   * Discovers and selects the most natural, human-like female voice available on the device.
   */
  fun selectNaturalFemaleVoice() {
    try {
      val voices = tts?.voices ?: return
      // Priority 1: High quality English female voice (e.g. Google network or local female voice)
      val femaleVoice = voices.filter { voice ->
        val loc = voice.locale
        (loc.language == "en" || loc == Locale.US) && !voice.isNetworkConnectionRequired
      }.sortedWith(
        compareByDescending<android.speech.tts.Voice> { v ->
          val n = v.name.lowercase()
          var score = 0
          if (n.contains("female") || n.contains("#female")) score += 20
          if (n.contains("en-us") || n.contains("en_us")) score += 5
          if (n.contains("sfg") || n.contains("tpd") || n.contains("iol") || n.contains("aura") || n.contains("calm")) score += 8
          if (v.quality >= android.speech.tts.Voice.QUALITY_HIGH) score += 6
          score
        }
      ).firstOrNull { v ->
        val n = v.name.lowercase()
        n.contains("female") || n.contains("#female") || n.contains("sfg") || n.contains("tpd") || n.contains("iol") || n.contains("en-us")
      } ?: voices.firstOrNull { v ->
        v.name.lowercase().contains("female")
      }

      if (femaleVoice != null) {
        tts?.voice = femaleVoice
        Log.i("SacredSoundEngine", "Selected natural female voice: ${femaleVoice.name}")
      }
    } catch (t: Throwable) {
      Log.e("SacredSoundEngine", "Could not set female voice", t)
    }
  }

  /**
   * Starts the continuous 432 Hz ambient meditative soundscape using safe chunk streaming.
   * Universal stereo PCM_16BIT output with exact frame alignment to prevent native audio errors.
   */
  fun start432HzAmbient(onStarted: (() -> Unit)? = null) {
    stopContinuousTone()

    synchronized(ambientLock) {
      isAmbientRunning.set(true)
      ambientThread = thread(name = "Ambient432HzThread") {
        var track: AudioTrack? = null
        try {
          val sampleRate = 44100
          val channelMask = AudioFormat.CHANNEL_OUT_STEREO
          val encoding = AudioFormat.ENCODING_PCM_16BIT
          val frameSize = 4 // 2 channels * 2 bytes per sample
          val minBuf = AudioTrack.getMinBufferSize(sampleRate, channelMask, encoding)
          val bufferSizeInBytes = (maxOf(minBuf * 2, 16384) / frameSize) * frameSize

          track = AudioTrack.Builder()
            .setAudioAttributes(
              AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()
            )
            .setAudioFormat(
              AudioFormat.Builder()
                .setEncoding(encoding)
                .setSampleRate(sampleRate)
                .setChannelMask(channelMask)
                .build()
            )
            .setBufferSizeInBytes(bufferSizeInBytes)
            .setTransferMode(AudioTrack.MODE_STREAM)
            .build()

          if (track.state != AudioTrack.STATE_INITIALIZED) {
            Log.e("SacredSoundEngine", "Ambient track failed to initialize")
            try { track.release() } catch (_: Throwable) {}
            return@thread
          }

          synchronized(ambientLock) {
            ambientTrack = track
          }

          try {
            track.setVolume(1.0f)
          } catch (_: Throwable) {}

          track.play()
          onStarted?.let { mainHandler.post(it) }

          val chunkFrames = 1024
          val buffer = ShortArray(chunkFrames * 2) // Stereo: Left + Right interleaved

          // 432 Hz Sacred Harmonics multi-octave frequencies for rich meditative soundscape
          val freqSub = 108.0    // Deep meditative ground om
          val freqBass = 216.0   // Warm cello/tanpura octave
          val freqFifth = 324.0  // Cosmic fifth harmony
          val freqRoot = 432.0   // Sacred heart fundamental
          val freqThird = 540.0  // Celestial warm major third
          val freqChime = 864.0  // High crystal singing bowl chime
          val freqChimeHigh = 1296.0

          // Phases for rich multi-voice polyphony
          var phaseSub = 0.0
          var phaseBass = 0.0
          var phaseFifth = 0.0
          var phaseRoot = 0.0
          var phaseThird = 0.0
          var phaseChime = 0.0
          var phaseChimeHigh = 0.0

          // LFO phases for breathing and organic chorus movement
          var phaseLfoSlow = 0.0   // 0.08 Hz (12s slow breathing swell)
          var phaseLfoChorus = 0.0 // 0.22 Hz gentle acoustic shimmer
          var phaseChimeEnv = 0.0  // 0.16 Hz singing bowl resonance cycle

          // Soft pink noise filter state for natural temple breeze texture
          var b0 = 0.0
          var b1 = 0.0
          var b2 = 0.0
          val random = java.util.Random(432)

          while (isAmbientRunning.get()) {
            for (i in 0 until chunkFrames) {
              // 1. Slow meditative prana breathing swell (0.08 Hz)
              val breathEnvelope = 0.55 + 0.18 * sin(2.0 * PI * phaseLfoSlow)

              // 2. Gentle choral chorus swell
              val chorusDetune = 1.0 + 0.0016 * sin(2.0 * PI * phaseLfoChorus)

              // 3. Periodic crystal singing bowl harmonic bell chime (peaks gently every 6.2s)
              val chimeCycle = sin(2.0 * PI * phaseChimeEnv)
              val chimeIntensity = if (chimeCycle > 0.4) Math.pow((chimeCycle - 0.4) / 0.6, 2.0) else 0.0

              // 4. Subtle temple breeze pink noise texture
              val white = (random.nextDouble() * 2.0 - 1.0)
              b0 = 0.99765 * b0 + white * 0.0990460
              b1 = 0.96300 * b1 + white * 0.2965164
              b2 = 0.57000 * b2 + white * 1.0526913
              val pinkNoise = (b0 + b1 + b2 + white * 0.1848) * 0.018

              // 5. Lush multi-layered celestial sacred chord (Left and Right slightly detuned for stereo depth)
              val chordWaveL = (
                sin(2.0 * PI * phaseSub) * 0.28 +
                sin(2.0 * PI * phaseBass) * 0.26 +
                sin(2.0 * PI * phaseFifth) * 0.22 +
                sin(2.0 * PI * phaseRoot) * 0.26 +
                sin(2.0 * PI * phaseThird) * 0.14 +
                (sin(2.0 * PI * phaseChime) * 0.09 + sin(2.0 * PI * phaseChimeHigh) * 0.05) * chimeIntensity
              )

              val chordWaveR = (
                sin(2.0 * PI * phaseSub) * 0.28 +
                sin(2.0 * PI * (phaseBass * 1.001)) * 0.26 +
                sin(2.0 * PI * phaseFifth) * 0.22 +
                sin(2.0 * PI * (phaseRoot * 0.999)) * 0.26 +
                sin(2.0 * PI * phaseThird) * 0.14 +
                (sin(2.0 * PI * phaseChime) * 0.09 + sin(2.0 * PI * phaseChimeHigh) * 0.05) * chimeIntensity
              )

              val sampleL = ((chordWaveL * breathEnvelope + pinkNoise) * chorusDetune * 24000.0).toInt().coerceIn(-31000, 31000).toShort()
              val sampleR = ((chordWaveR * breathEnvelope + pinkNoise) * chorusDetune * 24000.0).toInt().coerceIn(-31000, 31000).toShort()

              buffer[2 * i] = sampleL
              buffer[2 * i + 1] = sampleR

              // Advance oscillator phases
              phaseSub += freqSub / sampleRate
              if (phaseSub >= 1.0) phaseSub -= 1.0

              phaseBass += (freqBass * chorusDetune) / sampleRate
              if (phaseBass >= 1.0) phaseBass -= 1.0

              phaseFifth += freqFifth / sampleRate
              if (phaseFifth >= 1.0) phaseFifth -= 1.0

              phaseRoot += freqRoot / sampleRate
              if (phaseRoot >= 1.0) phaseRoot -= 1.0

              phaseThird += (freqThird / chorusDetune) / sampleRate
              if (phaseThird >= 1.0) phaseThird -= 1.0

              phaseChime += freqChime / sampleRate
              if (phaseChime >= 1.0) phaseChime -= 1.0

              phaseChimeHigh += freqChimeHigh / sampleRate
              if (phaseChimeHigh >= 1.0) phaseChimeHigh -= 1.0

              // Advance LFO phases
              phaseLfoSlow += 0.08 / sampleRate
              if (phaseLfoSlow >= 1.0) phaseLfoSlow -= 1.0

              phaseLfoChorus += 0.22 / sampleRate
              if (phaseLfoChorus >= 1.0) phaseLfoChorus -= 1.0

              phaseChimeEnv += 0.16 / sampleRate
              if (phaseChimeEnv >= 1.0) phaseChimeEnv -= 1.0
            }

            if (!isAmbientRunning.get()) break
            val written = track.write(buffer, 0, buffer.size)
            if (written < 0) break
          }
        } catch (t: Throwable) {
          Log.e("SacredSoundEngine", "Error in ambient audio thread", t)
        } finally {
          try {
            track?.stop()
          } catch (_: Throwable) {}
          try {
            track?.release()
          } catch (_: Throwable) {}
          synchronized(ambientLock) {
            if (ambientTrack == track) ambientTrack = null
          }
        }
      }
    }
  }

  fun stopContinuousTone() {
    isAmbientRunning.set(false)
    try {
      ambientThread?.interrupt()
    } catch (_: Throwable) {}
  }

  /**
   * Plays the authentic 7 Chakras pure Solfeggio frequency sound with harmonic singing bowl resonance.
   * Completely free of vocal chants ("LAM", "VAM", etc.), providing pure therapeutic frequencies.
   * Stereo output with frame-aligned buffer sizing for 100% crash immunity and clear audibility.
   */
  fun playChakraSound(
    freqHz: Double,
    seedMantra: String = "",
    chakraName: String = "",
    durationSeconds: Double = 6.0,
    includeMantraChant: Boolean = false
  ) {
    stopChakraSound()

    // Streamed pure Solfeggio singing bowl chime with organic frequency harmonics
    synchronized(chakraLock) {
      isChakraRunning.set(true)
      chakraThread = thread(name = "ChakraStreamThread") {
        var track: AudioTrack? = null
        try {
          val sampleRate = 44100
          val channelMask = AudioFormat.CHANNEL_OUT_STEREO
          val encoding = AudioFormat.ENCODING_PCM_16BIT
          val frameSize = 4 // 2 channels * 2 bytes per sample
          val minBuf = AudioTrack.getMinBufferSize(sampleRate, channelMask, encoding)
          val bufferSizeInBytes = (maxOf(minBuf * 2, 16384) / frameSize) * frameSize

          track = AudioTrack.Builder()
            .setAudioAttributes(
              AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()
            )
            .setAudioFormat(
              AudioFormat.Builder()
                .setEncoding(encoding)
                .setSampleRate(sampleRate)
                .setChannelMask(channelMask)
                .build()
            )
            .setBufferSizeInBytes(bufferSizeInBytes)
            .setTransferMode(AudioTrack.MODE_STREAM)
            .build()

          if (track.state != AudioTrack.STATE_INITIALIZED) {
            Log.e("SacredSoundEngine", "Chakra track failed to initialize")
            try { track.release() } catch (_: Throwable) {}
            return@thread
          }

          synchronized(chakraLock) {
            chakraTrack = track
          }

          try {
            track.setVolume(1.0f)
          } catch (_: Throwable) {}

          track.play()

          val chunkFrames = 1024
          val buffer = ShortArray(chunkFrames * 2)

          val fundamental = freqHz
          val subharmonic = freqHz * 0.5
          val overtoneFifth = freqHz * 1.5
          val overtoneOctave = freqHz * 2.0
          val overtoneHigh = freqHz * 2.756
          val tremoloRate = 4.8

          val totalSamples = (sampleRate * durationSeconds).toInt()
          val attackSamples = (sampleRate * 0.15).toInt()
          val releaseSamples = (sampleRate * 0.35).toInt()

          var samplesPlayed = 0
          var phase0 = 0.0
          var phaseSub = 0.0
          var phaseFifth = 0.0
          var phaseOctave = 0.0
          var phaseHigh = 0.0
          var phaseTremolo = 0.0

          while (isChakraRunning.get() && samplesPlayed < totalSamples) {
            val framesToWrite = Math.min(chunkFrames, totalSamples - samplesPlayed)

            for (i in 0 until framesToWrite) {
              val currentSample = samplesPlayed + i
              val t = currentSample.toDouble() / sampleRate

              // Smooth attack and release envelopes
              val attack = if (currentSample < attackSamples) {
                0.5 * (1.0 - Math.cos(PI * currentSample / attackSamples))
              } else 1.0

              val release = if (currentSample > totalSamples - releaseSamples) {
                val rIndex = totalSamples - currentSample
                0.5 * (1.0 - Math.cos(PI * rIndex / releaseSamples))
              } else 1.0

              // Gentle acoustic singing bowl decay curve
              val envFundamental = Math.exp(-t * 0.22)
              val envSub = Math.exp(-t * 0.25)
              val envFifth = Math.exp(-t * 0.55)
              val envOctave = Math.exp(-t * 0.45)
              val envHigh = Math.exp(-t * 0.85)

              val tremolo = 0.95 + 0.05 * sin(2.0 * PI * phaseTremolo)

              // Pure Solfeggio sinusoidal wave with rich singing bowl acoustic overtones
              val waveL = (
                sin(2.0 * PI * phase0) * 0.68 * envFundamental +
                sin(2.0 * PI * phaseSub) * 0.18 * envSub +
                sin(2.0 * PI * phaseFifth) * 0.10 * envFifth +
                sin(2.0 * PI * phaseOctave) * 0.08 * envOctave +
                sin(2.0 * PI * phaseHigh) * 0.04 * envHigh
              ) * attack * release * tremolo

              // Right channel slight acoustic chorus for wide spacious resonance
              val waveR = (
                sin(2.0 * PI * (phase0 * 1.0008)) * 0.68 * envFundamental +
                sin(2.0 * PI * phaseSub) * 0.18 * envSub +
                sin(2.0 * PI * phaseFifth) * 0.10 * envFifth +
                sin(2.0 * PI * (phaseOctave * 0.9992)) * 0.08 * envOctave +
                sin(2.0 * PI * phaseHigh) * 0.04 * envHigh
              ) * attack * release * tremolo

              val sampleL = (waveL * 28000.0).toInt().coerceIn(-32000, 32000).toShort()
              val sampleR = (waveR * 28000.0).toInt().coerceIn(-32000, 32000).toShort()

              buffer[2 * i] = sampleL
              buffer[2 * i + 1] = sampleR

              phase0 += fundamental / sampleRate
              if (phase0 >= 1.0) phase0 -= 1.0

              phaseSub += subharmonic / sampleRate
              if (phaseSub >= 1.0) phaseSub -= 1.0

              phaseFifth += overtoneFifth / sampleRate
              if (phaseFifth >= 1.0) phaseFifth -= 1.0

              phaseOctave += overtoneOctave / sampleRate
              if (phaseOctave >= 1.0) phaseOctave -= 1.0

              phaseHigh += overtoneHigh / sampleRate
              if (phaseHigh >= 1.0) phaseHigh -= 1.0

              phaseTremolo += tremoloRate / sampleRate
              if (phaseTremolo >= 1.0) phaseTremolo -= 1.0
            }

            if (!isChakraRunning.get()) break
            val written = track.write(buffer, 0, framesToWrite * 2)
            if (written < 0) break
            samplesPlayed += framesToWrite
          }
        } catch (t: Throwable) {
          Log.e("SacredSoundEngine", "Error streaming chakra tone", t)
        } finally {
          try {
            track?.stop()
          } catch (_: Throwable) {}
          try {
            track?.release()
          } catch (_: Throwable) {}
          synchronized(chakraLock) {
            if (chakraTrack == track) chakraTrack = null
          }
        }
      }
    }
  }

  fun stopChakraSound() {
    isChakraRunning.set(false)
    pendingMantraRunnable?.let {
      mainHandler.removeCallbacks(it)
      pendingMantraRunnable = null
    }
    try {
      chakraThread?.interrupt()
    } catch (_: Throwable) {}
  }

  /**
   * Backwards compatible singing bowl player.
   */
  fun playChakraSingingBowl(freqHz: Double, durationSeconds: Double = 4.5) {
    playChakraSound(freqHz, seedMantra = "OM", durationSeconds = durationSeconds, includeMantraChant = false)
  }

  /**
   * Chants the sacred Seed Mantra alone.
   */
  fun chantSeedMantraAlone(seedMantra: String) {
    if (!isTtsReady || tts == null) return
    val chantText = when (seedMantra.uppercase().trim()) {
      "LAM" -> "Luhm. Laaam."
      "VAM" -> "Vuhm. Vaaam."
      "RAM" -> "Raaam."
      "YAM" -> "Yaaam."
      "HAM" -> "Haaam."
      "OM" -> "Aum. Oooom."
      "AH" -> "Aaaah."
      else -> seedMantra
    }
    try {
      tts?.setPitch(0.85f)
      tts?.setSpeechRate(0.60f)
      tts?.speak(chantText, TextToSpeech.QUEUE_FLUSH, null, "seed_mantra_solo")
    } catch (t: Throwable) {
      Log.e("SacredSoundEngine", "Error chanting mantra alone", t)
    }
  }

  /**
   * Starts Ho'oponopono guided speech recital:
   * "I am sorry. Please forgive me. Thank you. I love you."
   */
  fun startHooponoponoVoiceLoop(includeVoice: Boolean) {
    start432HzAmbient()

    if (!includeVoice) {
      stopVoiceRecitation()
      return
    }

    isHooponoponoActive = true
    selectNaturalFemaleVoice()
    tts?.setPitch(1.10f)     // Natural warm female vocal pitch
    tts?.setSpeechRate(0.84f) // Soothing, mindful human recitation cadence

    val phrases = listOf(
      "I am sorry.",
      "Please forgive me.",
      "Thank you.",
      "I love you."
    )

    var phraseIndex = 0
    val loop = object : Runnable {
      override fun run() {
        if (!isHooponoponoActive) return

        if (isTtsReady && tts != null) {
          try {
            selectNaturalFemaleVoice()
            tts?.setPitch(1.10f)
            tts?.setSpeechRate(0.84f)
            val phrase = phrases[phraseIndex]
            val params = android.os.Bundle().apply {
              putFloat(TextToSpeech.Engine.KEY_PARAM_VOLUME, 1.0f)
            }
            tts?.speak(phrase, TextToSpeech.QUEUE_FLUSH, params, "hooponopono_$phraseIndex")
            phraseIndex = (phraseIndex + 1) % phrases.size
          } catch (t: Throwable) {
            Log.e("SacredSoundEngine", "Error in Ho'oponopono loop speech", t)
          }
        }

        if (isHooponoponoActive) {
          mainHandler.postDelayed(this, 4800)
        }
      }
    }

    hooponoponoRunnable = loop
    mainHandler.postDelayed(loop, 1200)
  }

  fun stopVoiceRecitation() {
    isHooponoponoActive = false
    hooponoponoRunnable?.let { mainHandler.removeCallbacks(it) }
    hooponoponoRunnable = null
    try {
      tts?.stop()
    } catch (_: Throwable) {}
  }

  fun stopAll() {
    stopVoiceRecitation()
    stopContinuousTone()
    stopChakraSound()
  }

  fun release() {
    stopAll()
    try {
      tts?.shutdown()
    } catch (_: Throwable) {}
    tts = null
  }
}
