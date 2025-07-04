package com.iskan.chroniclelens.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.iskan.chroniclelens.ui.viewmodel.JournalViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun JournalDetailScreen(
    entryId: String,
    viewModel: JournalViewModel = viewModel()
) {
    val entry = viewModel.getEntryById(entryId)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Detail Jurnal", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Text("ID: $entryId", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Title: ${entry?.title}")
    }
}
