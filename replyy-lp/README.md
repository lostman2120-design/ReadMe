# Replyy LP

Next.js 15 (App Router) + Tailwind landing page for Replyy.

## Local dev

```bash
cd replyy-lp
npm install
npm run dev
```

Open http://localhost:3000.

## Waitlist

Form posts to `/api/waitlist`. By default it logs signups to the server console.

To wire it to a real destination, set:

```
WAITLIST_WEBHOOK_URL=https://your-endpoint.example.com
```

Each signup is POSTed as:

```json
{ "email": "...", "source": "replyy-lp", "at": "2026-05-18T..." }
```

Drop-in options: a Slack/Discord webhook, Zapier catch hook, Formspree, or a small Vercel KV / Resend audience handler.

## Deploy

Connect this directory as the project root in Vercel.
