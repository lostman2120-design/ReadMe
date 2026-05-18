export type HeroVariant = {
  id: string;
  eyebrow: string;
  headline: string;
  headlineMuted: string;
  sub: string;
};

export const HERO_VARIANTS: HeroVariant[] = [
  {
    id: "calendly-not",
    eyebrow: "Launching June 2026 · 47 founding spots left",
    headline: "Reply with scheduling.",
    headlineMuted: "Not a Calendly link.",
    sub: "Paste the email you got. Replyy drafts three personal-sounding replies — each with real times pulled from your calendar — so you can close $50k+ deals without sounding like a SaaS form.",
  },
  {
    id: "for-people-who",
    eyebrow: "Launching June 2026 · 47 founding spots left",
    headline: "The scheduling tool for people",
    headlineMuted: "who don’t send Calendly links.",
    sub: "When a link feels too transactional, paste the email instead. Get three personal replies in your voice, with real availability — ready to send.",
  },
  {
    id: "fifty-k-deals",
    eyebrow: "For deals over $25k · Launching June 2026",
    headline: "AI-drafted scheduling emails",
    headlineMuted: "for $50k+ deals.",
    sub: "Stop hand-writing the same scheduling email forty-seven times a month. Replyy drafts three options in your voice, with real times from your calendar, in under five seconds.",
  },
];

export const COOKIE_NAME = "replyy_variant";

export function pickVariant(cookieValue: string | undefined): HeroVariant {
  if (cookieValue) {
    const found = HERO_VARIANTS.find((v) => v.id === cookieValue);
    if (found) return found;
  }
  return HERO_VARIANTS[0];
}
