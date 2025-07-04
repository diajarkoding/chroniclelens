package com.iskan.chroniclelens

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.iskan.chroniclelens.ui.screens.JournalListScreen
import com.iskan.chroniclelens.ui.viewmodel.JournalViewModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class JournalListScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun addJournalButton_addsNewEntry() {
        val viewModel = JournalViewModel()

        composeTestRule.setContent {
            JournalListScreen(
                navController = rememberNavController(),
                viewModel = viewModel
            )
        }

        composeTestRule.onNodeWithTag("add_journal_button").performClick()

        // Wait until loading is false (journal added)
        composeTestRule.waitUntil(timeoutMillis = 3000) {
            !viewModel.isLoading.value
        }

        assert(viewModel.notes.value.size == 4)
    }
}
