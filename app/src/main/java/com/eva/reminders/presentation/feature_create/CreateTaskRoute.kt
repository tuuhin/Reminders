package com.eva.reminders.presentation.feature_create

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.eva.reminders.domain.enums.TaskColorEnum
import com.eva.reminders.domain.models.TaskLabelModel
import com.eva.reminders.presentation.feature_create.composables.*
import com.eva.reminders.presentation.feature_create.utils.TaskReminderState
import com.eva.reminders.presentation.feature_create.utils.TaskRemindersEvents
import com.eva.reminders.presentation.utils.NavRoutes
import com.eva.reminders.presentation.utils.noColor
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateReminderRoute(
    state: CreateTaskState,
    navController: NavController,
    modifier: Modifier = Modifier,
    onCreateTaskEvents: (CreateTaskEvents) -> Unit,
    reminderState: TaskReminderState,
    onRemindersEvents: (TaskRemindersEvents) -> Unit,
    selectedLabels: List<TaskLabelModel>? = null
) {
    val colorSheetState = rememberModalBottomSheetState()
    val optionsSheetState = rememberModalBottomSheetState()

    val scope = rememberCoroutineScope()

    val titleFocus = remember { FocusRequester() }
    val contentFocus = remember { FocusRequester() }

    var showReminderDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { titleFocus.requestFocus() }

    Scaffold(
        topBar = {
            CreateTaskTopBar(
                navController = navController,
                isPinned = state.isPinned,
                isReminder = state.isReminderSelected,
                isArchived = state.isArchived,
                onPinClick = { onCreateTaskEvents(CreateTaskEvents.TogglePinned) },
                onReminderClick = { showReminderDialog = !showReminderDialog },
                onArchiveClick = { onCreateTaskEvents(CreateTaskEvents.ToggleArchive) }
            )
        },
        bottomBar = {
            CreateTaskBottomBar(
                onColor = { scope.launch { colorSheetState.show() } },
                onMoreOptions = { scope.launch { optionsSheetState.show() } },
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onCreateTaskEvents(CreateTaskEvents.OnSubmit) }
            ) {
                Icon(
                    imageVector = Icons.Outlined.Check,
                    contentDescription = "Add this task"
                )
            }
        }
    ) { padding ->

        TaskReminderPicker(
            state = reminderState,
            showDialog = showReminderDialog,
            onDismissRequest = { showReminderDialog = !showReminderDialog },
            onRemindersEvents = onRemindersEvents,
            onDelete = {
                onCreateTaskEvents(CreateTaskEvents.ReminderCanceled)
                showReminderDialog = false
            },
            onSave = {
                onCreateTaskEvents(CreateTaskEvents.ReminderPicked)
                showReminderDialog = false
            }
        )

        TaskColorPicker(
            isVisible = colorSheetState.isVisible,
            state = colorSheetState,
            selectedColor = state.color,
            onColorChange = { onCreateTaskEvents(CreateTaskEvents.OnColorChanged(it)) }
        )

        MoreOptionsPicker(
            isVisible = optionsSheetState.isVisible,
            sheetState = optionsSheetState,
            onLabels = {
                scope.launch {
                    if (optionsSheetState.isVisible)
                        optionsSheetState.hide()
                }
                navController.navigate(NavRoutes.AddLabels.route)
            }
        )
        LazyColumn(
            modifier = modifier
                .fillMaxSize(),
            contentPadding = padding
        ) {
            item {
                TextField(
                    value = state.title,
                    onValueChange = {
                        onCreateTaskEvents(CreateTaskEvents.OnTitleChange(it))
                    },
                    textStyle = MaterialTheme.typography.headlineSmall,
                    placeholder = {
                        Text(
                            text = "Title", style = MaterialTheme.typography.headlineSmall
                        )
                    },
                    colors = TextFieldDefaults.noColor(),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words, imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(onNext = { contentFocus.requestFocus() }),
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(titleFocus)
                )
            }
            item {
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
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        keyboardType = KeyboardType.Text,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(contentFocus)
                )
            }
            item {
                PickedLabels(
                    selectedLabels = selectedLabels,
                    onLabelClick = { navController.navigate(NavRoutes.AddLabels.route) },
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
            }
            item {
                PickedReminder(
                    show = state.isReminderSelected,
                    state = reminderState,
                    onClick = {
                        showReminderDialog = true
                    },
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
            }
            item {
                PickedColor(
                    color = if (state.color != TaskColorEnum.TRANSPARENT) state.color else null,
                    onClick = {
                        scope.launch {
                            if (!colorSheetState.isVisible)
                                colorSheetState.show()
                        }
                    },
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
            }

        }
    }
}