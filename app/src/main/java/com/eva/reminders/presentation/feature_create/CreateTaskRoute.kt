package com.eva.reminders.presentation.feature_create

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.eva.reminders.domain.models.TaskLabelModel
import com.eva.reminders.presentation.feature_create.composables.*
import com.eva.reminders.presentation.feature_create.utils.AddTaskEvents
import com.eva.reminders.presentation.feature_create.utils.AddTaskState
import com.eva.reminders.presentation.feature_create.utils.SelectLabelState
import com.eva.reminders.presentation.utils.LocalSnackBarHostProvider
import com.eva.reminders.ui.theme.RemindersTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTaskRoute(
    state: AddTaskState,
    navigation: @Composable () -> Unit,
    labelSearchQuery: String,
    onLabelSearchQuery: (String) -> Unit,
    onLabelSelect: (SelectLabelState) -> Unit,
    onNewLabelCreate: () -> Unit,
    modifier: Modifier = Modifier,
    onAddTaskEvents: (AddTaskEvents) -> Unit,
    queriedLabels: List<SelectLabelState>,
    selectedLabels: List<TaskLabelModel>,
    snackBarHostState: SnackbarHostState = LocalSnackBarHostProvider.current
) {

    val coroutineScope = rememberCoroutineScope()

    val colorSheetState = rememberModalBottomSheetState()

    var showReminderDialog by rememberSaveable { mutableStateOf(false) }
    var showLabelPicker by rememberSaveable { mutableStateOf(false) }
    var showDeleteDialog by rememberSaveable { mutableStateOf(false) }


    if (showLabelPicker)
        TaskLabelPicker(
            onDismissRequest = { showLabelPicker = !showLabelPicker },
            labels = queriedLabels,
            onSelect = onLabelSelect,
            query = labelSearchQuery,
            onQueryChanged = onLabelSearchQuery,
            onCreateNew = onNewLabelCreate,
        )

    if (colorSheetState.isVisible)
        TaskColorPicker(
            state = colorSheetState,
            selectedColor = state.color,
            onColorChange = { newColor -> onAddTaskEvents(AddTaskEvents.OnColorChanged(newColor)) }
        )

    if (showReminderDialog) {
        TaskReminderPickerDialog(
            state = state.reminderState,
            showDelete = state.id != null,
            onDismissRequest = { showReminderDialog = !showReminderDialog },
            onRemindersEvents = { remindersEvents ->
                onAddTaskEvents(AddTaskEvents.OnReminderEvent(remindersEvents))
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
    }

    if (showDeleteDialog) {
        DeleteReminderDialog(
            onDismiss = { showDeleteDialog = !showDeleteDialog },
            onConfirm = { onAddTaskEvents(AddTaskEvents.OnDelete) },
            onCancel = { showDeleteDialog = false })
    }


    Scaffold(
        topBar = {
            CreateTaskTopBar(
                navigation = navigation,
                isPinned = state.isPinned,
                isReminder = state.isReminderPresent,
                isArchived = state.isArchived,
                onPinClick = { onAddTaskEvents(AddTaskEvents.TogglePinned) },
                onReminderClick = { showReminderDialog = !showReminderDialog },
                onArchiveClick = { onAddTaskEvents(AddTaskEvents.ToggleArchive) },
                onAddColor = { coroutineScope.launch { colorSheetState.show() } },
                onAddLabels = { showLabelPicker = !showLabelPicker },
            )
        },
        bottomBar = {
            CreateTaskBottomBar(
                isUpdate = !state.isCreate,
                editedAt = state.editedAt,
                isCopyEnabled = !state.isCreate,
                isDeleteEnabled = !state.isCreate,
                onDelete = { showDeleteDialog = true },
                onCopy = {},
                onActionClick = { onAddTaskEvents(AddTaskEvents.OnSubmit) },
                floatingActionBarColor = MaterialTheme.colorScheme.secondaryContainer,
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        contentWindowInsets = WindowInsets.ime,
        modifier = modifier
    ) { scPadding ->
        TaskFields(
            state = state,
            scaffoldPadding = scPadding,
            selectedLabels = selectedLabels,
            onAddTaskEvents = onAddTaskEvents,
            onClickOnLabel = { showLabelPicker = !showLabelPicker },
            onClickOnColor = {
                coroutineScope.launch {
                    if (!colorSheetState.isVisible)
                        colorSheetState.show()
                }
            },
            onClickOnReminder = { showReminderDialog = true },
        )
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun CreateTaskRoutePreview() = RemindersTheme {
    CreateTaskRoute(
        state = AddTaskState(),
        navigation = {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
        },
        labelSearchQuery = "",
        onLabelSearchQuery = {},
        onLabelSelect = {},
        onNewLabelCreate = { },
        onAddTaskEvents = {},
        queriedLabels = emptyList(),
        selectedLabels = emptyList(),
    )
}