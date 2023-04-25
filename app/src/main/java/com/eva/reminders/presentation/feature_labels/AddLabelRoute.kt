package com.eva.reminders.presentation.feature_labels

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.eva.reminders.presentation.feature_labels.composabels.CheckLabelItem
import com.eva.reminders.presentation.feature_labels.composabels.SearchLabelsTopBar
import com.eva.reminders.presentation.feature_labels.utils.SelectLabelState
import com.eva.reminders.presentation.utils.UIEvents
import kotlinx.coroutines.flow.Flow

@Composable
fun AddLabelsRoute(
    navController: NavController,
    labels: List<SelectLabelState>,
    query: String,
    onSearch: (String) -> Unit,
    onSelect: (SelectLabelState) -> Unit,
    onCreateNew: () -> Unit,
    uiEvents: Flow<UIEvents>,
    modifier: Modifier = Modifier,
) {

    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        uiEvents.collect { event ->
            when (event) {
                is UIEvents.ShowSnackBar -> snackBarHostState
                    .showSnackbar(event.message)
            }
        }
    }

    Scaffold(
        topBar = {
            SearchLabelsTopBar(
                query = query,
                onSearch = onSearch,
                navigationIcon = {
                    if (navController.currentBackStackEntry != null)
                        IconButton(
                            onClick = {
                                navController.navigateUp()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.ArrowBack,
                                contentDescription = "Back button"
                            )
                        }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackBarHostState) }
    ) { padding ->
        Column(
            modifier = modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 10.dp)
        ) {
            LazyColumn {
                itemsIndexed(labels) { _, item ->
                    CheckLabelItem(
                        item = item,
                        onSelect = { onSelect(item) }
                    )
                }
            }
            if (query.isNotEmpty())
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable(onClick = onCreateNew, role = Role.Button),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Add,
                        contentDescription = null,
                        modifier = Modifier.weight(.1f),
                        tint = MaterialTheme.colorScheme.surfaceTint
                    )
                    Spacer(modifier = Modifier.weight(.1f))
                    Text(text = "Create New Label", modifier = Modifier.weight(.9f))
                }
        }
    }
}

