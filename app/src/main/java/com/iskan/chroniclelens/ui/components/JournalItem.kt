package com.iskan.chroniclelens.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.iskan.chroniclelens.domain.model.JournalEntry

@Composable
fun JournalItem(
    entry: JournalEntry,
    onClick: (JournalEntry) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(entry) }
            .padding(8.dp)
    ) {
        Text("#${entry.id} - ${entry.title}", modifier = Modifier.padding(16.dp))
    }
}