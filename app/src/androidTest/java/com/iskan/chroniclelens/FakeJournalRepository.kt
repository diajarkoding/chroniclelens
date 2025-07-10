package com.iskan.chroniclelens

import com.iskan.chroniclelens.domain.model.JournalEntry
import com.iskan.chroniclelens.domain.repository.JournalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Fake implementation dari JournalRepository untuk testing
 *
 * Fake vs Mock:
 * - Fake: Implementasi sederhana yang working untuk testing
 * - Mock: Object yang behavior-nya di-define saat test
 *
 * Fake repository ini:
 * - Menyimpan data di memory (MutableStateFlow)
 * - Tidak butuh database atau network
 * - Behavior predictable untuk testing
 */
class FakeJournalRepository : JournalRepository {

    /**
     * Internal mutable state untuk menyimpan journal entries
     *
     * MutableStateFlow dipilih karena:
     * - Support reactive updates
     * - Easy to test state changes
     * - Mirip dengan real implementation
     */
    private val _journalEntries = MutableStateFlow(
        listOf(
            JournalEntry("1", "Entry 1"),
            JournalEntry("2", "Entry 2"),
            JournalEntry("3", "Entry 3")
        )
    )

    /**
     * Expose sebagai StateFlow (read-only)
     * Sesuai dengan interface contract
     */
    override fun getAllEntries(): StateFlow<List<JournalEntry>> = _journalEntries

    /**
     * Get single entry by ID
     *
     * @param id ID dari journal entry
     * @return JournalEntry jika found, null jika tidak
     */
    override fun getEntryById(id: String): JournalEntry? {
        return _journalEntries.value.find { it.id == id }
    }

    /**
     * Add new journal entry
     *
     * Suspend function untuk simulate async operation
     * Menggunakan timestamp sebagai ID untuk uniqueness
     *
     * Update StateFlow akan trigger UI update di observers
     */
    override suspend fun addJournal() {
        val newEntry = JournalEntry(
            id = System.currentTimeMillis().toString(),
            title = "Entry Baru"
        )
        // Update state dengan menambah entry baru
        _journalEntries.value = _journalEntries.value + newEntry
    }
}