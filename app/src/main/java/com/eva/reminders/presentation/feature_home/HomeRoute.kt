package com.eva.reminders.presentation.feature_home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.eva.reminders.R
import com.eva.reminders.domain.enums.TaskColorEnum
import com.eva.reminders.domain.models.ArrangementStyle
import com.eva.reminders.domain.models.TaskLabelModel
import com.eva.reminders.domain.models.TaskModel
import com.eva.reminders.presentation.feature_home.composables.DrawerContent
import com.eva.reminders.presentation.feature_home.composables.HomeSearchBar
import com.eva.reminders.presentation.feature_home.composables.NoTasksToShow
import com.eva.reminders.presentation.feature_home.composables.TasksLayout
import com.eva.reminders.presentation.feature_home.utils.HomeSearchBarEvents
import com.eva.reminders.presentation.feature_home.utils.HomeSearchBarState
import com.eva.reminders.presentation.feature_home.utils.SearchResultsType
import com.eva.reminders.presentation.feature_home.utils.SearchType
import com.eva.reminders.presentation.navigation.NavigationTestTags
import com.eva.reminders.presentation.utils.HomeTabs
import com.eva.reminders.presentation.utils.LocalArrangementStyle
import com.eva.reminders.presentation.utils.LocalSnackBarHostProvider
import com.eva.reminders.presentation.utils.ShowContent
import kotlinx.coroutines.launch

@Composable
fun HomeRoute(
    labels: List<TaskLabelModel>,
    tasks: ShowContent<List<TaskModel>>,
    tab: HomeTabs,
    searchBarState: HomeSearchBarState,
    colorOptions: List<TaskColorEnum>,
    searchResultsType: SearchResultsType,
    onSearchType: (SearchType) -> Unit,
    onArrangementChange: (ArrangementStyle) -> Unit,
    onSearchBarEvents: (HomeSearchBarEvents) -> Unit,
    onTabChange: (HomeTabs) -> Unit,
    onAdd: () -> Unit,
    onEditRoute: () -> Unit,
    onTaskSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
    style: ArrangementStyle = LocalArrangementStyle.current,
    snackBarHostState: SnackbarHostState = LocalSnackBarHostProvider.current,
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.fillMaxWidth(0.75f),
            ) {
                DrawerContent(
                    labels = labels,
                    tab = tab,
                    onTabChange = onTabChange,
                    onEdit = onEditRoute,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }
        },
        gesturesEnabled = true
    ) {
        Scaffold(
            topBar = {
                HomeSearchBar(
                    onDrawerClick = { scope.launch { drawerState.open() } },
                    onArrangementChange = onArrangementChange,
                    labels = labels,
                    colors = colorOptions,
                    searchResultsType = searchResultsType,
                    onSearchType = onSearchType,
                    state = searchBarState,
                    onSearchEvent = onSearchBarEvents,
                    onTaskSelect = onTaskSelect,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    onClick = onAdd,
                    shape = MaterialTheme.shapes.large,
                    elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(id = R.string.create_new_task_button)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = stringResource(id = R.string.create_new_task_text),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            },
            snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
            contentWindowInsets = WindowInsets.systemBars,
            modifier = modifier.testTag(NavigationTestTags.SHOW_TASKS_ROUTE)
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
            ) {
                if (tasks.isLoading)
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                        content = { CircularProgressIndicator() }
                    )
                AnimatedVisibility(
                    visible = !tasks.isLoading,
                    enter = fadeIn() + expandVertically(expandFrom = Alignment.Bottom),
                    exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Top),
                    modifier = Modifier.fillMaxSize()
                ) {
                    when {
                        tasks.content.isEmpty() -> NoTasksToShow(modifier = Modifier.fillMaxSize())
                        else -> TasksLayout(
                            tasks = tasks.content,
                            style = style,
                            onTaskSelect = { task -> onTaskSelect(task.id) },
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }
}