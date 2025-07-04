package com.iskan.chroniclelens.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Extension properties untuk easy access
val MaterialTheme.glassMorphism: Color
    @Composable
    get() = if (isSystemInDarkTheme()) GlassDark else GlassLight

val MaterialTheme.cardGradient: Brush
    @Composable
    get() = if (isSystemInDarkTheme()) {
        Brush.verticalGradient(
            colors = listOf(
                colorScheme.surfaceVariant,
                colorScheme.surface
            )
        )
    } else {
        Brush.verticalGradient(
            colors = listOf(
                colorScheme.surface,
                colorScheme.surfaceVariant
            )
        )
    }

val MaterialTheme.primaryGradient: Brush
    @Composable
    get() = Brush.horizontalGradient(
        colors = listOf(
            colorScheme.primary,
            colorScheme.tertiary
        )
    )

val MaterialTheme.secondaryGradient: Brush
    @Composable
    get() = Brush.verticalGradient(
        colors = listOf(
            colorScheme.secondary,
            colorScheme.secondaryContainer
        )
    )

// Shadow colors untuk elevation effect
val MaterialTheme.shadowColor: Color
    @Composable
    get() = if (isSystemInDarkTheme()) {
        Color.Black.copy(alpha = 0.8f)
    } else {
        Color.Black.copy(alpha = 0.15f)
    }

// Spacing constants untuk consistent padding
object Spacing {
    const val xs = 4
    const val sm = 8
    const val md = 16
    const val lg = 24
    const val xl = 32
    const val xxl = 48
}

// Animation durations untuk consistent motion
object AnimationDuration {
    const val Fast = 150
    const val Normal = 300
    const val Slow = 500
    const val VerySlow = 1000
}