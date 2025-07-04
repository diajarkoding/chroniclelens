package com.iskan.chroniclelens.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat



// Dark Color Scheme - Smooth & Modern
private val DarkColorScheme = darkColorScheme(
    primary = LightIndigo,
    onPrimary = Color.White,
    primaryContainer = DeepIndigo,
    onPrimaryContainer = Color(0xFFE8DDFF),

    secondary = Coral,
    onSecondary = Color.White,
    secondaryContainer = DarkCoral,
    onSecondaryContainer = Color(0xFFFFDAD6),

    tertiary = Mint,
    onTertiary = Color.White,
    tertiaryContainer = DarkMint,
    onTertiaryContainer = Color(0xFFB4F5F0),

    error = Error,
    onError = Color.White,
    errorContainer = Color(0xFFB71C1C),
    onErrorContainer = Color(0xFFFFDAD6),

    background = DarkBackground,
    onBackground = Color(0xFFE6E1E5),

    surface = DarkSurface,
    onSurface = Color(0xFFE6E1E5),
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = Color(0xFFCAC4D0),

    outline = Color(0xFF938F99),
    outlineVariant = Color(0xFF49454F),

    scrim = Color.Black,
    inverseSurface = Color(0xFFE6E1E5),
    inverseOnSurface = DarkSurface,
    inversePrimary = Indigo
)

// Light Color Scheme - Clean & Fresh
private val LightColorScheme = lightColorScheme(
    primary = Indigo,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE8DDFF),
    onPrimaryContainer = DeepIndigo,

    secondary = Coral,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFDAD6),
    onSecondaryContainer = Color(0xFF410002),

    tertiary = Mint,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFB4F5F0),
    onTertiaryContainer = Color(0xFF002020),

    error = Error,
    onError = Color.White,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),

    background = LightBackground,
    onBackground = Color(0xFF1C1B1F),

    surface = LightSurface,
    onSurface = Color(0xFF1C1B1F),
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = Color(0xFF49454F),

    outline = Color(0xFF79747E),
    outlineVariant = Color(0xFFCAC4D0),

    scrim = Color.Black,
    inverseSurface = Color(0xFF313033),
    inverseOnSurface = Color(0xFFF4EFF4),
    inversePrimary = LightIndigo
)

@Composable
fun ChronicleLensTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color dimatikan by default untuk consistent branding
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}