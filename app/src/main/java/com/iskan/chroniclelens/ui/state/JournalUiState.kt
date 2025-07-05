package com.iskan.chroniclelens.ui.state

import com.iskan.chroniclelens.ui.components.journal.JournalEntry

/**
 * UI State untuk Journal screens
 *
 * Kenapa pakai data class?
 * - Immutable by default (val properties)
 * - Automatic equals(), hashCode(), copy()
 * - Perfect untuk Compose yang butuh compare state changes
 */
data class JournalListUiState(
    val journals: List<JournalEntry> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val searchQuery: String = "",
    val selectedJournals: Set<Int> = emptySet(), // For multi-select
    val isSelectionMode: Boolean = false
)

/**
 * Separate state untuk Create/Edit screen
 *
 * Kenapa pisah?
 * - Single Responsibility Principle
 * - Easier testing
 * - Avoid unnecessary recomposition di screen lain
 */
data class JournalEditUiState(
    val journalId: Int? = null, // null = create new, not null = edit existing
    val title: String = "",
    val content: String = "",
    val tags: List<String> = emptyList(),
    val hasUnsavedChanges: Boolean = false,
    val isSaving: Boolean = false,
    val validationErrors: ValidationErrors = ValidationErrors()
)

/**
 * Validation errors as separate data class
 *
 * Kenapa?
 * - Grouped related data
 * - Easy to reset all errors at once
 * - Type-safe error handling
 */
data class ValidationErrors(
    val titleError: String? = null,
    val contentError: String? = null,
    val tagError: String? = null
)

/**
 * Events yang bisa terjadi di Journal screens
 *
 * Kenapa pakai sealed class?
 * - Exhaustive when statements
 * - Type-safe event handling
 * - Clear action definitions
 */
sealed class JournalEvent {
    // List screen events
    data class SearchQueryChanged(val query: String) : JournalEvent()
    data class JournalClicked(val journal: JournalEntry) : JournalEvent()
    data class JournalLongPressed(val journalId: Int) : JournalEvent()
    data class DeleteJournals(val journalIds: Set<Int>) : JournalEvent()
    object ClearSelection : JournalEvent()
    object RefreshJournals : JournalEvent()

    // Edit screen events
    data class TitleChanged(val title: String) : JournalEvent()
    data class ContentChanged(val content: String) : JournalEvent()
    data class TagAdded(val tag: String) : JournalEvent()
    data class TagRemoved(val tag: String) : JournalEvent()
    object SaveJournal : JournalEvent()
    object DiscardChanges : JournalEvent()
}

/**
 * Side effects yang perlu di-handle di UI
 *
 * Kenapa pisah dari state?
 * - One-time events (show snackbar, navigate)
 * - Shouldn't survive configuration changes
 * - Avoid showing same message twice
 */
sealed class JournalUiEffect {
    data class ShowSnackbar(val message: String) : JournalUiEffect()
    data class NavigateToDetail(val journalId: Int) : JournalUiEffect()
    object NavigateBack : JournalUiEffect()
    data class ShowDeleteConfirmation(val journalIds: Set<Int>) : JournalUiEffect()
}