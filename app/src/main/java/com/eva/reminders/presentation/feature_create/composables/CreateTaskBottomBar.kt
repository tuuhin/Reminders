package com.eva.reminders.presentation.feature_create.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun CreateTaskBottomBar(
    modifier: Modifier = Modifier,
    onMoreOptions: () -> Unit,
    onColor: () -> Unit
) {
    val currentTime = remember {
        derivedStateOf {
            val time = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("hh:mm a"))
            "Edited: $time"
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = onColor) {
            Icon(
                imageVector = Icons.Outlined.Palette,
                contentDescription = "Color Palette",
            )
        }
        Text(
            text = currentTime.value,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )
        IconButton(onClick = onMoreOptions) {
            Icon(
                imageVector = Icons.Outlined.MoreVert,
                contentDescription = "Vertical option"
            )
        }
    }
}