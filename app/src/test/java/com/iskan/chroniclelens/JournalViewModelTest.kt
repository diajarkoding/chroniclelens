package com.iskan.chroniclelens

import com.iskan.chroniclelens.domain.model.JournalEntry
import com.iskan.chroniclelens.domain.repository.JournalRepository
import com.iskan.chroniclelens.ui.viewmodel.JournalViewModel
import junit.framework.TestCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class JournalViewModelTest {

    private lateinit var repository: JournalRepository
    private lateinit var viewModel: JournalViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        repository = mock()
        whenever(repository.getAllEntries()).thenReturn(
            MutableStateFlow(listOf(JournalEntry("001", "Mocked")))
        )
        viewModel = JournalViewModel(repository, testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun incrementCount_increases_count_by_1() = runTest {
        viewModel.incrementCount()
        TestCase.assertEquals(1, viewModel.count.value)
    }

    @Test
    fun setMood_updates_mood_state() = runTest {
        viewModel.setMood("😊")
        TestCase.assertEquals("😊", viewModel.selectedMood.value)
    }

    @Test
    fun addJournal_triggers_repository_add() = runTest {
        val fakeRepo = mock<JournalRepository>()
        val vm = JournalViewModel(fakeRepo, testDispatcher)
        vm.addJournal()
        testDispatcher.scheduler.advanceUntilIdle()
        verify(fakeRepo).addJournal()
    }
}
