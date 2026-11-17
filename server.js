const http = require('http');
const fs = require('fs');
const path = require('path');

const PORT = 3000;
const HOST = '0.0.0.0';
const PUBLIC_DIR = path.join(__dirname, 'web');

const MIME_TYPES = {
  '.html': 'text/html; charset=utf-8',
  '.css': 'text/css; charset=utf-8',
  '.js': 'application/javascript; charset=utf-8',
  '.json': 'application/json; charset=utf-8',
  '.png': 'image/png',
  '.jpg': 'image/jpeg',
  '.svg': 'image/svg+xml',
  '.ico': 'image/x-icon'
};

function loadDotEnv() {
  const envMap = {};
  try {
    const envPath = path.join(__dirname, '.env');
    if (fs.existsSync(envPath)) {
      const lines = fs.readFileSync(envPath, 'utf8').split(/\r?\n/);
      for (const line of lines) {
        const trimmed = line.trim();
        if (!trimmed || trimmed.startsWith('#')) continue;
        const eqIdx = trimmed.indexOf('=');
        if (eqIdx > 0) {
          const k = trimmed.slice(0, eqIdx).trim();
          const v = trimmed.slice(eqIdx + 1).trim().replace(/^["']|["']$/g, '');
          envMap[k] = v;
        }
      }
    }
  } catch (e) {}
  return envMap;
}

function normalizeUrl(raw) {
  let clean = (raw || '').trim().replace(/\/+$/, '');
  clean = clean.replace(/\/(rest|auth)\/v1$/i, '').replace(/\/+$/, '');
  return clean;
}

const server = http.createServer((req, res) => {
  let reqPath = req.url.split('?')[0];

  if (reqPath === '/api/config.js') {
    const dotEnv = loadDotEnv();
    const url = normalizeUrl(
      process.env.SUPABASE_URL ||
      process.env.NEXT_PUBLIC_SUPABASE_URL ||
      dotEnv.SUPABASE_URL ||
      dotEnv.NEXT_PUBLIC_SUPABASE_URL ||
      ''
    );
    const rawKey = (
      process.env.SUPABASE_ANON_KEY ||
      process.env.NEXT_PUBLIC_SUPABASE_ANON_KEY ||
      dotEnv.SUPABASE_ANON_KEY ||
      dotEnv.NEXT_PUBLIC_SUPABASE_ANON_KEY ||
      ''
    ).trim();

    const safePayload = {
      url: (!url || url.includes('your-project.supabase.co')) ? '' : url,
      anonKey: (!rawKey || rawKey === 'your-anon-key' || rawKey.startsWith('sb_secret_')) ? '' : rawKey
    };

    res.writeHead(200, {
      'Content-Type': 'application/javascript; charset=utf-8',
      'Cache-Control': 'no-store',
      'Access-Control-Allow-Origin': '*'
    });
    res.end(`window.MM_SUPABASE_CONFIG = ${JSON.stringify(safePayload)};`);
    return;
  }

  if (reqPath === '/') reqPath = '/index.html';
  else if (reqPath === '/privacy') reqPath = '/privacy.html';
  else if (reqPath === '/terms') reqPath = '/terms.html';
  
  const filePath = path.join(PUBLIC_DIR, reqPath);
  
  fs.readFile(filePath, (err, data) => {
    if (err) {
      if (reqPath !== '/index.html') {
        // Fallback for SPA routing
        fs.readFile(path.join(PUBLIC_DIR, 'index.html'), (fallbackErr, fallbackData) => {
          if (fallbackErr) {
            res.writeHead(404, { 'Content-Type': 'text/plain' });
            res.end('404 Not Found');
          } else {
            res.writeHead(200, { 'Content-Type': 'text/html; charset=utf-8' });
            res.end(fallbackData);
          }
        });
        return;
      }
      res.writeHead(404, { 'Content-Type': 'text/plain' });
      res.end('404 Not Found');
      return;
    }

    const ext = path.extname(filePath).toLowerCase();
    const contentType = MIME_TYPES[ext] || 'application/octet-stream';

    res.writeHead(200, {
      'Content-Type': contentType,
      'Access-Control-Allow-Origin': '*'
    });
    res.end(data);
  });
});

server.listen(PORT, HOST, () => {
  console.log(`Miracle Mornings Web Server listening on http://${HOST}:${PORT}`);
});
