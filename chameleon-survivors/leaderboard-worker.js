// Global online leaderboard for Chameleon Survivors — Cloudflare Worker.
//
// Setup (free tier is plenty):
//   1) npm i -g wrangler  (or use the Cloudflare dashboard)
//   2) Create a KV namespace and bind it as  SCORES
//        wrangler kv namespace create SCORES
//      then add the binding to wrangler.toml:
//        [[kv_namespaces]]
//        binding = "SCORES"
//        id = "<the id printed above>"
//   3) wrangler deploy
//   4) Put the deployed URL into  LB_ENDPOINT  in index.html, e.g.
//        const LB_ENDPOINT = "https://chameleon-lb.<you>.workers.dev";
//
// Endpoints:  GET /top  -> top scores (JSON array)   POST /submit {name,score}

export default {
  async fetch(req, env) {
    const url = new URL(req.url);
    const cors = {
      "Access-Control-Allow-Origin": "*",
      "Access-Control-Allow-Methods": "GET,POST,OPTIONS",
      "Access-Control-Allow-Headers": "Content-Type",
    };
    if (req.method === "OPTIONS") return new Response(null, { headers: cors });

    if (url.pathname === "/top") {
      const data = (await env.SCORES.get("board", "json")) || [];
      return Response.json(data.slice(0, 50), { headers: cors });
    }

    if (url.pathname === "/submit" && req.method === "POST") {
      let body;
      try { body = await req.json(); } catch { return new Response("bad request", { status: 400, headers: cors }); }
      const name = String(body.name || "YOU").slice(0, 12).replace(/[<>&]/g, "");
      const score = Math.max(0, Math.min(1e9, Math.floor(Number(body.score) || 0)));
      const data = (await env.SCORES.get("board", "json")) || [];
      data.push({ name, score, t: Date.now() });
      data.sort((a, b) => b.score - a.score);
      await env.SCORES.put("board", JSON.stringify(data.slice(0, 100)));
      return Response.json({ ok: true }, { headers: cors });
    }

    return new Response("Chameleon Survivors leaderboard", { headers: cors });
  },
};
