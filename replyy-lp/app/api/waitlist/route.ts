import { NextResponse } from "next/server";

const EMAIL_RE = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
const SLACK_HOST = "hooks.slack.com";

export async function POST(request: Request) {
  let body: unknown;
  try {
    body = await request.json();
  } catch {
    return NextResponse.json({ error: "Invalid JSON" }, { status: 400 });
  }

  const payload = body as { email?: unknown; variant?: unknown };
  const email = payload.email;
  if (typeof email !== "string" || !EMAIL_RE.test(email)) {
    return NextResponse.json({ error: "Please enter a valid email." }, { status: 400 });
  }
  const variant = typeof payload.variant === "string" ? payload.variant : "unknown";

  const signup = {
    email,
    variant,
    source: "replyy-lp",
    at: new Date().toISOString(),
  };

  const webhook = process.env.WAITLIST_WEBHOOK_URL;
  if (webhook) {
    await forwardToWebhook(webhook, signup);
  } else {
    console.log("[waitlist signup]", signup);
  }

  return NextResponse.json({ ok: true });
}

type Signup = {
  email: string;
  variant: string;
  source: string;
  at: string;
};

async function forwardToWebhook(url: string, signup: Signup) {
  const isSlack = (() => {
    try {
      return new URL(url).host.endsWith(SLACK_HOST);
    } catch {
      return false;
    }
  })();

  const body = isSlack
    ? JSON.stringify({
        text: `:tada: *New Replyy waitlist signup*\n\`${signup.email}\` — variant \`${signup.variant}\` — ${signup.at}`,
      })
    : JSON.stringify(signup);

  try {
    const res = await fetch(url, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body,
    });
    if (!res.ok) {
      console.error("waitlist webhook returned", res.status, await res.text().catch(() => ""));
    }
  } catch (err) {
    console.error("waitlist webhook threw", err);
  }
}
