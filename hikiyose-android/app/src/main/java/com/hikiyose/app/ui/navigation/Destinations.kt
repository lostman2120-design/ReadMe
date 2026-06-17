package com.hikiyose.app.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Style
import androidx.compose.ui.graphics.vector.ImageVector

/** Bottom-navigation destinations (wireframe ①: tab bar). */
enum class TopDestination(
    val route: String,
    val label: String,
    val icon: ImageVector,
) {
    Home("home", "ホーム", Icons.Filled.Dashboard),
    Journal("journal", "ジャーナル", Icons.Filled.Book),
    Affirmation("affirmation", "アファメーション", Icons.Filled.AutoAwesome),
    Templates("templates", "書式", Icons.Filled.Style);
}
