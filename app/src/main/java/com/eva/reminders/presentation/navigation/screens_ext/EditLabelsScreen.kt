package com.eva.reminders.presentation.navigation.screens_ext

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.eva.reminders.presentation.feature_labels.EditLabelRoute
import com.eva.reminders.presentation.feature_labels.LabelsViewModel
import com.eva.reminders.presentation.navigation.NavRoutes
import com.eva.reminders.presentation.utils.LocalSnackBarHostProvider
import com.eva.reminders.presentation.utils.UIEvents

fun NavGraphBuilder.editLabelsScreen(navHost: NavHostController) {
    composable(NavRoutes.EditLabels.route) {

        val lifecycleOwner = LocalLifecycleOwner.current
        val snackBarHostState = LocalSnackBarHostProvider.current

        val viewModel = hiltViewModel<LabelsViewModel>()
        val createState by viewModel.newLabelState.collectAsStateWithLifecycle()
        val editState by viewModel.editLabelStates.collectAsStateWithLifecycle()
        val sortDialogState by viewModel.showSortDialog.collectAsStateWithLifecycle()

        LaunchedEffect(viewModel.uiEvents, lifecycleOwner.lifecycle) {
            lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiEvents.collect { event ->
                    when (event) {
                        is UIEvents.ShowSnackBar -> snackBarHostState.showSnackbar(event.message)
                        UIEvents.NavigateBack -> navHost.navigateUp()
                    }
                }
            }
        }

        EditLabelRoute(
            sortDialogState = sortDialogState,
            createLabelState = createState,
            editLabelState = editState,
            onCreateLabelEvent = viewModel::onCreateLabelEvent,
            onEditLabelEvent = viewModel::onUpdateLabelEvent,
            onEditActions = viewModel::onLabelAction,
            onSortEvents = viewModel::onSortEvents,
            navigation = {
                if (navHost.previousBackStackEntry != null)
                    IconButton(onClick = navHost::navigateUp) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back Button"
                        )
                    }
            },
        )
    }
}