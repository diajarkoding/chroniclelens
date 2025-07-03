package com.iskan.chroniclelens.ui.components

// In a suitable file, e.g., MoodSelectorCard.kt

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun MoodSelectorCard(
    selectedMood: String,
    onMoodChange: (String) -> Unit
) {
    val moods = listOf("😊", "😐", "😞")
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(moods) { mood ->
            Card(
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (mood == selectedMood) Color(0xFFE3F2FD) else Color.Transparent
                ),
                border = if (mood == selectedMood) BorderStroke(2.dp, Color.LightGray) else null,
                modifier = Modifier
                    .clickable { onMoodChange(mood) }
                    .padding(4.dp)
            ) {
                Text(
                    text = mood,
                    modifier = Modifier.padding(12.dp),
                    color = if (mood == selectedMood) Color.Blue else Color.Unspecified
                )
            }
        }
    }
}