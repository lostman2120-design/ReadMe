# Replyy LP

Next.js 15 (App Router) + Tailwind landing page for Replyy.

## Local dev

```bash
cd replyy-lp
npm install
npm run dev
```

Open http://localhost:3000.

## Deploy to Vercel

1. Push this repo to GitHub (already done if you're reading this in the repo)
2. Go to https://vercel.com/new and import the repo
3. **Set the Root Directory to `replyy-lp`** — this is the only manual step
4. Framework preset auto-detects as Next.js. Leave build/install commands as default.
5. Add env vars (optional):
   - `WAITLIST_WEBHOOK_URL` — see `.env.example`
6. Deploy

Once deployed, point your domain (e.g. `replyy.com`) at the Vercel project.

### One-click deploy button

[![Deploy with Vercel](https://vercel.com/button)](https://vercel.com/new/clone?repository-url=https%3A%2F%2Fgithub.com%2Flostman2120-design%2Freadme&root-directory=replyy-lp&project-name=replyy-lp&repository-name=replyy-lp)

## Waitlist

Form posts to `/api/waitlist`. By default it logs signups to the server console.

To wire it to a real destination, set `WAITLIST_WEBHOOK_URL`. The route auto-detects:

- **Slack incoming webhooks** (`hooks.slack.com/...`) — formats as a Slack message
- **Anything else** — POSTs `{ email, variant, source, at }` as JSON

See `.env.example` for the exact format.

Drop-in options:
- **Slack incoming webhook** — simplest, get a notification per signup
- **Formspree / Zapier catch hook** — store to a Google Sheet
- **Vercel KV + Resend** — full-featured, but more setup

## A/B testing the headline

Three hero variants ship by default (see `lib/variants.ts`). A user is randomly
assigned one variant on first visit via `middleware.ts`, persisted in the
`replyy_variant` cookie for 90 days. The chosen variant ID is included in every
waitlist signup so you can compute conversion per variant.

To add or change variants, edit `lib/variants.ts`. Each variant has a unique
`id` (string) — pick stable IDs so historical data stays meaningful.

## OG / social card

A dynamic OpenGraph image is generated at build/request time via
`app/opengraph-image.tsx` using `next/og`. Edit that file to change the social
preview.
