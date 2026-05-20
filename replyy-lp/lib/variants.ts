export type HeroVariant = {
  id: string;
  eyebrow: string;
  headline: string;
  headlineMuted: string;
  sub: string;
};

export const HERO_VARIANTS: HeroVariant[] = [
  {
    id: "vendor-folder",
    eyebrow: "For the 14 deals you'll close in 2026. Not the 33 you won't.",
    headline: "You sent the Calendly link.",
    headlineMuted: "That deal is already lost.",
    sub: "The moment your prospect saw the link, they moved you to the “vendor” folder. VPs don’t schedule with one-of-forty. The Tuesday morning you wrote by hand — three real times, one line of warmth — that’s how $180K closes. You missed that morning today.",
  },
  {
    id: "564-emails",
    eyebrow: "Built for the 47 scheduling emails a month you’d rather not write.",
    headline: "564 scheduling emails.",
    headlineMuted: "That’s what you’ll write this year. By hand.",
    sub: "Three full workdays gone to six-sentence emails. Three days you’ll never get back. Replyy is how you take them back — starting Tuesday.",
  },
  {
    id: "hartmann",
    eyebrow: "For deals that close on a Tuesday morning. Not in a booking form.",
    headline: "The Hartmann deal arrived at 8:47.",
    headlineMuted: "You’ll reply at 10:08.",
    sub: "An hour and twenty-one minutes is how long you take to schedule. Your competitor takes eighteen. Same calendar. Same skills. Different software. The deal closes with whoever sounds human, fast.",
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
