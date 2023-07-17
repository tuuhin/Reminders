package com.eva.reminders.presentation.feature_home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.eva.reminders.R
import com.eva.reminders.domain.enums.TaskColorEnum
import com.eva.reminders.domain.models.TaskLabelModel
import com.eva.reminders.domain.models.TaskModel
import com.eva.reminders.presentation.feature_home.composables.DrawerContent
import com.eva.reminders.presentation.feature_home.composables.HomeSearchBar
import com.eva.reminders.presentation.feature_home.composables.TasksLayout
import com.eva.reminders.presentation.feature_home.utils.HomeSearchBarEvents
import com.eva.reminders.presentation.feature_home.utils.HomeSearchBarState
import com.eva.reminders.presentation.feature_home.utils.SearchResultsType
import com.eva.reminders.presentation.feature_home.utils.SearchType
import com.eva.reminders.presentation.feature_home.utils.TaskArrangementEvent
import com.eva.reminders.presentation.feature_home.utils.TaskArrangementStyle
import com.eva.reminders.presentation.utils.HomeTabs
import com.eva.reminders.presentation.utils.LocalArrangementStyle
import com.eva.reminders.presentation.utils.NavConstants
import com.eva.reminders.presentation.utils.NavRoutes
import com.eva.reminders.presentation.utils.ShowContent
import com.eva.reminders.presentation.utils.UIEvents
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@Composable
fun HomeRoute(
    navController: NavController,
    labels: List<TaskLabelModel>,
    tasks: ShowContent<List<TaskModel>>,
    tab: HomeTabs,
    searchBarState: HomeSearchBarState,
    colorOptions: List<TaskColorEnum>,
    searchResultsType: SearchResultsType,
    onSearchType: (SearchType) -> Unit,
    onArrangementChange: (TaskArrangementEvent) -> Unit,
    onSearchBarEvents: (HomeSearchBarEvents) -> Unit,
    onTabChange: (HomeTabs) -> Unit,
    uiEvents: Flow<UIEvents>,
    modifier: Modifier = Modifier,
    style: TaskArrangementStyle = LocalArrangementStyle.current
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = Unit) {
        uiEvents.collect { event ->
            when (event) {
                is UIEvents.ShowSnackBar -> snackBarHostState.showSnackbar(event.message)
                else -> {}
            }
        }
    }


    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.fillMaxWidth(0.75f),
                windowInsets = WindowInsets.statusBars
            ) {
                DrawerContent(
                    labels = labels,
                    tab = tab,
                    onTabChange = onTabChange,
                    onEdit = { navController.navigate(NavRoutes.EditLabels.route) },
                    modifier = Modifier.padding(4.dp),
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
                    onTaskSelect = { taskId ->
                        navController.navigate(NavRoutes.AddTask.route + "?${NavConstants.TASK_ID}=$taskId")
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    onClick = {
                        navController.navigate(NavRoutes.AddTask.route)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Task"
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(text = "Add")
                }
            },
            snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        ) { padding ->
            Column(
                modifier = modifier
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
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    when {
                        tasks.content.isEmpty() -> Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.bell),
                                contentDescription = "No associated notes are found",
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.surfaceTint)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = stringResource(id = R.string.no_reminders),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }

                        else -> TasksLayout(
                            tasks = tasks.content,
                            style = style,
                            onTaskSelect = { taskId ->
                                navController
                                    .navigate(NavRoutes.AddTask.route + "?${NavConstants.TASK_ID}=$taskId")
                            }
                        )
                    }
                }
            }
        }
    }
}