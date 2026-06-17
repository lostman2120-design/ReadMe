package com.hikiyose.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.hikiyose.app.ui.navigation.HikiyoseNavHost
import com.hikiyose.app.ui.theme.HikiyoseTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Seed a few starter affirmations on first launch so the home screen isn't empty.
        val app = application as HikiyoseApplication
        lifecycleScope.launch { app.repository.seedDefaultAffirmationsIfEmpty() }

        setContent {
            HikiyoseTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    HikiyoseNavHost()
                }
            }
        }
    }
}
