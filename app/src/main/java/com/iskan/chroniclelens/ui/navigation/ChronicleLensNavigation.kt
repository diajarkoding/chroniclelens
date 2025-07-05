package com.iskan.chroniclelens.ui.navigation

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.iskan.chroniclelens.ui.components.journal.JournalEntry
import com.iskan.chroniclelens.ui.screens.home.HomeScreen
import com.iskan.chroniclelens.ui.screens.journal.*
import com.iskan.chroniclelens.ui.state.JournalUiEffect
import kotlinx.coroutines.flow.collectLatest

/**
 * Sealed class untuk type-safe navigation
 *
 * Kenapa sealed class?
 * - Compile-time safety
 * - No typos in route strings
 * - Easy refactoring
 */
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object JournalList : Screen("journal_list")
    object CreateJournal : Screen("create_journal")
    object JournalDetail : Screen("journal_detail/{journalId}") {
        fun createRoute(journalId: Int) = "journal_detail/$journalId"
    }
}

/**
 * Main Navigation Component
 */
@SuppressLint("UnrememberedGetBackStackEntry")
@Composable
fun ChronicleLensNavigation(
    navController: NavHostController = rememberNavController(),
    isDarkTheme: Boolean,
    onThemeToggle: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        // Home Screen
        composable(Screen.Home.route) {
            HomeScreen(
                isDarkTheme = isDarkTheme,
                onThemeToggle = onThemeToggle,
                onNavigateToJournals = {
                    navController.navigate(Screen.JournalList.route)
                }
            )
        }

        // Journal List Screen
        composable(Screen.JournalList.route) {
            // Create ViewModel instance
            val viewModel: JournalListViewModel = viewModel()
            val uiState by viewModel.uiState.collectAsState()

            // Handle side effects
            LaunchedEffect(viewModel) {
                viewModel.uiEffect.collectLatest { effect ->
                    when (effect) {
                        is JournalUiEffect.NavigateToDetail -> {
                            navController.navigate(
                                Screen.JournalDetail.createRoute(effect.journalId)
                            )
                        }
                        is JournalUiEffect.ShowSnackbar -> {
                            // Handle in screen
                        }
                        else -> {}
                    }
                }
            }

            JournalListScreen(
                journals = uiState.journals,
                onJournalClick = { journal ->
                    navController.navigate(
                        Screen.JournalDetail.createRoute(journal.id)
                    )
                },
                onDeleteJournal = { journal ->
                    viewModel.deleteConfirmed(setOf(journal.id))
                },
                onCreateNewClick = {
                    navController.navigate(Screen.CreateJournal.route)
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // Create Journal Screen
        composable(Screen.CreateJournal.route) {
            val listViewModel: JournalListViewModel = viewModel(
                viewModelStoreOwner = navController.getBackStackEntry(Screen.JournalList.route)
            )

            CreateJournalScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onSaveJournal = { title, content, tags ->
                    listViewModel.createJournal(title, content, tags)
                    navController.popBackStack()
                }
            )
        }

        // Journal Detail Screen (placeholder for now)
        composable(Screen.JournalDetail.route) { backStackEntry ->
            val journalId = backStackEntry.arguments?.getString("journalId")?.toIntOrNull()

            // Temporary detail screen
            JournalDetailScreen(
                journalId = journalId ?: 0,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}