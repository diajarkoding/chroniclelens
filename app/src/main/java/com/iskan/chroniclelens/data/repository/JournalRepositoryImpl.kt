package com.iskan.chroniclelens.data.repository

import com.iskan.chroniclelens.domain.model.JournalEntry
import com.iskan.chroniclelens.domain.repository.JournalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.collections.plus

class JournalRepositoryImpl : JournalRepository {

    private val journalList = MutableStateFlow(
        listOf(
            JournalEntry("001", "Hari yang cerah"),
            JournalEntry("002", "Hari yang penuh tantangan"),
            JournalEntry("003", "Hari yang penuh kebahagiaan"),
        )
    )

    override fun getAllEntries(): StateFlow<List<JournalEntry>> = journalList.asStateFlow()

    override fun getEntryById(id: String): JournalEntry? {
        return journalList.value.find { it.id == id }
    }

    override suspend fun addJournal() {
        val newId = "%03d".format(journalList.value.size + 1)
        journalList.value = journalList.value + JournalEntry(newId, "Jurnal baru $newId")
    }
}
