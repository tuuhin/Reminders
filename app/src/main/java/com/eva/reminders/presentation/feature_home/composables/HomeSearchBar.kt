package com.eva.reminders.presentation.feature_home.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.ViewList
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eva.reminders.R
import com.eva.reminders.domain.enums.TaskColorEnum
import com.eva.reminders.domain.models.TaskLabelModel
import com.eva.reminders.presentation.feature_home.utils.SearchResultsType
import com.eva.reminders.presentation.feature_home.utils.SearchType
import com.eva.reminders.presentation.feature_home.utils.TaskArrangementEvent
import com.eva.reminders.presentation.feature_home.utils.TaskArrangementStyle
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeSearchBar(
    labels: List<TaskLabelModel>,
    colors: List<TaskColorEnum>,
    arrangement: TaskArrangementStyle,
    searchResultsType: SearchResultsType,
    onSearchType: (SearchType) -> Unit,
    onArrangementChange: (TaskArrangementEvent) -> Unit,
    onDrawerClick: () -> Unit,
    onTaskSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isActive by remember { mutableStateOf(false) }
    var query by remember { mutableStateOf("") }

    //Status bar color
    val systemUiController = rememberSystemUiController()

    val statusBarColorFocused = MaterialTheme.colorScheme.surfaceVariant
    val statusBarColorUnFocused = MaterialTheme.colorScheme.surface


    LaunchedEffect(systemUiController, isActive) {
        systemUiController.setNavigationBarColor(
            color = if (isActive) statusBarColorFocused else statusBarColorUnFocused
        )
        systemUiController.setStatusBarColor(
            color = if (isActive) statusBarColorFocused else statusBarColorUnFocused
        )
    }

    SearchBar(
        query = query,
        onQueryChange = { query = it },
        onSearch = { searchedText ->
            if (searchedText.isNotEmpty())
                onSearchType(SearchType.BasicSearch(searchedText))
            else
                onSearchType(SearchType.BlankSearch)
        },
        active = isActive,
        onActiveChange = {
            onSearchType(SearchType.BlankSearch)
            isActive = it
            query = ""
        },
        placeholder = { Text(text = "Search ....") },
        leadingIcon = {
            if (!isActive)
                IconButton(onClick = onDrawerClick) {
                    Icon(
                        imageVector = Icons.Outlined.Menu,
                        contentDescription = "Menu Item"
                    )
                }
            else
                IconButton(onClick = { isActive = false }) {
                    Icon(
                        imageVector = Icons.Outlined.Close,
                        contentDescription = "Close the search bar"
                    )
                }

        },
        trailingIcon = {
            AnimatedVisibility(
                visible = !isActive,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                IconButton(
                    onClick = {
                        when (arrangement) {
                            TaskArrangementStyle.GRID_STYLE -> onArrangementChange(
                                TaskArrangementEvent.BlockStyleEvent
                            )

                            TaskArrangementStyle.BLOCK_STYLE -> onArrangementChange(
                                TaskArrangementEvent.GridStyleEvent
                            )
                        }
                    },
                ) {
                    Icon(
                        imageVector = when (arrangement) {
                            TaskArrangementStyle.GRID_STYLE -> Icons.Outlined.GridView
                            TaskArrangementStyle.BLOCK_STYLE -> Icons.Outlined.ViewList
                        },
                        contentDescription = "Change Arrangement"
                    )
                }
            }
        },
        modifier = modifier,
        colors = SearchBarDefaults.colors(
            dividerColor = MaterialTheme.colorScheme.primary,
            containerColor = MaterialTheme.colorScheme.surface
        ),
    ) {
        Text(
            text = stringResource(id = R.string.search_help),
            color = MaterialTheme.colorScheme.tertiary,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        )
        Crossfade(
            targetState = searchResultsType
        ) { results ->
            when (results) {
                SearchResultsType.NoResultsType -> SearchResultsNoResults(
                    labels = labels,
                    colors = colors,
                    onSearchType = onSearchType
                )

                is SearchResultsType.SearchResults -> TaskSearchResults(
                    tasks = results.tasks,
                    arrangement = arrangement,
                    onTaskSelect = onTaskSelect
                )
            }
        }
    }
}

@Preview
@Composable
fun HomeSearchBarPreview() {
    HomeSearchBar(
        onDrawerClick = {},
        arrangement = TaskArrangementStyle.GRID_STYLE,
        onArrangementChange = {},
        labels = emptyList(),
        colors = emptyList(),
        searchResultsType = SearchResultsType.NoResultsType,
        onSearchType = {
        }, onTaskSelect = {

        }
    )
}