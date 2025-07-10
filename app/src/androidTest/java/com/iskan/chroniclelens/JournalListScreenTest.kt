package com.iskan.chroniclelens

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.iskan.chroniclelens.ui.screens.JournalListScreen
import com.iskan.chroniclelens.ui.viewmodel.JournalViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * UI Test untuk JournalListScreen menggunakan Compose Testing
 *
 * @OptIn(ExperimentalCoroutinesApi::class) diperlukan untuk test coroutines
 * @RunWith(AndroidJUnit4::class) menjalankan test di Android environment
 *
 * Test ini memverifikasi:
 * - Button add journal berfungsi dengan benar
 * - State ter-update setelah action
 * - UI merespons perubahan state
 */
@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class JournalListScreenTest {

    /**
     * Compose Test Rule untuk setup dan interact dengan Compose UI
     *
     * @get:Rule menandakan ini akan di-setup sebelum setiap test
     * createComposeRule() membuat environment untuk test Compose
     */
    @get:Rule
    val composeTestRule = createComposeRule()

    /**
     * Test untuk memverifikasi button "Tambah Jurnal" berfungsi
     *
     * Test scenario:
     * 1. Setup fake repository dengan 3 entries
     * 2. Render screen dengan viewModel yang menggunakan fake repo
     * 3. Click button tambah journal
     * 4. Verifikasi entries bertambah menjadi 4
     *
     * runTest membuat coroutine test environment
     */
    @Test
    fun addJournalButton_addsNewEntry() = runTest {
        // 🔧 Setup fake repository untuk testing
        // Menghindari dependency ke database/API real
        val fakeRepo = FakeJournalRepository()

        // 🧪 UnconfinedTestDispatcher untuk immediate execution
        // Penting untuk testing agar coroutines tidak delayed
        val testDispatcher = UnconfinedTestDispatcher()

        // Create ViewModel dengan test dependencies
        val viewModel = JournalViewModel(fakeRepo, testDispatcher)

        // Setup Compose content untuk testing
        composeTestRule.setContent {
            JournalListScreen(
                navController = rememberNavController(), // Mock navigation
                viewModel = viewModel
            )
        }

        // Ambil initial size untuk verifikasi
        val initialSize = viewModel.notes.value.size

        // 🎯 Find dan click button dengan test tag
        composeTestRule.onNodeWithTag("add_journal_button").performClick()

        // ⏳ Advance coroutines sampai idle
        // Memastikan semua coroutines selesai dijalankan
        advanceUntilIdle()

        // ✅ Verifikasi journal entry bertambah
        val finalSize = viewModel.notes.value.size
        assert(finalSize == initialSize + 1) {
            "Expected ${initialSize + 1} entries, but got $finalSize"
        }
    }

    /**
     * Test alternatif dengan assertion yang lebih explicit
     */
    @Test
    fun addJournalButton_addsNewEntry_withAssertion() = runTest {
        val fakeRepo = FakeJournalRepository()
        val testDispatcher = UnconfinedTestDispatcher()
        val viewModel = JournalViewModel(fakeRepo, testDispatcher)

        composeTestRule.setContent {
            JournalListScreen(
                navController = rememberNavController(),
                viewModel = viewModel
            )
        }

        // Verifikasi initial state
        assert(viewModel.notes.value.size == 3) {
            "Initial entries should be 3"
        }

        // Perform action
        composeTestRule.onNodeWithTag("add_journal_button").performClick()

        // Wait for state update
        advanceUntilIdle()

        // Verifikasi final state
        assert(viewModel.notes.value.size == 4) {
            "Should have 4 entries after adding"
        }

        // Verifikasi entry baru ada
        val newEntry = viewModel.notes.value.last()
        assert(newEntry.title == "Entry Baru") {
            "New entry should have title 'Entry Baru'"
        }
    }
}