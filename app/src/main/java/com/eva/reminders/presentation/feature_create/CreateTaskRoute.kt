package com.eva.reminders.presentation.feature_create

import androidx.compose.foundation.layout.*
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
import com.eva.reminders.domain.models.TaskLabelModel
import com.eva.reminders.presentation.feature_create.composables.*
import com.eva.reminders.presentation.feature_create.utils.TaskReminderState
import com.eva.reminders.presentation.feature_create.utils.TaskRemindersEvents
import com.eva.reminders.presentation.utils.NavRoutes
import com.eva.reminders.presentation.utils.noColor
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
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

    LaunchedEffect(Unit) { titleFocus.requestFocus() }

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
        },
        bottomBar = {
            CreateTaskBottomBar(
                onColor = {
                    scope.launch {
                        colorSheetState.show()
                    }
                }, onMoreOptions = {
                    scope.launch {
                        optionsSheetState.show()
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onCreateTaskEvents(CreateTaskEvents.OnSubmit) }
            ) {
                Icon(imageVector = Icons.Outlined.Check, contentDescription = "Add this task")
            }
        }
    ) { padding ->

        TaskReminderPicker(
            state = reminderState,
            showDialog = state.isReminder,
            onDismissRequest = { onCreateTaskEvents(CreateTaskEvents.ToggleReminder) },
            onRemindersEvents = onRemindersEvents,
            onDelete = {},
            onSave = {}
        )

        TaskColorPicker(
            isVisible = colorSheetState.isVisible,
            state = colorSheetState,
            selectedColor = state.color,
            onColorChange = { onCreateTaskEvents(CreateTaskEvents.OnColorChanged(it)) }
        )

        MoreOptionsPicker(isVisible = optionsSheetState.isVisible,
            sheetState = optionsSheetState,
            onDelete = {},
            onCopy = {},
            onLabels = {}
        )

        Column(
            modifier = modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 8.dp)
        ) {
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
                keyboardActions = KeyboardActions(onDone = {}),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(contentFocus)
            )
            Text(
                text = "Suggestions",
                color = MaterialTheme.colorScheme.outline,
                style = MaterialTheme.typography.titleMedium
            )
            if (selectedLabels.isNullOrEmpty())
                SuggestionChip(
                    onClick = { navController.navigate(NavRoutes.AddLabels.route) },
                    label = { Text(text = "Add Labels") },
                )
            else
                FlowRow(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    selectedLabels.forEach { item ->
                        AssistChip(
                            onClick = { },
                            label = { Text(text = item.label) },
                            border = AssistChipDefaults
                                .assistChipBorder(
                                    borderColor = MaterialTheme.colorScheme.primary
                                ),
                            colors = AssistChipDefaults
                                .assistChipColors(
                                    labelColor = MaterialTheme.colorScheme.primary
                                ),
                            modifier = Modifier.padding(horizontal = 2.dp)
                        )
                    }
                }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}