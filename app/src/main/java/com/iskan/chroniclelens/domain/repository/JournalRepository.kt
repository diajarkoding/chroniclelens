package com.iskan.chroniclelens.domain.repository

import com.iskan.chroniclelens.domain.model.JournalEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface JournalRepository {
    fun getAllEntries(): StateFlow<List<JournalEntry>>
    fun getEntryById(id: String): JournalEntry?
   suspend fun addJournal()
}
