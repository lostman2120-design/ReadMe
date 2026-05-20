import type { Metadata } from "next";
import "./globals.css";

const siteUrl =
  process.env.NEXT_PUBLIC_SITE_URL ?? "https://replyy-lp.vercel.app";

export const metadata: Metadata = {
  metadataBase: new URL(siteUrl),
  title: "Replyy — Reply with scheduling. Not a Calendly link.",
  description:
    "AI-drafted scheduling emails for $50k+ deals. Paste the email you got. Get three personal-sounding replies with real times from your calendar.",
  openGraph: {
    title: "Replyy — Reply with scheduling. Not a Calendly link.",
    description:
      "AI-drafted scheduling emails for high-trust deals. Built for executive sales, premium consulting, and exec coaches.",
    type: "website",
  },
  twitter: {
    card: "summary_large_image",
    title: "Replyy — Reply with scheduling. Not a Calendly link.",
    description:
      "AI-drafted scheduling emails for high-trust deals. $9/mo founding price.",
  },
};

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="en">
      <body className="font-sans">{children}</body>
    </html>
  );
}
