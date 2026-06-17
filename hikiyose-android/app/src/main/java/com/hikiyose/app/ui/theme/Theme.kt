package com.hikiyose.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = Purple,
    onPrimary = SurfaceLight,
    primaryContainer = PurpleLight,
    onPrimaryContainer = Ink,
    secondary = Gold,
    background = Lavender,
    onBackground = Ink,
    surface = SurfaceLight,
    onSurface = Ink,
    surfaceVariant = PurpleLight,
    onSurfaceVariant = Mist,
)

private val DarkColors = darkColorScheme(
    primary = PurpleDark,
    onPrimary = Ink,
    primaryContainer = Purple,
    onPrimaryContainer = SurfaceLight,
    secondary = Gold,
    background = SurfaceDark,
    onBackground = SurfaceLight,
    surface = SurfaceDark,
    onSurface = SurfaceLight,
)

@Composable
fun HikiyoseTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors = if (darkTheme) DarkColors else LightColors
    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content,
    )
}
