import { NextResponse } from "next/server";

const EMAIL_RE = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

export async function POST(request: Request) {
  let body: unknown;
  try {
    body = await request.json();
  } catch {
    return NextResponse.json({ error: "Invalid JSON" }, { status: 400 });
  }

  const email = (body as { email?: unknown })?.email;
  if (typeof email !== "string" || !EMAIL_RE.test(email)) {
    return NextResponse.json({ error: "Please enter a valid email." }, { status: 400 });
  }

  const webhook = process.env.WAITLIST_WEBHOOK_URL;
  if (webhook) {
    try {
      await fetch(webhook, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          email,
          source: "replyy-lp",
          at: new Date().toISOString(),
        }),
      });
    } catch (err) {
      console.error("waitlist webhook failed", err);
    }
  } else {
    console.log("[waitlist signup]", email, new Date().toISOString());
  }

  return NextResponse.json({ ok: true });
}
