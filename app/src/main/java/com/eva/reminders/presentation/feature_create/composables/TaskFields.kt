package com.eva.reminders.presentation.feature_create.composables

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.eva.reminders.R
import com.eva.reminders.domain.enums.TaskColorEnum
import com.eva.reminders.domain.models.TaskLabelModel
import com.eva.reminders.presentation.feature_create.utils.AddTaskEvents
import com.eva.reminders.presentation.feature_create.utils.AddTaskState
import com.eva.reminders.presentation.utils.noColor

@Composable
fun TaskFields(
    state: AddTaskState,
    scaffoldPadding: PaddingValues,
    selectedLabels: List<TaskLabelModel>,
    modifier: Modifier = Modifier,
    onAddTaskEvents: (AddTaskEvents) -> Unit,
    onClickOnLabel: () -> Unit,
    onClickOnColor: () -> Unit,
    onClickOnReminder: () -> Unit,
) {

    val titleFocus = remember { FocusRequester() }
    val contentFocus = remember { FocusRequester() }


    LaunchedEffect(Unit) {
        if (state.isCreate) titleFocus.requestFocus()
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = scaffoldPadding
    ) {
        item(key = 0) {
            TextField(
                value = state.title,
                onValueChange = { title -> onAddTaskEvents(AddTaskEvents.OnTitleChange(title)) },
                textStyle = MaterialTheme.typography.headlineSmall,
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.title_text),
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.secondary
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
        item(key = 1) {
            TextField(
                value = state.content,
                onValueChange = { onAddTaskEvents(AddTaskEvents.OnContentChange(it)) },
                textStyle = MaterialTheme.typography.titleMedium,
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.note_text),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                },
                singleLine = false,
                maxLines = Int.MAX_VALUE,
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

        item(key = 2) {
            PickedLabels(
                selectedLabels = selectedLabels,
                onLabelClick = onClickOnLabel,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
        }
        item(key = 3) {
            SelectedReminderTimeChip(
                showReminder = state.isReminderPresent,
                state = state.reminderState,
                onClick = onClickOnReminder,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
        }
        item(key = 4) {
            SelectedTaskColorChip(
                taskColor = if (state.color != TaskColorEnum.TRANSPARENT) state.color else null,
                onClick = onClickOnColor,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
        }
    }
}