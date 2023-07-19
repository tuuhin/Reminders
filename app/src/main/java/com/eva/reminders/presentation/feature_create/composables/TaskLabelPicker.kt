package com.eva.reminders.presentation.feature_create.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.eva.reminders.R
import com.eva.reminders.presentation.feature_create.utils.SelectLabelState
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class
)
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
    searchBoxColor: Color = MaterialTheme.colorScheme.inverseOnSurface,
    surfaceColor: Color = MaterialTheme.colorScheme.surface
) {
    AnimatedVisibility(
        visible = show,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        val systemUiController = rememberSystemUiController()

        DisposableEffect(systemUiController) {
            systemUiController.setStatusBarColor(searchBoxColor)
            onDispose {
                systemUiController.setStatusBarColor(surfaceColor)
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
                        dividerColor = MaterialTheme.colorScheme.secondary,
                        containerColor = searchBoxColor
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
                            Icon(
                                painter = painterResource(id = R.drawable.ic_search),
                                contentDescription = "Not found",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "No labels are found",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(2.dp),
                        contentPadding = PaddingValues(8.dp)
                    ) {
                        itemsIndexed(
                            labels, key = { _, item -> item.idx }
                        ) { _, item ->
                            CheckLabelItem(
                                item = item,
                                onSelect = { onSelect(item) },
                                modifier = Modifier.animateItemPlacement(
                                    tween(100, easing = EaseInOut)
                                )
                            )
                        }
                    }
                    AnimatedVisibility(
                        visible = query.isNotEmpty(),
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        CreateNewLabelCard(onClick = onCreateNew)
                    }
                }
            }
        }
    }
}