package com.iskan.chroniclelens.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class JournalViewModel : ViewModel() {

    // Mutable state
    private val _count = mutableStateOf(0)
    val count: State<Int> get() = _count

    private val _selectedMood = mutableStateOf("😊")
    val selectedMood: State<String> get() = _selectedMood

    private val _notes = mutableStateListOf("001", "002", "003")
    val notes: List<String> get() = _notes

    // Actions
    fun incrementCount() {
        _count.value++
    }

    fun setMood(mood: String) {
        _selectedMood.value = mood
    }

    fun addJournal() {
        val newId = "%03d".format(_notes.size + 1)
        _notes.add(newId)
    }

    fun getEntryById(id: String): String? {
        return _notes.find { it == id }
    }
}
