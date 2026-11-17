// Vercel Serverless Function — Exposes public runtime Supabase configuration safely.
// Never exposes private/secret service keys (e.g., sb_secret_*) to browser clients.

function normalizeUrl(raw) {
  let clean = (raw || '').trim().replace(/\/+$/, '');
  clean = clean.replace(/\/(rest|auth)\/v1$/i, '').replace(/\/+$/, '');
  return clean;
}

module.exports = (req, res) => {
  const url = normalizeUrl(
    process.env.SUPABASE_URL ||
      process.env.NEXT_PUBLIC_SUPABASE_URL ||
      ''
  );
  const rawKey = (
    process.env.SUPABASE_ANON_KEY ||
    process.env.NEXT_PUBLIC_SUPABASE_ANON_KEY ||
    ''
  ).trim();

  const isPlaceholderUrl = !url || url.includes('your-project.supabase.co');
  const isPlaceholderKey = !rawKey || rawKey === 'your-anon-key' || rawKey.startsWith('sb_secret_');

  const payload = {
    url: isPlaceholderUrl ? '' : url,
    anonKey: isPlaceholderKey ? '' : rawKey
  };

  res.setHeader('Content-Type', 'application/javascript; charset=utf-8');
  res.setHeader('Cache-Control', 'no-store');
  res.status(200).send(`window.MM_SUPABASE_CONFIG = ${JSON.stringify(payload)};`);
};
