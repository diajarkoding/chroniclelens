package com.iskan.chroniclelens.ui.screens

import android.util.Log
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.iskan.chroniclelens.ui.components.CounterCard
import com.iskan.chroniclelens.ui.components.MoodSelectorCard
import com.iskan.chroniclelens.ui.viewmodel.JournalViewModel

@Composable
fun JournalListScreen(
    navController: NavController,
    viewModel: JournalViewModel = viewModel(),
) {
    val count by viewModel.count.collectAsState()
    val selectedMood by viewModel.selectedMood.collectAsState()
    val notes = viewModel.notes.collectAsState()

    val isLoading = viewModel.isLoading.collectAsState()

    var selectedNoteId by remember { mutableStateOf<String?>(null) }


    Crossfade(targetState = selectedNoteId) { noteId ->
        if (noteId == null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text("Daftar Jurnal")

                CounterCard(
                    count = count,
                    onIncrement = viewModel::incrementCount
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text("Pilih Mood Hari Ini:")

                MoodSelectorCard(
                    selectedMood = selectedMood,
                    onMoodChange = viewModel::setMood
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = viewModel::addJournal,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Text("➕ Tambah Jurnal Baru")
                }

                LazyColumn {
                    items(notes.value) { entry ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedNoteId = entry.id }
                                .padding(8.dp)
                        ) {
                            Text("#${entry.id} - ${entry.title}", modifier = Modifier.padding(16.dp))
                        }
                    }
                }

                if (isLoading.value){
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        } else {
            LaunchedEffect(noteId) {
                navController.navigate("detail/$noteId")
                selectedNoteId = null
            }
        }
    }
}