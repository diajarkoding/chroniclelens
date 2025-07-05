package com.iskan.chroniclelens.ui.screens.journal

import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iskan.chroniclelens.ui.components.journal.JournalEntry
import com.iskan.chroniclelens.ui.state.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime

/**
 * ViewModel untuk Journal List Screen
 *
 * Kenapa ViewModel?
 * - Survives configuration changes
 * - Coroutine scope management
 * - Business logic separation
 * - Testable
 */
class JournalListViewModel : ViewModel() {

    /**
     * StateFlow vs MutableStateFlow
     *
     * MutableStateFlow (private):
     * - Internal state management
     * - Can emit new values
     *
     * StateFlow (public):
     * - Read-only for UI
     * - Prevents UI from modifying state directly
     * - Enforces unidirectional data flow
     */
    private val _uiState = MutableStateFlow(JournalListUiState())
    val uiState: StateFlow<JournalListUiState> = _uiState.asStateFlow()

    /**
     * SharedFlow untuk one-time events
     *
     * Kenapa SharedFlow untuk effects?
     * - Doesn't have initial value
     * - Can emit to multiple collectors
     * - Perfect for navigation, snackbars
     */
    private val _uiEffect = MutableSharedFlow<JournalUiEffect>()
    val uiEffect: SharedFlow<JournalUiEffect> = _uiEffect.asSharedFlow()

    // Temporary in-memory storage (akan diganti Room di Module 8)
    private val journalsList = mutableListOf<JournalEntry>()
    private var nextId = 1

    init {
        // Load initial data
        loadJournals()
    }

    /**
     * Handle semua events dari UI
     *
     * Kenapa single function?
     * - Consistent event handling
     * - Easy to trace actions
     * - Type-safe with sealed class
     */
    fun onEvent(event: JournalEvent) {
        when (event) {
            is JournalEvent.SearchQueryChanged -> updateSearchQuery(event.query)
            is JournalEvent.JournalClicked -> openJournal(event.journal)
            is JournalEvent.JournalLongPressed -> toggleSelection(event.journalId)
            is JournalEvent.DeleteJournals -> confirmDelete(event.journalIds)
            JournalEvent.ClearSelection -> clearSelection()
            JournalEvent.RefreshJournals -> loadJournals()
            else -> {} // Handle other events in JournalEditViewModel
        }
    }

    private fun updateSearchQuery(query: String) {
        /**
         * Update state immutably dengan copy()
         *
         * Kenapa copy()?
         * - Data class immutability
         * - Triggers recomposition properly
         * - Thread-safe
         */
        _uiState.update { currentState ->
            currentState.copy(searchQuery = query)
        }
    }

    private fun toggleSelection(journalId: Int) {
        _uiState.update { currentState ->
            val newSelection = if (journalId in currentState.selectedJournals) {
                currentState.selectedJournals - journalId
            } else {
                currentState.selectedJournals + journalId
            }

            currentState.copy(
                selectedJournals = newSelection,
                isSelectionMode = newSelection.isNotEmpty()
            )
        }
    }

    private fun clearSelection() {
        _uiState.update { currentState ->
            currentState.copy(
                selectedJournals = emptySet(),
                isSelectionMode = false
            )
        }
    }

    private fun confirmDelete(journalIds: Set<Int>) {
        viewModelScope.launch {
            _uiEffect.emit(JournalUiEffect.ShowDeleteConfirmation(journalIds))
        }
    }

    fun deleteConfirmed(journalIds: Set<Int>) {
        viewModelScope.launch {
            try {
                // Remove from list
                journalsList.removeAll { it.id in journalIds }

                // Update UI state
                _uiState.update { currentState ->
                    currentState.copy(
                        journals = journalsList.toList(),
                        selectedJournals = emptySet(),
                        isSelectionMode = false
                    )
                }

                // Show success message
                _uiEffect.emit(
                    JournalUiEffect.ShowSnackbar(
                        "${journalIds.size} journal(s) deleted"
                    )
                )
            } catch (e: Exception) {
                _uiEffect.emit(
                    JournalUiEffect.ShowSnackbar(
                        "Failed to delete journals"
                    )
                )
            }
        }
    }

    private fun openJournal(journal: JournalEntry) {
        viewModelScope.launch {
            _uiEffect.emit(JournalUiEffect.NavigateToDetail(journal.id))
        }
    }

    private fun loadJournals() {
        /**
         * viewModelScope untuk coroutines
         *
         * Kenapa viewModelScope?
         * - Automatically cancelled when ViewModel cleared
         * - Prevents memory leaks
         * - Main-safe by default
         */
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                // Simulate loading delay
                kotlinx.coroutines.delay(1000)

                // Add sample data if empty
                if (journalsList.isEmpty()) {
                    addSampleJournals()
                }

                _uiState.update { currentState ->
                    currentState.copy(
                        journals = journalsList.toList(),
                        isLoading = false,
                        errorMessage = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        errorMessage = "Failed to load journals: ${e.message}"
                    )
                }
            }
        }
    }

    fun createJournal(title: String, content: String, tags: List<String>) {
        val newJournal = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            JournalEntry(
                id = nextId++,
                title = title,
                content = content,
                createdAt = LocalDateTime.now(),
                tags = tags
            )
        } else {
            TODO("VERSION.SDK_INT < O")
        }

        journalsList.add(0, newJournal) // Add to beginning

        // Update state
        _uiState.update { currentState ->
            currentState.copy(journals = journalsList.toList())
        }
    }

    private fun addSampleJournals() {
        val sampleJournals = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            listOf(
                JournalEntry(
                    id = nextId++,
                    title = "My First Memory",
                    content = "Today I started using ChronicleLens to capture my memories...",
                    createdAt = LocalDateTime.now().minusDays(3),
                    tags = listOf("first", "milestone"),
                    hasPhoto = true
                ),
                JournalEntry(
                    id = nextId++,
                    title = "Weekend Adventure",
                    content = "Went hiking with friends and discovered an amazing viewpoint...",
                    createdAt = LocalDateTime.now().minusDays(1),
                    tags = listOf("travel", "friends"),
                    hasLocation = true
                ),
                JournalEntry(
                    id = nextId++,
                    title = "Cooking Experiment",
                    content = "Tried making grandma's secret recipe. It turned out better than expected!",
                    createdAt = LocalDateTime.now().minusHours(5),
                    tags = listOf("food", "family"),
                    hasPhoto = true,
                    hasAudio = true
                )
            )
        } else {
            TODO("VERSION.SDK_INT < O")
        }

        journalsList.addAll(sampleJournals)
    }
}

/**
 * ViewModel untuk Create/Edit Journal
 *
 * Separate ViewModel karena:
 * - Different lifecycle
 * - Different state requirements
 * - Cleaner separation of concerns
 */
class JournalEditViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(JournalEditUiState())
    val uiState: StateFlow<JournalEditUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<JournalUiEffect>()
    val uiEffect: SharedFlow<JournalUiEffect> = _uiEffect.asSharedFlow()

    /**
     * Track unsaved changes
     *
     * Kenapa track changes?
     * - Warn user before navigation
     * - Enable/disable save button
     * - Auto-save drafts
     */
    private var originalState = JournalEditUiState()

    fun initializeForEdit(journalId: Int) {
        viewModelScope.launch {
            // TODO: Load journal from repository
            val journal = loadJournalById(journalId)

            val editState = JournalEditUiState(
                journalId = journal.id,
                title = journal.title,
                content = journal.content,
                tags = journal.tags
            )

            originalState = editState
            _uiState.value = editState
        }
    }

    fun onEvent(event: JournalEvent) {
        when (event) {
            is JournalEvent.TitleChanged -> updateTitle(event.title)
            is JournalEvent.ContentChanged -> updateContent(event.content)
            is JournalEvent.TagAdded -> addTag(event.tag)
            is JournalEvent.TagRemoved -> removeTag(event.tag)
            JournalEvent.SaveJournal -> saveJournal()
            JournalEvent.DiscardChanges -> discardChanges()
            else -> {} // Handle list events in JournalListViewModel
        }
    }

    private fun updateTitle(title: String) {
        _uiState.update { currentState ->
            currentState.copy(
                title = title,
                hasUnsavedChanges = checkForChanges(currentState.copy(title = title)),
                validationErrors = currentState.validationErrors.copy(
                    titleError = validateTitle(title)
                )
            )
        }
    }

    private fun updateContent(content: String) {
        _uiState.update { currentState ->
            currentState.copy(
                content = content,
                hasUnsavedChanges = checkForChanges(currentState.copy(content = content)),
                validationErrors = currentState.validationErrors.copy(
                    contentError = validateContent(content)
                )
            )
        }
    }

    private fun addTag(tag: String) {
        if (tag.isBlank()) return

        _uiState.update { currentState ->
            if (currentState.tags.size >= 5) {
                currentState.copy(
                    validationErrors = currentState.validationErrors.copy(
                        tagError = "Maximum 5 tags allowed"
                    )
                )
            } else if (tag in currentState.tags) {
                currentState.copy(
                    validationErrors = currentState.validationErrors.copy(
                        tagError = "Tag already exists"
                    )
                )
            } else {
                currentState.copy(
                    tags = currentState.tags + tag,
                    hasUnsavedChanges = true,
                    validationErrors = currentState.validationErrors.copy(
                        tagError = null
                    )
                )
            }
        }
    }

    private fun removeTag(tag: String) {
        _uiState.update { currentState ->
            currentState.copy(
                tags = currentState.tags - tag,
                hasUnsavedChanges = checkForChanges(
                    currentState.copy(tags = currentState.tags - tag)
                )
            )
        }
    }

    private fun saveJournal() {
        val currentState = _uiState.value

        // Validate all fields
        val titleError = validateTitle(currentState.title)
        val contentError = validateContent(currentState.content)

        if (titleError != null || contentError != null) {
            _uiState.update {
                it.copy(
                    validationErrors = ValidationErrors(
                        titleError = titleError,
                        contentError = contentError
                    )
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }

            try {
                // Simulate save operation
                kotlinx.coroutines.delay(1000)

                // TODO: Save to repository

                _uiEffect.emit(JournalUiEffect.ShowSnackbar("Journal saved successfully"))
                _uiEffect.emit(JournalUiEffect.NavigateBack)
            } catch (e: Exception) {
                _uiState.update { it.copy(isSaving = false) }
                _uiEffect.emit(JournalUiEffect.ShowSnackbar("Failed to save journal"))
            }
        }
    }

    private fun discardChanges() {
        if (_uiState.value.hasUnsavedChanges) {
            viewModelScope.launch {
                // You might want to show a confirmation dialog here
                _uiEffect.emit(JournalUiEffect.NavigateBack)
            }
        } else {
            viewModelScope.launch {
                _uiEffect.emit(JournalUiEffect.NavigateBack)
            }
        }
    }

    private fun checkForChanges(newState: JournalEditUiState): Boolean {
        return newState.title != originalState.title ||
                newState.content != originalState.content ||
                newState.tags != originalState.tags
    }

    private fun validateTitle(title: String): String? {
        return when {
            title.isBlank() -> "Title cannot be empty"
            title.length < 3 -> "Title must be at least 3 characters"
            title.length > 100 -> "Title must be less than 100 characters"
            else -> null
        }
    }

    private fun validateContent(content: String): String? {
        return when {
            content.isBlank() -> "Content cannot be empty"
            content.length < 10 -> "Content must be at least 10 characters"
            content.length > 5000 -> "Content must be less than 5000 characters"
            else -> null
        }
    }

    // Temporary function - will be replaced with repository
    private suspend fun loadJournalById(id: Int): JournalEntry {
        // Simulate loading
        kotlinx.coroutines.delay(500)

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            JournalEntry(
                id = id,
                title = "Sample Journal",
                content = "This is a sample journal content",
                createdAt = LocalDateTime.now(),
                tags = listOf("sample")
            )
        } else {
            TODO("VERSION.SDK_INT < O")
        }
    }
}