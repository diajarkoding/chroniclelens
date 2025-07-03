package com.iskan.chroniclelens.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.iskan.chroniclelens.ui.screens.JournalDetailScreen
import com.iskan.chroniclelens.ui.screens.JournalListScreen
import com.iskan.chroniclelens.viewmodel.JournalViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val journalViewModel: JournalViewModel = viewModel()

    NavHost(navController = navController, startDestination = "list") {
        composable("list") {
            JournalListScreen(
                navController = navController,
                viewModel = journalViewModel
            )
        }
        composable("detail/{entryId}") {
            val entryId = it.arguments?.getString("entryId")
            JournalDetailScreen(entryId = entryId ?: "Unknown")
        }
    }
}