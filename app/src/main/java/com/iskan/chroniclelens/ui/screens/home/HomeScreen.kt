package com.iskan.chroniclelens.ui.screens.home

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.iskan.chroniclelens.ui.theme.AnimationDuration
import com.iskan.chroniclelens.ui.theme.CustomShapes
import com.iskan.chroniclelens.ui.theme.Spacing
import com.iskan.chroniclelens.ui.theme.cardGradient
import com.iskan.chroniclelens.ui.theme.glassMorphism
import com.iskan.chroniclelens.ui.theme.primaryGradient
import com.iskan.chroniclelens.ui.theme.shadowColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    isDarkTheme: Boolean,
    onThemeToggle: () -> Unit,
    onNavigateToJournals: () -> Unit = {}
) {
    // States
    var journalCount by remember { mutableStateOf(0) }
    var showMotivation by remember { mutableStateOf(false) }
    var currentMotivation by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    // Animation states
    val fabScale = remember { Animatable(1f) }
    val cardElevation by animateFloatAsState(
        targetValue = if (journalCount > 0) 8f else 4f,
        animationSpec = tween(AnimationDuration.Normal),
        label = "cardElevation"
    )

    // Motivational messages
    val motivationalMessages = listOf(
        "Every memory is a treasure! 💎",
        "You're creating your legacy! 🌟",
        "Capture this moment forever! 📸",
        "Your story matters! ✨",
        "Keep those memories alive! 🎯"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            "ChronicleLens",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        // Beta badge
                        Surface(
                            color = MaterialTheme.colorScheme.secondary,
                            shape = MaterialTheme.shapes.small
                        ) {
                            Text(
                                "BETA",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSecondary
                            )
                        }
                    }
                },
                actions = {
                    // Theme toggle button dengan animasi
                    IconButton(
                        onClick = onThemeToggle,
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(MaterialTheme.glassMorphism)
                    ) {
                        AnimatedContent(
                            targetState = isDarkTheme,
                            transitionSpec = {
                                fadeIn() + scaleIn() togetherWith fadeOut() + scaleOut()
                            },
                            label = "themeIcon"
                        ) { darkTheme ->
                            Icon(
                                imageVector = if (darkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                                contentDescription = "Toggle theme",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                ),
                modifier = Modifier.shadow(
                    elevation = 4.dp,
                    spotColor = MaterialTheme.shadowColor
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    coroutineScope.launch {
                        // Animate FAB
                        fabScale.animateTo(0.8f, tween(100))
                        fabScale.animateTo(1.2f, tween(100))
                        fabScale.animateTo(1f, tween(100))

                        // Update state
                        journalCount++
                        currentMotivation = motivationalMessages.random()
                        showMotivation = true

                        // Navigate to create journal
                        onNavigateToJournals()

                        // Hide motivation after 3 seconds
                        delay(3000)
                        showMotivation = false
                    }
                },
                containerColor = MaterialTheme.colorScheme.primary,
                shape = CustomShapes.fabShape,
                modifier = Modifier
                    .scale(fabScale.value)
                    .shadow(
                        elevation = 8.dp,
                        shape = CustomShapes.fabShape,
                        spotColor = MaterialTheme.shadowColor
                    )
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Add Journal",
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.background,
                            MaterialTheme.colorScheme.background.copy(alpha = 0.95f),
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                        )
                    )
                )
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(Spacing.lg.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Welcome text dengan gradient
                Box(
                    modifier = Modifier
                        .background(
                            brush = MaterialTheme.primaryGradient,
                            shape = MaterialTheme.shapes.medium
                        )
                        .padding(1.dp)
                ) {
                    Surface(
                        color = MaterialTheme.colorScheme.background,
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text(
                            text = "Welcome to Your\nMemory Vault! 🎯",
                            modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
                            style = MaterialTheme.typography.headlineMedium,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Memory counter card dengan glassmorphism effect
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(
                            elevation = cardElevation.dp,
                            shape = MaterialTheme.shapes.large,
                            spotColor = MaterialTheme.shadowColor
                        ),
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                brush = MaterialTheme.cardGradient
                            )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Total Memories Captured",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Animated counter
                            AnimatedContent(
                                targetState = journalCount,
                                transitionSpec = {
                                    slideInVertically { height -> -height } + fadeIn() togetherWith
                                            slideOutVertically { height -> height } + fadeOut()
                                },
                                label = "counterAnimation"
                            ) { count ->
                                Text(
                                    text = "$count",
                                    style = MaterialTheme.typography.displayLarge,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Black
                                )
                            }

                            if (journalCount > 0) {
                                Spacer(modifier = Modifier.height(8.dp))

                                // Progress indicator
                                LinearProgressIndicator(
                                    progress = { (journalCount % 10) / 10f },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(8.dp)
                                        .clip(MaterialTheme.shapes.small),
                                    color = MaterialTheme.colorScheme.primary,
                                    trackColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = "${10 - (journalCount % 10)} more to next milestone!",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                // Button to view journals
                                FilledTonalButton(
                                    onClick = onNavigateToJournals,
                                    modifier = Modifier.fillMaxWidth(0.8f)
                                ) {
                                    Icon(
                                        Icons.Default.AutoAwesome,
                                        contentDescription = null,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("View All Memories")
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Instruction text
                AnimatedVisibility(
                    visible = journalCount == 0,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Surface(
                        color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text(
                            text = "Tap the + button to start capturing memories!",
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // Motivational message overlay
            AnimatedVisibility(
                visible = showMotivation,
                enter = fadeIn() + slideInVertically(initialOffsetY = { -40 }),
                exit = fadeOut() + slideOutVertically(targetOffsetY = { -40 }),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 100.dp)
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = MaterialTheme.shapes.large,
                    shadowElevation = 8.dp
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = currentMotivation,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}