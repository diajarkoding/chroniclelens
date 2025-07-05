package com.iskan.chroniclelens.ui.screens

import android.util.Log
import android.widget.Toast
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.iskan.chroniclelens.ui.components.CounterCard
import com.iskan.chroniclelens.ui.components.JournalItem
import com.iskan.chroniclelens.ui.components.MoodSelectorCard
import com.iskan.chroniclelens.ui.viewmodel.JournalViewModel

@Composable
fun JournalListScreen(
    navController: NavController,
    viewModel: JournalViewModel = viewModel(),
) {
    val count by viewModel.count.collectAsState()
    val selectedMood by viewModel.selectedMood.collectAsState()
    val notes by viewModel.notes.collectAsState()

    val isLoading = viewModel.isLoading.collectAsState()

    val context = LocalContext.current

    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is JournalViewModel.UiEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                    Log.d("JournalListScreen", "ShowToast: ${event.message}")
                }
                is JournalViewModel.UiEvent.ShowSnackBar -> {
                    snackBarHostState.showSnackbar(event.message)
                    Log.d("JournalListScreen", "ShowSnackBar: ${event.message}")
                }
                is JournalViewModel.UiEvent.NavigateToDetail -> {
                    navController.navigate("detail/${event.entryId}")
                }
            }
        }
    }


    Scaffold(
        snackbarHost = {
            SnackbarHost(snackBarHostState)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
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
                onClick = { viewModel.showSnackBar("Ini adalah snackbar dari ViewModel!") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Text("Tampilkan Snackbar")
            }

            Button(
                onClick = viewModel::addJournal,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .testTag("add_journal_button")
            ) {
                Text("➕ Tambah Jurnal Baru")
            }

            LazyColumn {
                items( items = notes, key = { it.id } )
                { entry ->
                    JournalItem(entry)
                    {
                        viewModel.navigateToDetail(entry.id)
                        Log.d("JournalListScreen", "JournalItem clicked: ${entry.id}")
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
        }
    }
