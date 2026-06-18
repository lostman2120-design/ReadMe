package com.hikiyose.app.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.History
import androidx.compose.ui.graphics.vector.ImageVector

/** Bottom-navigation destinations (wireframes ①〜⑤). */
enum class TopDestination(
    val route: String,
    val label: String,
    val icon: ImageVector,
) {
    Home("home", "ホーム", Icons.Filled.AutoAwesome),
    Entry("entry", "記入", Icons.Filled.EditNote),
    Journal("journal", "日記", Icons.Filled.CalendarMonth),
    Achievement("achievement", "達成", Icons.Filled.EmojiEvents),
    Records("records", "記録", Icons.Filled.History);
}
