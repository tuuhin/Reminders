package com.eva.reminders.presentation.feature_labels.composabels

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.eva.reminders.presentation.feature_labels.utils.EditLabelEvents
import com.eva.reminders.presentation.feature_labels.utils.EditLabelsActions
import com.eva.reminders.presentation.feature_labels.utils.EditableLabelsPreviewParams
import com.eva.reminders.presentation.feature_labels.utils.LabelEditableState
import com.eva.reminders.presentation.feature_labels.utils.slideContentHorizontally
import com.eva.reminders.ui.theme.RemindersTheme

@Composable
fun EditableLabels(
    state: LabelEditableState,
    onEdit: () -> Unit,
    onDone: () -> Unit,
    onDelete: () -> Unit,
    onCancel: () -> Unit,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedContent(
        targetState = state.isEdit,
        label = "Animation for label",
        transitionSpec = { slideContentHorizontally() }
    ) { isEdit ->

        when {
            isEdit -> TaskLabelsEditable(
                previous = state.previousLabel,
                edited = state.updatedLabel,
                onValueChange = onValueChange,
                onDelete = onDelete,
                onCancel = onCancel,
                onDone = onDone, modifier = modifier
            )

            else -> TaskLabelsReadOnly(
                label = state.previousLabel,
                onEdit = onEdit,
                modifier = modifier
            )
        }
    }
}

@Composable
fun EditableLabels(
    state: LabelEditableState,
    onAction: (EditLabelsActions) -> Unit,
    onEvents: (EditLabelEvents) -> Unit,
    modifier: Modifier = Modifier
) {
    EditableLabels(
        state = state,
        onEdit = {
            onEvents(EditLabelEvents.ToggleEnabled(state.labelId))
        },
        onDelete = { onAction(EditLabelsActions.OnDelete(state)) },
        onValueChange = { updated ->
            onEvents(EditLabelEvents.OnLabelValueUpdate(updated, state.labelId))
        },
        onDone = { onAction(EditLabelsActions.OnUpdate(state)) },
        onCancel = { onEvents(EditLabelEvents.ToggleEnabled(state.labelId)) },
        modifier = modifier
    )
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun EditableLabelPreview(
    @PreviewParameter(EditableLabelsPreviewParams::class)
    state: LabelEditableState
) = RemindersTheme {
    Surface(color = MaterialTheme.colorScheme.background) {
        EditableLabels(
            state = state,
            onEdit = { },
            onDone = { },
            onDelete = { },
            onCancel = { },
            onValueChange = {}
        )
    }
}