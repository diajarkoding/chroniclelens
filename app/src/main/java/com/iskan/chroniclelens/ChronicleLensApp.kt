package com.iskan.chroniclelens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.iskan.chroniclelens.ui.screens.home.HomeScreen
import com.iskan.chroniclelens.ui.theme.ChronicleLensTheme

@Composable
fun ChronicleLensApp() {
    // State untuk dark theme toggle
    var isDarkTheme by remember { mutableStateOf(false) }

    ChronicleLensTheme(darkTheme = isDarkTheme) {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            HomeScreen(
                isDarkTheme = isDarkTheme,
                onThemeToggle = { isDarkTheme = !isDarkTheme }
            )
        }
    }
}

