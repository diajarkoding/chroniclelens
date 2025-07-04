package com.iskan.chroniclelens.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

// Shape system dengan modern rounded corners
val Shapes = Shapes(
    // Extra small - Untuk chips, small buttons
    extraSmall = RoundedCornerShape(4.dp),

    // Small - Untuk cards kecil, list items
    small = RoundedCornerShape(8.dp),

    // Medium - Default untuk cards dan containers
    medium = RoundedCornerShape(16.dp),

    // Large - Untuk bottom sheets, dialogs
    large = RoundedCornerShape(24.dp),

    // Extra large - Untuk full screen modals
    extraLarge = RoundedCornerShape(32.dp)
)

// Custom shapes untuk special components
object CustomShapes {
    // Untuk FAB yang lebih rounded
    val fabShape = RoundedCornerShape(16.dp)

    // Untuk image containers
    val imageShape = RoundedCornerShape(12.dp)

    // Untuk top bar dengan rounded bottom
    val topBarShape = RoundedCornerShape(
        topStart = 0.dp,
        topEnd = 0.dp,
        bottomStart = 24.dp,
        bottomEnd = 24.dp
    )

    // Untuk bottom navigation dengan rounded top
    val bottomNavShape = RoundedCornerShape(
        topStart = 24.dp,
        topEnd = 24.dp,
        bottomStart = 0.dp,
        bottomEnd = 0.dp
    )
}