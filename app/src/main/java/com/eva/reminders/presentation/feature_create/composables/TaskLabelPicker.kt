package com.eva.reminders.presentation.feature_create.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.eva.reminders.presentation.feature_labels.utils.SelectLabelState

@Composable
fun TaskLabelPicker(
    show: Boolean,
    query: String,
    onQueryChanged: (String) -> Unit,
    labels: List<SelectLabelState>,
    onDismissRequest: () -> Unit,
    onSelect: (SelectLabelState) -> Unit,
    onCreateNew: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = show,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Dialog(
            onDismissRequest = onDismissRequest
        ) {
            Card(
                modifier = modifier
                    .fillMaxWidth()
                    .fillMaxHeight(.7f),
                shape = MaterialTheme.shapes.extraLarge
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                ) {
                    Text(
                        text = "Select Your labels",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        text = "Pick the Labels that you wanna associate with this task",
                        style = MaterialTheme.typography.labelLarge
                    )
                    Divider(modifier = Modifier.padding(vertical = 2.dp))
                    TextField(
                        value = query,
                        onValueChange = onQueryChanged,
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        textStyle = MaterialTheme.typography.bodyMedium,
                        placeholder = {
                            Text(
                                text = "Enter label name",
                                color = MaterialTheme.colorScheme.outline,
                            )
                        },
                        maxLines = 1,
                    )
                    Divider()
                    LazyColumn {
                        itemsIndexed(labels) { _, item ->
                            CheckLabelItem(
                                item = item,
                                onSelect = { onSelect(item) }
                            )
                        }
                    }
                    AnimatedVisibility(
                        visible = query.isNotEmpty(),
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable(onClick = onCreateNew, role = Role.Button),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Add,
                                contentDescription = null,
                                modifier = Modifier.weight(.1f),
                                tint = MaterialTheme.colorScheme.surfaceTint
                            )
                            Spacer(modifier = Modifier.weight(.1f))
                            Text(text = "Create New Label", modifier = Modifier.weight(.9f))
                        }
                    }
                }
            }
        }
    }
}