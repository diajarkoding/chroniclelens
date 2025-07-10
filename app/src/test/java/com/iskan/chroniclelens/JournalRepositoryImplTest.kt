package com.iskan.chroniclelens

import com.iskan.chroniclelens.data.repository.JournalRepositoryImpl
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class JournalRepositoryImplTest {

    private lateinit var repository: JournalRepositoryImpl

    @Before
    fun setup() {
        repository = JournalRepositoryImpl()
    }

    @Test
    fun addJournal_should_increase_list_size() = runTest {
        val before = repository.getAllEntries().value.size
        repository.addJournal()
        val after = repository.getAllEntries().value.size
        TestCase.assertEquals(before + 1, after)
    }

    @Test
    fun getEntryById_should_return_correct_journal() = runTest {
        val entry = repository.getEntryById("001")
        TestCase.assertNotNull(entry)
        TestCase.assertEquals("001", entry?.id)
    }
}