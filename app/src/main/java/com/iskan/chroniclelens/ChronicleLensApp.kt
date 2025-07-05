package com.iskan.chroniclelens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.iskan.chroniclelens.ui.navigation.ChronicleLensNavigation
import com.iskan.chroniclelens.ui.theme.ChronicleLensTheme

/**
 * Main App Composable
 *
 * Ini adalah entry point untuk seluruh app
 * Manages:
 * - Theme state
 * - Navigation
 * - Global app state
 */
@Composable
fun ChronicleLensApp() {
    // Theme state di-hoist ke app level
    // Kenapa? Agar bisa diakses dari screen manapun
    var isDarkTheme by remember { mutableStateOf(false) }

    ChronicleLensTheme(darkTheme = isDarkTheme) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ChronicleLensNavigation(
                isDarkTheme = isDarkTheme,
                onThemeToggle = { isDarkTheme = !isDarkTheme }
            )
        }
    }
}