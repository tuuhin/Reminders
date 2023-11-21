package com.eva.reminders.presentation.feature_home.composables

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eva.reminders.R
import com.eva.reminders.domain.enums.TaskColorEnum
import com.eva.reminders.domain.models.ArrangementStyle
import com.eva.reminders.domain.models.TaskLabelModel
import com.eva.reminders.presentation.feature_home.utils.HomeSearchBarEvents
import com.eva.reminders.presentation.feature_home.utils.HomeSearchBarState
import com.eva.reminders.presentation.feature_home.utils.SearchResultsType
import com.eva.reminders.presentation.feature_home.utils.SearchType
import com.eva.reminders.presentation.utils.LocalArrangementStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeSearchBar(
    labels: List<TaskLabelModel>,
    colors: List<TaskColorEnum>,
    state: HomeSearchBarState,
    onSearchEvent: (HomeSearchBarEvents) -> Unit,
    searchResultsType: SearchResultsType,
    onSearchType: (SearchType) -> Unit,
    onArrangementChange: (ArrangementStyle) -> Unit,
    onDrawerClick: () -> Unit,
    onTaskSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
    arrangement: ArrangementStyle = LocalArrangementStyle.current,
) {

    val paddingTransition by animateDpAsState(
        targetValue = if (state.isActive) 0.dp
        else dimensionResource(id = R.dimen.scaffold_padding),
        label = "Padding Transition for the search box",
        animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing)
    )

    SearchBar(
        query = state.query,
        onQueryChange = { onSearchEvent(HomeSearchBarEvents.OnQueryChange(it)) },
        onSearch = { onSearchEvent(HomeSearchBarEvents.OnSearch(it)) },
        active = state.isActive,
        onActiveChange = { onSearchEvent(HomeSearchBarEvents.OnActiveChange(it)) },
        placeholder = { Text(text = stringResource(id = R.string.search_placeholder)) },
        leadingIcon = {
            when {
                state.isActive -> IconButton(
                    onClick = { onSearchEvent(HomeSearchBarEvents.OnActiveChange(false)) }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Close,
                        contentDescription = stringResource(id = R.string.close_icon_desc)
                    )
                }

                else -> IconButton(
                    onClick = onDrawerClick
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Menu,
                        contentDescription = stringResource(id = R.string.menu_icon_desc)
                    )
                }
            }
        },
        trailingIcon = {
            AnimatedVisibility(visible = !state.isActive) {
                ToggleArrangementButton(
                    style = arrangement,
                    onChange = onArrangementChange
                )
            }
        },
        modifier = modifier.padding(horizontal = paddingTransition),
        colors = SearchBarDefaults.colors(
            dividerColor = MaterialTheme.colorScheme.primary,
            containerColor = MaterialTheme.colorScheme.surface
        ),
    ) {
        Text(
            text = stringResource(id = R.string.search_information_text),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        )
        Crossfade(
            targetState = searchResultsType,
            label = "Search Result Transitions"
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


@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun HomeSearchBarPreview() {
    HomeSearchBar(
        onDrawerClick = {},
        arrangement = ArrangementStyle.GRID_STYLE,
        onArrangementChange = {},
        state = HomeSearchBarState(),
        labels = emptyList(),
        colors = emptyList(),
        searchResultsType = SearchResultsType.NoResultsType,
        onSearchType = {},
        onTaskSelect = {},
        onSearchEvent = {}
    )
}