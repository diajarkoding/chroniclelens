package com.iskan.chroniclelens.ui.screens.journal

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.iskan.chroniclelens.ui.components.journal.*
import com.iskan.chroniclelens.ui.components.common.*
import com.iskan.chroniclelens.ui.theme.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun JournalListScreen(
    journals: List<JournalEntry>,
    onJournalClick: (JournalEntry) -> Unit,
    onDeleteJournal: (JournalEntry) -> Unit,
    onCreateNewClick: () -> Unit,
    onNavigateBack: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    var journalToDelete by remember { mutableStateOf<JournalEntry?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var sortOrder by remember { mutableStateOf(SortOrder.NEWEST_FIRST) }
    var showSortMenu by remember { mutableStateOf(false) }

    // Filter journals based on search
    val filteredJournals = remember(journals, searchQuery, sortOrder) {
        journals
            .filter { journal ->
                searchQuery.isBlank() ||
                        journal.title.contains(searchQuery, ignoreCase = true) ||
                        journal.content.contains(searchQuery, ignoreCase = true) ||
                        journal.tags.any { it.contains(searchQuery, ignoreCase = true) }
            }
            .sortedWith(
                when (sortOrder) {
                    SortOrder.NEWEST_FIRST -> compareByDescending { it.createdAt }
                    SortOrder.OLDEST_FIRST -> compareBy { it.createdAt }
                    SortOrder.ALPHABETICAL -> compareBy { it.title.lowercase() }
                }
            )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Your Memories",
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showSortMenu = true }) {
                        Icon(Icons.Default.Sort, contentDescription = "Sort")
                    }

                    DropdownMenu(
                        expanded = showSortMenu,
                        onDismissRequest = { showSortMenu = false }
                    ) {
                        SortOrder.values().forEach { order ->
                            DropdownMenuItem(
                                text = {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(order.displayName)
                                        if (sortOrder == order) {
                                            Icon(
                                                Icons.Default.Check,
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.primary,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                    }
                                },
                                onClick = {
                                    sortOrder = order
                                    showSortMenu = false
                                }
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        floatingActionButton = {
            AnimatedFab(
                onClick = onCreateNewClick,
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Create new journal",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.background,
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.1f)
                        )
                    )
                )
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Search Bar
                SearchBar(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )

                // Content
                AnimatedContent(
                    targetState = filteredJournals.isEmpty(),
                    transitionSpec = {
                        fadeIn() togetherWith fadeOut()
                    },
                    label = "contentTransition"
                ) { isEmpty ->
                    if (isEmpty) {
                        EmptyState(
                            isSearching = searchQuery.isNotBlank(),
                            onCreateClick = onCreateNewClick
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(
                                horizontal = 16.dp,
                                vertical = 8.dp
                            ),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(
                                items = filteredJournals,
                                key = { it.id }
                            ) { journal ->
                                JournalCard(
                                    journal = journal,
                                    onClick = { onJournalClick(journal) },
                                    onDeleteClick = {
                                        journalToDelete = journal
                                        showDeleteDialog = true
                                    },
                                    modifier = Modifier.animateItem(fadeInSpec = null, fadeOutSpec = null, placementSpec = spring(
                                        dampingRatio = Spring.DampingRatioMediumBouncy,
                                        stiffness = Spring.StiffnessLow
                                    )
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Delete Confirmation Dialog
    if (showDeleteDialog && journalToDelete != null) {
        AlertDialog(
            onDismissRequest = {
                showDeleteDialog = false
                journalToDelete = null
            },
            icon = {
                Icon(
                    Icons.Default.Warning,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(48.dp)
                )
            },
            title = { Text("Delete Memory?") },
            text = {
                Text(
                    "Are you sure you want to delete \"${journalToDelete?.title}\"? This action cannot be undone."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        journalToDelete?.let { onDeleteJournal(it) }
                        showDeleteDialog = false
                        journalToDelete = null
                    }
                ) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        journalToDelete = null
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    DockedSearchBar(
        query = query,
        onQueryChange = onQueryChange,
        onSearch = { },
        active = false,
        onActiveChange = { },
        placeholder = { Text("Search memories...") },
        leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = null)
        },
        trailingIcon = {
            AnimatedVisibility(
                visible = query.isNotEmpty(),
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut()
            ) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(Icons.Default.Clear, contentDescription = "Clear")
                }
            }
        },
        modifier = modifier
    ) { }
}

@Composable
private fun EmptyState(
    isSearching: Boolean,
    onCreateClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = if (isSearching) Icons.Default.SearchOff else Icons.Default.AutoAwesome,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (isSearching) {
                "No memories found"
            } else {
                "Start Your Journey"
            },
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = if (isSearching) {
                "Try searching with different keywords"
            } else {
                "Capture your first memory and begin your chronicle"
            },
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        if (!isSearching) {
            Spacer(modifier = Modifier.height(24.dp))

            GradientButton(
                onClick = onCreateClick,
                text = "Create First Memory"
            )
        }
    }
}

enum class SortOrder(val displayName: String) {
    NEWEST_FIRST("Newest First"),
    OLDEST_FIRST("Oldest First"),
    ALPHABETICAL("A to Z")
}