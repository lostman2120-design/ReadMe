# LinkedIn → HubSpot AI Assistant — Demo Video

60s product demo (final render: `out/demo_final.mp4`, 1920×1080, 58s after transitions).

## Structure
- **0–5s Hook** – "Copy. Paste. Repeat." chaotic manual copy-paste between LinkedIn & HubSpot, ending on an animated red ✖ + "Still copy-pasting to HubSpot by hand?"
- **5–15s Step 1 · Analyze** – one click "Analyze Profile" → AI Lead Score
- **15–25s Step 2 · Insights** – pain points, buying signals, ready-to-send DM
- **25–40s Step 3 · Add to HubSpot** – one-click sync, no retyping
- **40–55s Step 4 · Done** – real HubSpot contact + AI summary note + follow-up task
- **55–58s CTA** – "Built for LinkedIn + HubSpot users"

All on-screen captions are English (Inter, white w/ black outline, bottom).

## Pipeline
| script | output |
|---|---|
| `build_intro.py` | 0–5s hook (`out/intro.mp4`) |
| `build_demo.py`  | 4 act clips from the screen recording, with spotlights/subtitles/labels |
| `cta.html` + Chromium screenshot | end card (`assets/cta_card.png`) |
| `audio_gen.py`   | synthesized soundtrack + SFX (`out/soundtrack.wav`) |
| final `ffmpeg` xfade + mux | `out/demo_final.mp4` |

## Source assets (not committed — user uploads)
- `PR__21.mp4` – main 55s screen recording (full workflow)
- `PR__1.mp4`  – high-res HubSpot add-contact clip
- `demo_storyboard.html` – storyboard / CTA reference

Fonts: static Inter weights instantiated from `fonts/Inter-Variable.ttf`.
