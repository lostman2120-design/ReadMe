import { ImageResponse } from "next/og";

export const runtime = "edge";
export const alt = "Replyy — Reply with scheduling. Not a Calendly link.";
export const size = { width: 1200, height: 630 };
export const contentType = "image/png";

export default function OpenGraphImage() {
  return new ImageResponse(
    (
      <div
        style={{
          width: "100%",
          height: "100%",
          display: "flex",
          flexDirection: "column",
          justifyContent: "space-between",
          padding: "72px",
          backgroundColor: "#FAFAF7",
          color: "#0B0B0F",
          fontFamily: "Georgia, serif",
        }}
      >
        <div style={{ display: "flex", alignItems: "center", fontSize: 32, fontFamily: "system-ui" }}>
          <span style={{ fontWeight: 600 }}>Replyy</span>
          <span style={{ color: "#D97757" }}>.</span>
        </div>

        <div style={{ display: "flex", flexDirection: "column" }}>
          <div style={{ fontSize: 96, lineHeight: 1.05, letterSpacing: "-0.02em" }}>
            Reply with scheduling.
          </div>
          <div
            style={{
              fontSize: 96,
              lineHeight: 1.05,
              letterSpacing: "-0.02em",
              color: "#6B6B7B",
            }}
          >
            Not a Calendly link.
          </div>
        </div>

        <div
          style={{
            display: "flex",
            justifyContent: "space-between",
            alignItems: "flex-end",
            fontFamily: "system-ui",
            fontSize: 24,
            color: "#6B6B7B",
          }}
        >
          <div style={{ display: "flex" }}>
            AI-drafted scheduling emails for $50k+ deals.
          </div>
          <div style={{ display: "flex" }}>$9/mo founding price</div>
        </div>
      </div>
    ),
    { ...size },
  );
}
