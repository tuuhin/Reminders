package com.eva.reminders.presentation.feature_labels.composabels

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.eva.reminders.presentation.feature_labels.utils.CreateLabelState
import com.eva.reminders.presentation.feature_labels.utils.CreateNewLabelPreviewParams
import com.eva.reminders.presentation.feature_labels.utils.FeatureLabelsTestTags
import com.eva.reminders.presentation.feature_labels.utils.slideContentVertically
import com.eva.reminders.ui.theme.RemindersTheme

@Composable
fun CreateNewLabel(
    state: CreateLabelState,
    modifier: Modifier = Modifier,
    onDone: () -> Unit,
    onValueChange: (String) -> Unit
) {

    var showCreateField by rememberSaveable { mutableStateOf(false) }

    AnimatedContent(
        targetState = showCreateField,
        label = "Transition for create labels",
        transitionSpec = { slideContentVertically() }
    ) { isFieldVisible ->
        when {
            isFieldVisible -> CreateNewLabelField(
                label = state.label,
                onValueChange = onValueChange,
                onDone = {
                    showCreateField = false
                    onDone()
                },
                onCancel = { showCreateField = false },
                modifier = modifier,
                textStyle = MaterialTheme.typography.titleMedium
            )

            else -> CreateNewLabelPlaceHolder(
                onAdd = { showCreateField = true },
                labelStyle = MaterialTheme.typography.titleMedium,
                modifier = modifier.testTag(FeatureLabelsTestTags.CREATE_LABEL_PLACEHOLDER),
            )
        }

    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun CreateNewLabelPreview(
    @PreviewParameter(CreateNewLabelPreviewParams::class) state: CreateLabelState
) = RemindersTheme {
    Surface(color = MaterialTheme.colorScheme.surface) {
        CreateNewLabel(
            state = state,
            onDone = { },
            onValueChange = {}
        )
    }
}