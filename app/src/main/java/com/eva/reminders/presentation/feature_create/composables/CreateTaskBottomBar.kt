package com.eva.reminders.presentation.feature_create.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun CreateTaskBottomBar(
    modifier: Modifier = Modifier,
    onMoreOptions: () -> Unit,
    floatingActionButton: @Composable () -> Unit,
    onColor: () -> Unit
) {
    val currentTime = remember {
        derivedStateOf {
            val time = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("hh:mm a"))
            "Edited: $time"
        }
    }
    BottomAppBar(
        modifier = modifier
            .fillMaxWidth(),
        floatingActionButton = floatingActionButton,
        actions = {
            IconButton(onClick = onColor) {
                Icon(
                    imageVector = Icons.Outlined.Palette,
                    contentDescription = "Color Palette",
                )
            }
            IconButton(onClick = onMoreOptions) {
                Icon(
                    imageVector = Icons.Outlined.MoreVert,
                    contentDescription = "Vertical option"
                )
            }
            Text(
                text = currentTime.value,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleMedium
            )
        }
    )
}
