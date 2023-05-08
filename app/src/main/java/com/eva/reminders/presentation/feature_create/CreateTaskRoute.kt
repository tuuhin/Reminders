package com.eva.reminders.presentation.feature_create

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Update
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
import com.eva.reminders.presentation.feature_create.utils.AddTaskEvents
import com.eva.reminders.presentation.feature_create.utils.AddTaskState
import com.eva.reminders.presentation.feature_labels.utils.SelectLabelState
import com.eva.reminders.presentation.utils.UIEvents
import com.eva.reminders.presentation.utils.noColor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateReminderRoute(
    state: AddTaskState,
    navController: NavController,
    labelSearchQuery: String,
    onLabelSearchQuery: (String) -> Unit,
    onLabelSelect: (SelectLabelState) -> Unit,
    onNewLabelCreate: () -> Unit,
    modifier: Modifier = Modifier,
    onAddTaskEvents: (AddTaskEvents) -> Unit,
    queriedLabels: List<SelectLabelState>,
    pickedLabels: List<TaskLabelModel>,
    uiEvents: Flow<UIEvents>,
) {

    val colorSheetState = rememberModalBottomSheetState()
    val optionsSheetState = rememberModalBottomSheetState()

    val scope = rememberCoroutineScope()

    val titleFocus = remember { FocusRequester() }
    val contentFocus = remember { FocusRequester() }

    val snackBarHostState = remember { SnackbarHostState() }

    var showReminderDialog by remember { mutableStateOf(false) }
    var showLabelPicker by remember { mutableStateOf(false) }


    LaunchedEffect(Unit) {
        if (state.isCreate)
            titleFocus.requestFocus()
    }

    LaunchedEffect(key1 = Unit) {
        uiEvents.collect { event ->
            when (event) {
                is UIEvents.ShowSnackBar -> snackBarHostState.showSnackbar(event.message)
                UIEvents.NavigateBack -> navController.navigateUp()
            }
        }
    }


    TaskLabelPicker(
        show = showLabelPicker,
        onDismissRequest = { showLabelPicker = !showLabelPicker },
        labels = queriedLabels,
        onSelect = onLabelSelect,
        query = labelSearchQuery,
        onQueryChanged = onLabelSearchQuery,
        onCreateNew = onNewLabelCreate,
    )
    Scaffold(
        topBar = {
            CreateTaskTopBar(
                navController = navController,
                isPinned = state.isPinned,
                isReminder = state.isReminderPresent,
                isArchived = state.isArchived,
                onPinClick = { onAddTaskEvents(AddTaskEvents.TogglePinned) },
                onReminderClick = { showReminderDialog = !showReminderDialog },
                onArchiveClick = { onAddTaskEvents(AddTaskEvents.ToggleArchive) }
            )
        },
        bottomBar = {
            CreateTaskBottomBar(
                onColor = { scope.launch { colorSheetState.show() } },
                onMoreOptions = { scope.launch { optionsSheetState.show() } },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = { onAddTaskEvents(AddTaskEvents.OnSubmit) }
                    ) {
                        if (state.isCreate)
                            Icon(
                                imageVector = Icons.Outlined.Check,
                                contentDescription = "Add this task"
                            )
                        else
                            Icon(
                                imageVector = Icons.Outlined.Update,
                                contentDescription = "Update this task"
                            )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
    ) { padding ->

        TaskReminderPicker(
            state = state.reminderState,
            showDialog = showReminderDialog,
            onDismissRequest = { showReminderDialog = !showReminderDialog },
            onRemindersEvents = {
                onAddTaskEvents(AddTaskEvents.OnReminderEvent(it))
            },
            onDelete = {
                onAddTaskEvents(AddTaskEvents.ReminderStateUnpicked)
                showReminderDialog = false
            },
            onSave = {
                onAddTaskEvents(AddTaskEvents.ReminderStatePicked)
                showReminderDialog = false
            }
        )

        TaskColorPicker(
            isVisible = colorSheetState.isVisible,
            state = colorSheetState,
            selectedColor = state.color,
            onColorChange = { onAddTaskEvents(AddTaskEvents.OnColorChanged(it)) }
        )
        MoreOptionsPicker(
            isVisible = optionsSheetState.isVisible,
            sheetState = optionsSheetState,
            onLabels = {
                scope.launch {
                    if (optionsSheetState.isVisible)
                        optionsSheetState.hide()
                }
                showLabelPicker = !showLabelPicker
            },
            onDelete = { onAddTaskEvents(AddTaskEvents.OnDelete) },
            deleteEnabled = !state.isCreate,
            makeCopyEnabled = !state.isCreate,
            onCopy = { onAddTaskEvents(AddTaskEvents.MakeCopy) }
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
                        onAddTaskEvents(AddTaskEvents.OnTitleChange(it))
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
                    onValueChange = { onAddTaskEvents(AddTaskEvents.OnContentChange(it)) },
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
                    selectedLabels = pickedLabels,
                    onLabelClick = { showLabelPicker = !showLabelPicker },
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
            }
            item {
                PickedReminder(
                    show = state.isReminderPresent,
                    state = state.reminderState,
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