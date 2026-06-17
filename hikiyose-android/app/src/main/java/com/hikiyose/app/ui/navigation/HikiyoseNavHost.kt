package com.hikiyose.app.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.hikiyose.app.ui.screens.affirmation.AffirmationScreen
import com.hikiyose.app.ui.screens.home.HomeScreen
import com.hikiyose.app.ui.screens.journal.JournalScreen
import com.hikiyose.app.ui.screens.template.TemplateScreen

@Composable
fun HikiyoseNavHost() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.hierarchy

    Scaffold(
        bottomBar = {
            NavigationBar {
                TopDestination.entries.forEach { dest ->
                    val selected = currentRoute?.any { it.route == dest.route } == true
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            navController.navigate(dest.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(dest.icon, contentDescription = dest.label) },
                        label = { Text(dest.label) },
                    )
                }
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = TopDestination.Home.route,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(TopDestination.Home.route) { HomeScreen() }
            composable(TopDestination.Journal.route) { JournalScreen() }
            composable(TopDestination.Affirmation.route) { AffirmationScreen() }
            composable(TopDestination.Templates.route) { TemplateScreen() }
        }
    }
}
