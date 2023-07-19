package com.eva.reminders.presentation.feature_create.composables

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.Update
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun CreateTaskBottomBar(
    modifier: Modifier = Modifier,
    onMoreOptions: () -> Unit,
    isCreate: Boolean = false,
    onActionClick: () -> Unit,
    onColor: () -> Unit,
    editedAt: LocalDateTime = LocalDateTime.now(),
    iconColor: Color = MaterialTheme.colorScheme.secondary,
    floatingActionBarColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    floatingActionBarContentColor: Color = MaterialTheme.colorScheme.onSecondaryContainer,
    timeColor: Color = MaterialTheme.colorScheme.secondary
) {
    val currentTime = remember(editedAt) {
        derivedStateOf {
            val time = editedAt
                .format(DateTimeFormatter.ofPattern("hh:mm a"))
            "Edited: $time"
        }
    }
    BottomAppBar(
        modifier = modifier
            .fillMaxWidth(),
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onActionClick,
                elevation = FloatingActionButtonDefaults.elevation(),
                containerColor = floatingActionBarColor,
                contentColor = floatingActionBarContentColor
            ) {
                when {
                    isCreate -> {
                        Icon(
                            imageVector = Icons.Outlined.Check,
                            contentDescription = "Add this task"
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Save")
                    }

                    else -> {
                        Icon(
                            imageVector = Icons.Outlined.Update,
                            contentDescription = "Update this task"
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Update")
                    }
                }
            }
        },
        windowInsets = WindowInsets.navigationBars,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        actions = {
            IconButton(
                onClick = onColor,
                colors = IconButtonDefaults
                    .iconButtonColors(contentColor = iconColor)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Palette,
                    contentDescription = "Color Palette",
                )
            }
            IconButton(
                onClick = onMoreOptions,
                colors = IconButtonDefaults
                    .iconButtonColors(contentColor = iconColor)
            ) {
                Icon(
                    imageVector = Icons.Outlined.MoreVert,
                    contentDescription = "Vertical option"
                )
            }
            Text(
                text = currentTime.value,
                color = timeColor,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Medium
            )
        }
    )
}


@Preview
@Composable
fun CreateTaskBottomBarPreview() {
    CreateTaskBottomBar(
        onMoreOptions = {  },
        onColor = { },
        onActionClick = {}
    )
}