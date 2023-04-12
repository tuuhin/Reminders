package com.eva.reminders.presentation.feature_create

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.eva.reminders.presentation.feature_create.composables.CreateTaskBottomBar
import com.eva.reminders.presentation.feature_create.composables.CreateTaskTopBar
import com.eva.reminders.presentation.feature_create.composables.MoreOptionsPicker
import com.eva.reminders.presentation.utils.noColor
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateReminderRoute(
    state: CreateTaskState,
    navController: NavController,
    modifier: Modifier = Modifier,
    onCreateTaskEvents: (CreateTaskEvents) -> Unit
) {
    val colorSheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            CreateTaskTopBar(
                navController = navController,
                isPinned = state.isPinned,
                isReminder = state.isReminder,
                isArchived = state.isArchived,
                onPinClick = { onCreateTaskEvents(CreateTaskEvents.TogglePinned) },
                onReminderClick = { onCreateTaskEvents(CreateTaskEvents.ToggleReminder) },
                onArchiveClick = { onCreateTaskEvents(CreateTaskEvents.ToggleArchive) }
            )
        }
    ) { padding ->

        if (colorSheetState.isVisible)
            MoreOptionsPicker(sheetState = colorSheetState)

        Column(
            modifier = modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            TextField(
                value = state.title,
                onValueChange = {
                    onCreateTaskEvents(CreateTaskEvents.OnTitleChange(it))
                },
                textStyle = MaterialTheme.typography.headlineSmall,
                placeholder = {
                    Text(
                        text = "Title",
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                colors = TextFieldDefaults.noColor(),
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = state.content,
                onValueChange = { onCreateTaskEvents(CreateTaskEvents.OnContentChange(it)) },
                textStyle = MaterialTheme.typography.titleMedium,
                placeholder = {
                    Text(
                        text = "Note",
                        style = MaterialTheme.typography.titleMedium,
                    )
                },
                singleLine = false,
                colors = TextFieldDefaults.noColor(),
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.weight(1f))
            CreateTaskBottomBar(
                onColor = {
                    scope.launch { colorSheetState.show() }
                },
                onMoreOptions = {}
            )
        }
    }
}