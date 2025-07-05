package com.iskan.chroniclelens.ui.components.journal
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.iskan.chroniclelens.ui.theme.cardGradient
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// Data class for Journal - temporary until Room implementation
data class JournalEntry(
    val id: Int,
    val title: String,
    val content: String,
    val createdAt: LocalDateTime,
    val tags: List<String> = emptyList(),
    val hasPhoto: Boolean = false,
    val hasAudio: Boolean = false,
    val hasLocation: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JournalCard(
    journal: JournalEntry,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val dateFormatter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        DateTimeFormatter.ofPattern("MMM dd, yyyy")
    } else {
        TODO("VERSION.SDK_INT < O")
    }
    val timeFormatter = DateTimeFormatter.ofPattern("hh:mm a")

    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 8.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = MaterialTheme.cardGradient
                )
                .padding(16.dp)
        ) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = journal.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.CalendarToday,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = journal.createdAt.format(dateFormatter),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Text(
                            text = "•",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Text(
                            text = journal.createdAt.format(timeFormatter),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Action buttons
                Row {
                    IconButton(
                        onClick = { expanded = !expanded }
                    ) {
                        Icon(
                            imageVector = if (expanded) {
                                Icons.Default.ExpandLess
                            } else {
                                Icons.Default.ExpandMore
                            },
                            contentDescription = "Expand"
                        )
                    }

                    IconButton(onClick = onDeleteClick) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Content preview
            Text(
                text = journal.content,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = if (expanded) Int.MAX_VALUE else 2,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Media indicators
            if (journal.hasPhoto || journal.hasAudio || journal.hasLocation) {
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (journal.hasPhoto) {
                        MediaIndicator(
                            icon = Icons.Default.Photo,
                            label = "Photo"
                        )
                    }
                    if (journal.hasAudio) {
                        MediaIndicator(
                            icon = Icons.Default.Mic,
                            label = "Audio"
                        )
                    }
                    if (journal.hasLocation) {
                        MediaIndicator(
                            icon = Icons.Default.LocationOn,
                            label = "Location"
                        )
                    }
                }
            }

            // Tags
            if (journal.tags.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    journal.tags.forEach { tag ->
                        AssistChip(
                            onClick = { },
                            label = {
                                Text(
                                    text = tag,
                                    style = MaterialTheme.typography.labelSmall
                                )
                            },
                            modifier = Modifier.height(28.dp),
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MediaIndicator(
    icon: ImageVector,
    label: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            modifier = Modifier.size(16.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary
        )
    }
}