package com.eva.reminders.presentation.feature_labels.composabels

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.eva.reminders.R
import com.eva.reminders.presentation.feature_labels.utils.FeatureLabelsTestTags
import com.eva.reminders.presentation.utils.noColor

@Composable
fun CreateNewLabelField(
    label: String,
    onValueChange: (String) -> Unit,
    onDone: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
    isError: String? = null,
    textStyle: TextStyle = MaterialTheme.typography.titleSmall,
    errorStyle: TextStyle = MaterialTheme.typography.labelSmall,
    errorColor: Color = MaterialTheme.colorScheme.error,
    focusRequest: FocusRequester = remember { FocusRequester() },
) {

    LaunchedEffect(Unit) {
        focusRequest.requestFocus()
    }

    Column {
        Row(
            modifier = modifier
                .padding(horizontal = 12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onCancel,
                modifier = Modifier
                    .weight(.1f)
                    .testTag(FeatureLabelsTestTags.CREATE_NEW_LABEL_ACTION_CANCEL)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = stringResource(id = R.string.close_icon_desc)
                )
            }
            TextField(
                value = label,
                onValueChange = onValueChange,
                colors = TextFieldDefaults.noColor(),
                keyboardActions = KeyboardActions(onDone = { onDone() }),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                singleLine = true,
                maxLines = 1,
                textStyle = textStyle,
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.create_label_placeholder),
                        style = textStyle,
                    )
                },
                modifier = Modifier
                    .testTag(FeatureLabelsTestTags.CREATE_NEW_LABEL_TEXT_FIELD)
                    .weight(.7f)
                    .focusRequester(focusRequest),
            )
            IconButton(
                onClick = onDone,
                modifier = Modifier
                    .weight(.1f)
                    .testTag(FeatureLabelsTestTags.CREATE_NEW_LABEL_ACTION_CREATE)
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = stringResource(id = R.string.icon_check_desc)
                )
            }
        }
        AnimatedVisibility(
            visible = isError != null,
            enter = slideInVertically(),
            exit = slideOutVertically()
        ) {
            isError?.let { error ->
                Text(
                    text = error,
                    style = errorStyle,
                    color = errorColor,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}