package com.eva.reminders.presentation.feature_labels

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.eva.reminders.R
import com.eva.reminders.presentation.feature_labels.composabels.CreateNewLabel
import com.eva.reminders.presentation.feature_labels.composabels.EditableLabels
import com.eva.reminders.presentation.feature_labels.utils.CreateLabelEvents
import com.eva.reminders.presentation.feature_labels.utils.CreateLabelState
import com.eva.reminders.presentation.feature_labels.utils.EditLabelEvents
import com.eva.reminders.presentation.feature_labels.utils.EditLabelState
import com.eva.reminders.presentation.utils.UIEvents
import kotlinx.coroutines.flow.Flow


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditLabelRoute(
    navController: NavController,
    createLabelState: CreateLabelState,
    onCreateLabelEvent: (CreateLabelEvents) -> Unit,
    editLabelState: List<EditLabelState>,
    onEditLabelEvent: (EditLabelEvents) -> Unit,
    uiEvents: Flow<UIEvents>,
    modifier: Modifier = Modifier,
) {
    val snackBar = remember { SnackbarHostState() }

    LaunchedEffect(true) {
        uiEvents.collect { event ->
            when (event) {
                is UIEvents.ShowSnackBar -> snackBar.showSnackbar(event.message)
                else -> {}
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBar) },
        topBar = {
            TopAppBar(
                title = { Text(text = "Edit Labels") },
                navigationIcon = {
                    if (navController.previousBackStackEntry != null)
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back Button"
                            )
                        }
                }
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
                enter = slideInVertically(),
                exit = slideOutVertically()
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(editLabelState.size) { idx ->
                        val item = editLabelState[idx]
                        EditableLabels(
                            state = item,
                            onEdit = { onEditLabelEvent(EditLabelEvents.ToggleEnabled(item)) },
                            onDelete = { onEditLabelEvent(EditLabelEvents.OnDelete(item)) },
                            onValueChange = {
                                onEditLabelEvent(EditLabelEvents.OnValueChange(it, item))
                            },
                            onDone = { onEditLabelEvent(EditLabelEvents.OnUpdate(item)) },
                            onCancel = { onEditLabelEvent(EditLabelEvents.ToggleEnabled(item)) }
                        )
                    }
                }
            }
            if (editLabelState.isEmpty())
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.label),
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

