package com.hikiyose.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Start from the Material 3 defaults, then apply the rounded font to every style
// so all components (buttons, labels, etc.) share the friendly look.
private val Default = Typography()

private fun TextStyle.rounded() = copy(fontFamily = ZenMaruGothic)

val Typography = Typography(
    displayLarge = Default.displayLarge.rounded(),
    displayMedium = Default.displayMedium.rounded(),
    displaySmall = Default.displaySmall.rounded(),
    headlineLarge = Default.headlineLarge.rounded(),
    headlineMedium = TextStyle(
        fontFamily = ZenMaruGothic,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 34.sp,
    ),
    headlineSmall = Default.headlineSmall.rounded(),
    titleLarge = TextStyle(
        fontFamily = ZenMaruGothic,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        lineHeight = 28.sp,
    ),
    titleMedium = TextStyle(
        fontFamily = ZenMaruGothic,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
    ),
    titleSmall = Default.titleSmall.rounded(),
    bodyLarge = TextStyle(
        fontFamily = ZenMaruGothic,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 26.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = ZenMaruGothic,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 23.sp,
    ),
    bodySmall = Default.bodySmall.rounded(),
    labelLarge = TextStyle(
        fontFamily = ZenMaruGothic,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
    ),
    labelMedium = Default.labelMedium.rounded(),
    labelSmall = Default.labelSmall.rounded(),
)
