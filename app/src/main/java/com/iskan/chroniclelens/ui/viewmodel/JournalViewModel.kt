package com.iskan.chroniclelens.ui.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class JournalEntry(val id: String, val title: String)

class JournalViewModel : ViewModel() {

    // MutableStateFlow
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()


    private val _count = MutableStateFlow(0)
    val count: StateFlow<Int> = _count.asStateFlow()

    private val _selectedMood = MutableStateFlow("😊")
    val selectedMood: StateFlow<String> = _selectedMood.asStateFlow()

    private val _notes = MutableStateFlow(
        listOf(
            JournalEntry("001", "Hari yang cerah"),
            JournalEntry("002", "Hari yang penuh tantangan"),
            JournalEntry("003", "Hari yang penuh kebahagiaan"),
        )
    )
    val notes: StateFlow<List<JournalEntry>> = _notes.asStateFlow()


    // Actions
    fun incrementCount() {
        _count.value++
    }

    fun setMood(mood: String) {
        _selectedMood.value = mood
    }

    fun addJournal() {
        viewModelScope.launch {
            _isLoading.value = true
            delay(1500)
            val newId = "%03d".format((_notes.value.size) + 1)
            val newEntry = JournalEntry(newId, "Jurnal Baru $newId")
            _notes.value += newEntry
            _isLoading.value = false
        }
    }


    fun getEntryById(id: String): JournalEntry? {
        return _notes.value.find { it.id == id }
    }
}
