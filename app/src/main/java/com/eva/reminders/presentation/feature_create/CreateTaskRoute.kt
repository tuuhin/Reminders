package com.eva.reminders.presentation.feature_create

import android.content.res.Configuration
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imeNestedScroll
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.eva.reminders.domain.models.TaskLabelModel
import com.eva.reminders.presentation.feature_create.composables.CreateCopyDialog
import com.eva.reminders.presentation.feature_create.composables.CreateTaskBottomBar
import com.eva.reminders.presentation.feature_create.composables.CreateTaskTopBar
import com.eva.reminders.presentation.feature_create.composables.DeleteReminderDialog
import com.eva.reminders.presentation.feature_create.composables.TaskColorPicker
import com.eva.reminders.presentation.feature_create.composables.TaskFields
import com.eva.reminders.presentation.feature_create.composables.TaskReminderPickerDialog
import com.eva.reminders.presentation.feature_create.utils.AddTaskEvents
import com.eva.reminders.presentation.feature_create.utils.AddTaskState
import com.eva.reminders.presentation.utils.LocalSnackBarHostProvider
import com.eva.reminders.ui.theme.RemindersTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun CreateTaskRoute(
    state: AddTaskState,
    navigation: @Composable () -> Unit,
    onLabelPickerDialog: () -> Unit,
    modifier: Modifier = Modifier,
    onAddTaskEvents: (AddTaskEvents) -> Unit,
    selectedLabels: List<TaskLabelModel>,
    snackBarHostState: SnackbarHostState = LocalSnackBarHostProvider.current
) {

    val coroutineScope = rememberCoroutineScope()

    val colorSheetState = rememberModalBottomSheetState()

    var showReminderDialog by rememberSaveable { mutableStateOf(false) }
    var showDeleteDialog by rememberSaveable { mutableStateOf(false) }
    var showCopyDialog by rememberSaveable { mutableStateOf(false) }

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
            onCancel = { showDeleteDialog = false },
        )
    }

    if (showCopyDialog) {
        CreateCopyDialog(
            onDismiss = { showCopyDialog = !showCopyDialog },
            onConfirm = { onAddTaskEvents(AddTaskEvents.MakeCopy) },
            onCancel = { showCopyDialog = false },
        )
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
                onAddLabels = onLabelPickerDialog,
            )
        },
        bottomBar = {
            CreateTaskBottomBar(
                isUpdate = !state.isCreate,
                editedAt = state.editedAt,
                isCopyEnabled = !state.isCreate,
                isDeleteEnabled = !state.isCreate,
                onDelete = { showDeleteDialog = true },
                onCopy = { showCopyDialog = true },
                onActionClick = { onAddTaskEvents(AddTaskEvents.OnSubmit) },
                onAddColor = { coroutineScope.launch { colorSheetState.show() } },
                modifier = Modifier.imePadding(),
                iconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                windowInsets = WindowInsets.navigationBars,
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        contentWindowInsets = WindowInsets.systemBars,
        modifier = modifier
    ) { scPadding ->
        TaskFields(
            state = state,
            scaffoldPadding = scPadding,
            selectedLabels = selectedLabels,
            onAddTaskEvents = onAddTaskEvents,
            onClickOnLabel = onLabelPickerDialog,
            onClickOnColor = {
                coroutineScope.launch {
                    if (!colorSheetState.isVisible)
                        colorSheetState.show()
                }
            },
            onClickOnReminder = { showReminderDialog = true },
            modifier = modifier
                .fillMaxSize()
                .imeNestedScroll()
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
        onLabelPickerDialog = {},
        onAddTaskEvents = {},
        selectedLabels = emptyList(),
    )
}