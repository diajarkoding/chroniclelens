package com.iskan.chroniclelens.ui.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iskan.chroniclelens.domain.model.JournalEntry
import com.iskan.chroniclelens.domain.repository.JournalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JournalViewModel @Inject constructor(
    private val repo: JournalRepository
) : ViewModel() {

    val notes = repo.getAllEntries()

    private val _count = MutableStateFlow(0)
    val count: StateFlow<Int> get() = _count

    private val _selectedMood = MutableStateFlow("😎")
    val selectedMood: StateFlow<String> get() = _selectedMood

    private val _eventChannel = Channel<UiEvent>()
    val events = _eventChannel.receiveAsFlow()

    val isLoading = MutableStateFlow(false)

    fun incrementCount() {
        _count.value++
    }

    fun setMood(mood: String) {
        _selectedMood.value = mood
    }

    fun addJournal() {
        if (isLoading.value) return
        viewModelScope.launch {
            isLoading.value = true
            delay(1000)
            repo.addJournal()
            isLoading.value = false
        }
    }

    fun getEntryById(id: String): JournalEntry? {
        return repo.getEntryById(id)
    }

    fun showToast(message: String) {
        viewModelScope.launch {
            _eventChannel.send(UiEvent.ShowToast(message))
        }
    }

    fun showSnackBar(message: String) {
        viewModelScope.launch {
            _eventChannel.send(UiEvent.ShowSnackBar(message))
        }
    }

    fun navigateToDetail(id: String) {
        viewModelScope.launch {
            _eventChannel.send(UiEvent.NavigateToDetail(id))
        }
    }

    sealed class UiEvent {
        data class ShowToast(val message: String) : UiEvent()
        data class ShowSnackBar(val message: String) : UiEvent()
        data class NavigateToDetail(val entryId: String) : UiEvent()
    }

}
