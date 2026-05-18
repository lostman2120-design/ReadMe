import { NextResponse, type NextRequest } from "next/server";
import { HERO_VARIANTS, COOKIE_NAME } from "@/lib/variants";

const NINETY_DAYS = 60 * 60 * 24 * 90;

export function middleware(request: NextRequest) {
  const response = NextResponse.next();

  const existing = request.cookies.get(COOKIE_NAME)?.value;
  const isValid = existing && HERO_VARIANTS.some((v) => v.id === existing);

  if (!isValid) {
    const pick = HERO_VARIANTS[Math.floor(Math.random() * HERO_VARIANTS.length)];
    response.cookies.set(COOKIE_NAME, pick.id, {
      maxAge: NINETY_DAYS,
      path: "/",
      sameSite: "lax",
    });
  }

  return response;
}

export const config = {
  matcher: ["/((?!api|_next|favicon|.*\\..*).*)"],
};
