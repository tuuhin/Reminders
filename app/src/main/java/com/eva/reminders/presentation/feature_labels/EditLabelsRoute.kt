package com.eva.reminders.presentation.feature_labels

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.eva.reminders.R
import com.eva.reminders.presentation.feature_labels.composabels.CreateNewLabel
import com.eva.reminders.presentation.feature_labels.composabels.EditableLabels
import com.eva.reminders.presentation.feature_labels.utils.CreateLabelEvents
import com.eva.reminders.presentation.feature_labels.utils.CreateLabelState
import com.eva.reminders.presentation.feature_labels.utils.EditLabelEvents
import com.eva.reminders.presentation.feature_labels.utils.EditLabelState
import com.eva.reminders.presentation.feature_labels.utils.EditLabelsActions
import com.eva.reminders.presentation.utils.LocalSnackBarHostProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditLabelRoute(
    createLabelState: CreateLabelState,
    onCreateLabelEvent: (CreateLabelEvents) -> Unit,
    editLabelState: List<EditLabelState>,
    onEditLabelEvent: (EditLabelEvents) -> Unit,
    onEditActions: (EditLabelsActions) -> Unit,
    modifier: Modifier = Modifier,
    navigation: (@Composable () -> Unit)? = null,
    snackBarHostState: SnackbarHostState = LocalSnackBarHostProvider.current
) {
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(text = "Edit Labels") },
                navigationIcon = navigation ?: {}
            )
        },
        contentWindowInsets = WindowInsets.systemBars
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Divider()
            CreateNewLabel(
                state = createLabelState,
                onAdd = { onCreateLabelEvent(CreateLabelEvents.ToggleEnabled) },
                onCancel = { onCreateLabelEvent(CreateLabelEvents.ToggleEnabled) },
                onDone = { onCreateLabelEvent(CreateLabelEvents.OnSubmit) },
                onValueChange = { onCreateLabelEvent(CreateLabelEvents.OnValueChange(it)) }
            )
            Divider()

            AnimatedVisibility(
                visible = editLabelState.isNotEmpty(),
                enter = slideInVertically() + fadeIn(),
                exit = slideOutVertically() + fadeOut()
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    itemsIndexed(
                        editLabelState,
                        key = { _, item -> item.labelId ?: -1 }
                    ) { _, state ->
                        EditableLabels(
                            state = state,
                            onEdit = { onEditLabelEvent(EditLabelEvents.ToggleEnabled(state)) },
                            onDelete = { onEditActions(EditLabelsActions.OnDelete(state)) },
                            onValueChange = {
                                onEditLabelEvent(EditLabelEvents.OnValueChange(it, state))
                            },
                            onDone = { onEditActions(EditLabelsActions.OnUpdate(state)) },
                            onCancel = { onEditLabelEvent(EditLabelEvents.ToggleEnabled(state)) }
                        )
                    }
                }
            }

            when {
                editLabelState.isEmpty() -> Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_labels),
                        contentDescription = "No Labels are present",
                        colorFilter = ColorFilter.tint(
                            color = MaterialTheme.colorScheme.surfaceTint
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "No Labels are added",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}

