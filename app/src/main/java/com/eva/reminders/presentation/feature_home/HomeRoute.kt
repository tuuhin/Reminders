package com.eva.reminders.presentation.feature_home

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.eva.reminders.presentation.feature_home.composables.DrawerContent
import com.eva.reminders.presentation.utils.NavRoutes
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeRoute(
    navController: NavController,
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
                DrawerContent(modifier = Modifier.padding(4.dp))
            }
        },
        gesturesEnabled = true
    ) {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text("App Name") }, navigationIcon = {
                    IconButton(
                        onClick = {
                            scope.launch {
                                drawerState.open()
                            }
                        }) {
                        Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")
                    }
                })
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { navController.navigate(NavRoutes.AddReminder.route) }) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add reminder")
                }
            }
        ) { padding ->
            Column(
                modifier = modifier.padding(padding)
            ) {

            }
        }
    }
}