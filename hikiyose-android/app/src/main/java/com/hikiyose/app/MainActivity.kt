package com.hikiyose.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.hikiyose.app.ui.navigation.HikiyoseNavHost
import com.hikiyose.app.ui.screens.splash.SplashScreen
import com.hikiyose.app.ui.theme.HikiyoseTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            HikiyoseTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    var showSplash by remember { mutableStateOf(true) }
                    LaunchedEffect(Unit) {
                        delay(SPLASH_DURATION_MS)
                        showSplash = false
                    }
                    Crossfade(targetState = showSplash, animationSpec = tween(500), label = "splash") { splash ->
                        if (splash) SplashScreen() else HikiyoseNavHost()
                    }
                }
            }
        }
    }

    companion object {
        private const val SPLASH_DURATION_MS = 2000L
    }
}
