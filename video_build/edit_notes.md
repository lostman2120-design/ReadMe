# Edit Notes — Alex ICP demo (v0.2.3)

## Source footage inspected
| Label | File | Spec | Content |
|-------|------|------|---------|
| A | `PR動画4.mp4` (PR__43) | 1716×1836, 21.9s | **Options / ICP Settings page** — Settings, "Your product or service", "Target customer profile", ICP Settings (industries, roles, company size, region, pain points, excluded, outreach tone, DM tone, lifecycle stage), Save Settings, License. |
| B | `PR動画4-2.mp4` (PR__411) | 1920×1080, 57.8s | LinkedIn profile (Alex Enache) + extension sidebar. Header "Analysis complete". Sidebar: ICP settings, **Profile context (High)**, **ICP Fit Score 90 / Confidence High**, CRM sync status, Persona, Pain points, Icebreaker, Outreach strategy, **Suggested DM (real draft + Personalization + Spam risk: low)**, HubSpot action buttons. |
| C | `PR動画4-3.mp4` (PR__421) | 1920×1080, 53.0s | Same profile + sidebar, but with the **"Scoring against ICP"** section expanded — Roles, Industries, Company size, Offer, Pain points, Tone in one view. |

## Clips selected and why
- **Title (A11/C 11s)** — full profile+sidebar frame as a calm backdrop for the version badge.
- **Define your ICP (A, ~1s, pan down)** — the Options page is the clearest proof the user *inputs* their ICP. Pans across product/service → target customer profile → ICP Settings ("These help the AI score profiles against your best-fit customers") → industries/roles. **Answers Alex Q2 (can I input my ICP).**
- **Sidebar "Scoring against ICP" (C, 33s, vertical pan)** — shows the saved ICP (roles, industries, company size, offer, pain points, tone) living *inside the sidebar*, i.e. what each profile is scored against. **Answers Alex Q3 (does it relate the profile to that ICP).**
- **Analyze (B, 13s, slow zoom to sidebar)** — establishes "this runs on a visible LinkedIn profile", header reads "Analysis complete".
- **ICP Fit Score (B, 15s, pan from Profile context → score)** — Profile context: High, then ICP Fit Score 90, Confidence: High. **Answers Alex Q1 (how the score relates to context).**
- **Reasoning (B, 43s, pan persona→pain points→icebreaker→outreach strategy)** — the "why" behind the score/angle. **Supports Alex Q1 (understand why).**
- **Personalized DM draft (B, 53s, gentle pan)** — the real generated "Suggested DM" with Personalization score and Spam risk: low. **Answers Alex Q4 (DM improves with context)** — and honestly framed as *manual review*.
- **HubSpot (B, 29s, caption on top)** — Add to HubSpot / Create HubSpot note / Create follow-up task buttons.
- **Closing** — generated card with the end-to-end flow + honest pricing/limits line.

## Readability / motion
- Source sidebar is a narrow ~430 px panel inside 1920 px. Each detail beat crops to that panel (16:9 slice) and upscales ~4.5×, with slow vertical pans (eased) so every section is legible. Options page is near-full-width with a gentle downward pan. Full-frame beats use a subtle Ken-Burns zoom. 0.4s cross-dissolves between beats. Stills (not live scroll) were used for the zoom beats to keep motion smooth and jitter-free.

## What was deliberately NOT shown (honesty / quality)
- **"DM variants could not be generated. Please try Analyze Profile again."** — this empty/error state appears in footage B. All DM crops sit *below* it (Suggested DM only), so the error never appears. The separate "3 variants" feature is **not** claimed anywhere.
- **Blank/white transition frames** in C (~19–26s and ~47–52s) — avoided; only static, populated windows were sampled.
- **"Edit ICP" button** — never focused on or clicked (it can open a blocked page); the ICP *summary* is shown instead.
- **Backend API URL** (render.com) on the Options page — Gaussian-blurred in the still as a precaution and kept out of frame by the crop.
- License key is already masked (dots) in the source.
- No fake UI, no auto-send/auto-message claims, no "scrapes LinkedIn" implication.

## Score wording note
The score card in this footage reads "90 / Not enough data / Confidence: High" because a
**default ICP** was in use (the sidebar literally notes "Using default ICP. Customize it for
better scoring"). The edit keeps the honest "Confidence: High / Profile context: High"
framing and does not over-claim the number. If you re-record after saving a customized ICP,
the score card and DM-variants block should populate fully and could replace these shots.

## Outputs
- `alex_icp_demo_v023.mp4` — 66.8s (target 60–75) ✅
- `alex_icp_demo_v023_short.mp4` — 32.0s (target 30–40) ✅
- 1920×1080, H.264 + AAC, faststart. Calm instrumental bed at ~-20 dB, fades out at the end.
