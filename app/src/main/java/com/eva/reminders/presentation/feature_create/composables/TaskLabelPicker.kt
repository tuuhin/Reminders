package com.eva.reminders.presentation.feature_create.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.eva.reminders.R
import com.eva.reminders.presentation.feature_labels.utils.SelectLabelState
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@OptIn(ExperimentalMaterial3Api::class)
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
        val systemUiController = rememberSystemUiController()
        val surface = MaterialTheme.colorScheme.surface
        val surfaceVariant = MaterialTheme.colorScheme.surfaceVariant

        DisposableEffect(systemUiController) {
            systemUiController.setStatusBarColor(surfaceVariant)
            onDispose {
                systemUiController.setStatusBarColor(surface)
            }
        }
        Dialog(
            onDismissRequest = onDismissRequest,
            properties = DialogProperties(
                usePlatformDefaultWidth = false
            )
        ) {
            SearchBar(
                query = query,
                onQueryChange = onQueryChanged,
                active = true,
                onActiveChange = {},
                onSearch = {},
                colors = SearchBarDefaults
                    .colors(
                        dividerColor = MaterialTheme.colorScheme.primary,
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                placeholder = { Text(text = "Search..") },
                leadingIcon = {
                    IconButton(onClick = onDismissRequest) {
                        Icon(
                            imageVector = Icons.Outlined.Close,
                            contentDescription = "Close the search box"
                        )
                    }
                }
            ) {
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(vertical = 4.dp)
                ) {
                    AnimatedVisibility(
                        visible = labels.isEmpty() && query.isEmpty(),
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.loupe),
                                contentDescription = "Not found"
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "No labels are found",
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    }
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
                                contentDescription = "Add a new Label",
                                modifier = Modifier.weight(.1f),
                                tint = MaterialTheme.colorScheme.surfaceTint
                            )
                            Spacer(modifier = Modifier.weight(.1f))
                            Text(
                                text = "Create New Label",
                                modifier = Modifier.weight(.9f)
                            )
                        }
                    }
                }
            }
        }
    }
}