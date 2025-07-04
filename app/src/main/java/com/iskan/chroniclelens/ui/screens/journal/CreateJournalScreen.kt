package com.iskan.chroniclelens.ui.screens.journal
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.iskan.chroniclelens.ui.components.common.*
import com.iskan.chroniclelens.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateJournalScreen(
    onNavigateBack: () -> Unit,
    onSaveJournal: (title: String, content: String, tags: List<String>) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var tags by remember { mutableStateOf(listOf<String>()) }

    var titleError by remember { mutableStateOf(false) }
    var contentError by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // Validation functions
    fun validateInputs(): Boolean {
        titleError = title.isBlank()
        contentError = content.isBlank()
        return !titleError && !contentError
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create New Memory") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    TextButton(
                        onClick = {
                            if (validateInputs()) {
                                onSaveJournal(title.trim(), content.trim(), tags)
                                showSuccessDialog = true
                            } else {
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar(
                                        message = "Please fill in all required fields",
                                        duration = SnackbarDuration.Short
                                    )
                                }
                            }
                        }
                    ) {
                        Icon(
                            Icons.Default.Save,
                            contentDescription = "Save",
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("SAVE")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.background,
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                        )
                    )
                )
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Title Input
                ChronicleTextField(
                    value = title,
                    onValueChange = {
                        title = it
                        if (titleError) titleError = false
                    },
                    label = "Memory Title *",
                    leadingIcon = Icons.Default.Title,
                    isError = titleError,
                    errorMessage = if (titleError) "Title is required" else null,
                    singleLine = true
                )

                // Content Input with character counter
                CharacterCountTextField(
                    value = content,
                    onValueChange = {
                        content = it
                        if (contentError) contentError = false
                    },
                    label = "What's on your mind? *",
                    maxCharacters = 1000,
                    minLines = 5
                )

                // Tag Input
                TagInputField(
                    tags = tags,
                    onAddTag = { newTag ->
                        if (!tags.contains(newTag)) {
                            tags = tags + newTag
                        }
                    },
                    onRemoveTag = { tagToRemove ->
                        tags = tags.filter { it != tagToRemove }
                    }
                )

                // Media Actions (placeholder for now)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Attach Media",
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            MediaActionButton(
                                icon = Icons.Default.CameraAlt,
                                label = "Photo",
                                onClick = {
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar(
                                            "Camera integration coming in Module 10!"
                                        )
                                    }
                                }
                            )
                            MediaActionButton(
                                icon = Icons.Default.Mic,
                                label = "Audio",
                                onClick = {
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar(
                                            "Audio recording coming in Module 11!"
                                        )
                                    }
                                }
                            )
                            MediaActionButton(
                                icon = Icons.Default.LocationOn,
                                label = "Location",
                                onClick = {
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar(
                                            "Location feature coming in Module 12!"
                                        )
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    // Success Dialog
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { },
            icon = {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Success,
                    modifier = Modifier.size(48.dp)
                )
            },
            title = { Text("Memory Saved!") },
            text = { Text("Your memory has been captured successfully.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showSuccessDialog = false
                        onNavigateBack()
                    }
                ) {
                    Text("OK")
                }
            }
        )
    }
}

@Composable
private fun MediaActionButton(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FilledTonalButton(
            onClick = onClick,
            modifier = Modifier.size(64.dp),
            contentPadding = PaddingValues(0.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.size(28.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall
        )
    }
}