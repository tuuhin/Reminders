package com.eva.reminders.presentation.feature_labels.composabels

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.eva.reminders.R
import com.eva.reminders.presentation.feature_labels.utils.LabelSortOrder
import com.eva.reminders.presentation.feature_labels.utils.LabelsSortOrderPreviewParams
import com.eva.reminders.presentation.feature_labels.utils.SortLabelEvents
import com.eva.reminders.ui.theme.RemindersTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortLabelsDialog(
    selected: LabelSortOrder,
    onOrderChange: (LabelSortOrder) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = modifier,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Card(
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp),
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        ) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.sort_labels_heading),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                LabelSortOrder.entries.forEach { types ->

                    val interaction = remember { MutableInteractionSource() }
                    val rippleIndication = rememberRipple()

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .fillMaxWidth()
                            .clickable(
                                interactionSource = interaction,
                                indication = rippleIndication,
                                onClick = { onOrderChange(types) },
                                role = Role.Button
                            ),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        RadioButton(
                            selected = types == selected,
                            onClick = { onOrderChange(types) },
                            modifier = Modifier.weight(.1f)
                        )
                        Text(
                            text = stringResource(id = types.textRes),
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.weight(.9f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SortLabelsDialog(
    selected: LabelSortOrder,
    onSortEvents: (SortLabelEvents) -> Unit,
) {
    SortLabelsDialog(
        selected = selected,
        onOrderChange = { newSortOrder ->
            onSortEvents(SortLabelEvents.SelectSortOrder(newSortOrder))
            onSortEvents(SortLabelEvents.ToggleDialog)
        },
        onDismiss = { onSortEvents(SortLabelEvents.ToggleDialog) },
    )
}


@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun SortLabelsDialogPreview(
    @PreviewParameter(LabelsSortOrderPreviewParams::class)
    order: LabelSortOrder,
) = RemindersTheme {
    SortLabelsDialog(
        selected = order,
        onOrderChange = {},
        onDismiss = {},
    )
}