package com.hikiyose.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.hikiyose.app.ui.navigation.HikiyoseNavHost
import com.hikiyose.app.ui.theme.HikiyoseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            HikiyoseTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    HikiyoseNavHost()
                }
            }
        }
    }
}
