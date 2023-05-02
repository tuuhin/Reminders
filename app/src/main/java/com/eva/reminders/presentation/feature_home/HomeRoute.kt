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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.eva.reminders.R
import com.eva.reminders.domain.models.TaskLabelModel
import com.eva.reminders.domain.models.TaskModel
import com.eva.reminders.presentation.feature_home.composables.DrawerContent
import com.eva.reminders.presentation.feature_home.composables.HomeSearchBar
import com.eva.reminders.presentation.feature_home.composables.TasksGridLayout
import com.eva.reminders.presentation.feature_home.composables.TasksLinearLayout
import com.eva.reminders.presentation.feature_home.utils.TaskArrangementEvent
import com.eva.reminders.presentation.feature_home.utils.TaskArrangementStyle
import com.eva.reminders.presentation.utils.HomeTabs
import com.eva.reminders.presentation.utils.NavRoutes
import com.eva.reminders.presentation.utils.ShowContent
import kotlinx.coroutines.launch

@Composable
fun HomeRoute(
    navController: NavController,
    labels: List<TaskLabelModel>,
    tasks: ShowContent<List<TaskModel>>,
    tab: HomeTabs,
    arrangement: TaskArrangementStyle,
    onArrangementChange: (TaskArrangementEvent) -> Unit,
    onTabChange: (HomeTabs) -> Unit,
    modifier: Modifier = Modifier,
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()


    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.fillMaxWidth(0.75f),
                windowInsets = WindowInsets.statusBars
            ) {
                DrawerContent(
                    navController = navController,
                    labels = labels,
                    tab = tab,
                    onTabChange = onTabChange,
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
                    arrangement = arrangement,
                    onArrangementChange = onArrangementChange,
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        navController.navigate(NavRoutes.AddTask.route)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Task"
                    )
                }
            },
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
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    if (!tasks.isLoading && tasks.content.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.label),
                                contentDescription = "No associated notes are found",
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.surfaceTint)
                            )
                        }
                    } else
                        when (arrangement) {
                            TaskArrangementStyle.GRID_STYLE -> TasksGridLayout(
                                tasks = tasks.content,
                                modifier = Modifier.fillMaxHeight()
                            )

                            TaskArrangementStyle.BLOCK_STYLE -> TasksLinearLayout(
                                tasks = tasks.content,
                                modifier = Modifier.fillMaxHeight()
                            )
                        }
                }
            }
        }
    }
}