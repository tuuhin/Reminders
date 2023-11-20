package com.eva.reminders.presentation.feature_labels

import android.content.res.Configuration
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.eva.reminders.R
import com.eva.reminders.presentation.feature_home.utils.PreviewTaskModels
import com.eva.reminders.presentation.feature_labels.composabels.CreateNewLabel
import com.eva.reminders.presentation.feature_labels.composabels.EditableLabels
import com.eva.reminders.presentation.feature_labels.composabels.NoLabelsFound
import com.eva.reminders.presentation.feature_labels.composabels.SortLabelsDialog
import com.eva.reminders.presentation.feature_labels.utils.CreateLabelEvents
import com.eva.reminders.presentation.feature_labels.utils.CreateLabelState
import com.eva.reminders.presentation.feature_labels.utils.EditLabelEvents
import com.eva.reminders.presentation.feature_labels.utils.EditLabelsActions
import com.eva.reminders.presentation.feature_labels.utils.FeatureLabelsTestTags
import com.eva.reminders.presentation.feature_labels.utils.LabelEditableState
import com.eva.reminders.presentation.feature_labels.utils.LabelSortOrder
import com.eva.reminders.presentation.feature_labels.utils.toEditState
import com.eva.reminders.presentation.navigation.NavigationTestTags
import com.eva.reminders.presentation.utils.LocalSnackBarHostProvider
import com.eva.reminders.ui.theme.RemindersTheme

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class,
)
@Composable
fun EditLabelRoute(
    labelsSortOrder: LabelSortOrder,
    createLabelState: CreateLabelState,
    onCreateLabelEvent: (CreateLabelEvents) -> Unit,
    editLabelState: List<LabelEditableState>,
    onEditLabelEvent: (EditLabelEvents) -> Unit,
    onEditActions: (EditLabelsActions) -> Unit,
    modifier: Modifier = Modifier,
    navigation: (@Composable () -> Unit)? = null,
    onSortOrderChange: (LabelSortOrder) -> Unit,
    snackBarHostState: SnackbarHostState = LocalSnackBarHostProvider.current
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    var showSortDialog by rememberSaveable {
        mutableStateOf(false)
    }

    // Though it's not required but still may avoid some errors
    val taskLabels by remember(editLabelState) {
        derivedStateOf { editLabelState.filter { it.labelId != null } }
    }

    if (showSortDialog) {
        SortLabelsDialog(
            selected = labelsSortOrder,
            onDismiss = { showSortDialog = false },
            onOrderChange = onSortOrderChange,
            modifier = Modifier.testTag(FeatureLabelsTestTags.SORT_OPTIONS_DIALOG)
        )
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = MaterialTheme.colorScheme.inverseSurface,
                    contentColor = MaterialTheme.colorScheme.inverseOnSurface,
                )
            }
        },
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.edit_labels_route_title)) },
                navigationIcon = navigation ?: {},
                scrollBehavior = scrollBehavior, actions = {
                    IconButton(
                        onClick = { showSortDialog = true },
                        modifier = Modifier.testTag(FeatureLabelsTestTags.TOGGLE_SORT_OPTIONS_DIALOG)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Sort,
                            contentDescription = stringResource(id = R.string.sort_icon_desc)
                        )
                    }
                }
            )
        },
        contentWindowInsets = WindowInsets.systemBars,
        modifier = Modifier
            .testTag(NavigationTestTags.EDIT_LABEL_ROUTE)
            .nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Divider()
            CreateNewLabel(
                state = createLabelState,
                onDone = { onCreateLabelEvent(CreateLabelEvents.OnSubmit) },
                onValueChange = { label -> onCreateLabelEvent(CreateLabelEvents.OnValueChange(label)) }
            )
            Divider()
            Crossfade(
                targetState = editLabelState.isEmpty(),
                label = "Show saved labels cross-fade animation",
            ) { isEmpty ->
                when {
                    isEmpty -> NoLabelsFound(
                        modifier = Modifier
                            .fillMaxSize()
                            .testTag(FeatureLabelsTestTags.NO_LABELS_ADDED_TAG)
                    )

                    else -> LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .imePadding()
                            .testTag(FeatureLabelsTestTags.LOADED_LABELS_LAZY_COL),
                        contentPadding = PaddingValues(
                            horizontal = dimensionResource(id = R.dimen.scaffold_padding_min)
                        )
                    ) {
                        itemsIndexed(
                            taskLabels,
                            key = { _, item -> item.labelId ?: -1 }) { _, item ->
                            item.labelId?.let { labelId ->
                                Column(
                                    modifier = Modifier
                                        .animateContentSize()
                                        .animateItemPlacement()
                                ) {
                                    EditableLabels(
                                        labelId = labelId,
                                        state = item,
                                        onAction = onEditActions,
                                        onEvents = onEditLabelEvent
                                    )
                                    Divider(color = MaterialTheme.colorScheme.outlineVariant)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun EditLabelRoutePreview() = RemindersTheme {
    EditLabelRoute(
        labelsSortOrder = LabelSortOrder.REGULAR,
        createLabelState = CreateLabelState(),
        onCreateLabelEvent = {},
        editLabelState = PreviewTaskModels.taskLabelModelList.map { it.toEditState() },
        onEditLabelEvent = {},
        onEditActions = {},
        onSortOrderChange = {}
    )
}

