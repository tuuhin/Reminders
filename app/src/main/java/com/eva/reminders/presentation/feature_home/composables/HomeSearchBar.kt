package com.eva.reminders.presentation.feature_home.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.ViewList
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.eva.reminders.presentation.feature_home.utils.TaskArrangementEvent
import com.eva.reminders.presentation.feature_home.utils.TaskArrangementStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeSearchBar(
    modifier: Modifier = Modifier,
    onDrawerClick: () -> Unit,
    arrangement: TaskArrangementStyle,
    onArrangementChange: (TaskArrangementEvent) -> Unit,
) {
    TopAppBar(
        title = { Text("App Name") },
        navigationIcon = {
            IconButton(
                onClick = onDrawerClick
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu"
                )
            }
        },
        modifier = modifier,
        actions = {
            IconButton(
                onClick = {
                    when (arrangement) {
                        TaskArrangementStyle.GRID_STYLE -> onArrangementChange(TaskArrangementEvent.BlockStyleEvent)
                        TaskArrangementStyle.BLOCK_STYLE -> onArrangementChange(TaskArrangementEvent.GridStyleEvent)
                    }
                }
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
    )
}

@Preview
@Composable
fun HomeSearchBarPreview() {
    HomeSearchBar(
        onDrawerClick = {},
        arrangement = TaskArrangementStyle.GRID_STYLE,
        onArrangementChange = {}
    )
}