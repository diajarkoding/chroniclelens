package com.iskan.chroniclelens.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iskan.chroniclelens.di.DefaultDispatcher
import com.iskan.chroniclelens.domain.model.JournalEntry
import com.iskan.chroniclelens.domain.repository.JournalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class JournalViewModel @Inject constructor(
    private val repo: JournalRepository,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher
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
        viewModelScope.launch(dispatcher) { // Use injected dispatcher
            isLoading.value = true
            try {
                repo.addJournal()
                showToast("Jurnal berhasil ditambahkan!")
            } catch (e: Exception) {
                showSnackBar("Gagal menambah jurnal: ${e.message}")
            } finally {
                isLoading.value = false
            }
        }
    }


    fun getEntryById(id: String): JournalEntry? {
        return repo.getEntryById(id)
    }

    fun showToast(message: String) {
        viewModelScope.launch(dispatcher) {
            _eventChannel.send(UiEvent.ShowToast(message))
        }
    }

    fun showSnackBar(message: String) {
        viewModelScope.launch(dispatcher) {
            _eventChannel.send(UiEvent.ShowSnackBar(message))
        }
    }

    fun navigateToDetail(id: String) {
        viewModelScope.launch(dispatcher) {
            _eventChannel.send(UiEvent.NavigateToDetail(id))
        }
    }

    sealed class UiEvent {
        data class ShowToast(val message: String) : UiEvent()
        data class ShowSnackBar(val message: String) : UiEvent()
        data class NavigateToDetail(val entryId: String) : UiEvent()
    }

}
