package com.eva.reminders.presentation.feature_home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.eva.reminders.domain.models.TaskLabelModel
import com.eva.reminders.domain.models.TaskModel
import com.eva.reminders.presentation.feature_home.composables.DrawerContent
import com.eva.reminders.presentation.feature_home.composables.ReminderCard
import com.eva.reminders.presentation.utils.HomeTabs
import com.eva.reminders.presentation.utils.NavRoutes
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeRoute(
    navController: NavController,
    labels: List<TaskLabelModel>,
    tasks: List<TaskModel>,
    tab: HomeTabs,
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
                TopAppBar(
                    title = { Text("App Name") },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                scope.launch { drawerState.open() }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu"
                            )
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { navController.navigate(NavRoutes.AddTask.route) }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Task"
                    )
                }
            }
        ) { padding ->
            Column(
                modifier = modifier.padding(padding)
            ) {
                LazyColumn {
                    itemsIndexed(tasks) { _, item ->
                        ReminderCard(
                            taskModel = item,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}