package com.hikiyose.app.ui.theme

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.hikiyose.app.R

/**
 * Zen Maru Gothic — a soft, rounded Japanese typeface ("丸ゴシック").
 * Chosen for a friendly, feminine feel that helps make journaling a habit.
 * Bundled in res/font so it works fully offline.
 */
val ZenMaruGothic = FontFamily(
    Font(R.font.zen_maru_gothic_regular, FontWeight.Normal),
    Font(R.font.zen_maru_gothic_medium, FontWeight.Medium),
    Font(R.font.zen_maru_gothic_bold, FontWeight.Bold),
)
