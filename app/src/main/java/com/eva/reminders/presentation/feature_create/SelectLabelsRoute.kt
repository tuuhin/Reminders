package com.eva.reminders.presentation.feature_create

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.eva.reminders.R
import com.eva.reminders.presentation.feature_create.composables.TaskLabelPicker
import com.eva.reminders.presentation.feature_create.utils.SelectLabelState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectLabelsRoute(
    query: String,
    onQueryChanged: (String) -> Unit,
    labels: List<SelectLabelState>,
    onDismissRequest: () -> Unit,
    onSelect: (SelectLabelState) -> Unit,
    onCreateNew: () -> Unit,
    modifier: Modifier = Modifier,
) {
    SearchBar(
        query = query,
        onQueryChange = onQueryChanged,
        active = true,
        onActiveChange = {},
        onSearch = {},
        placeholder = { Text(text = stringResource(id = R.string.search_placeholder)) },
        leadingIcon = {
            IconButton(onClick = onDismissRequest) {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = stringResource(id = R.string.close_icon_desc)
                )
            }
        },
        trailingIcon = {
            IconButton(onClick = onDismissRequest) {
                Icon(
                    imageVector = Icons.Outlined.Check,
                    contentDescription = stringResource(id = R.string.icon_check_desc)
                )
            }
        },
        modifier = modifier,
        colors = SearchBarDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp),
            dividerColor = MaterialTheme.colorScheme.outline,
            inputFieldColors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp)
            )
        )
    ) {
        TaskLabelPicker(
            labels = labels,
            showPlaceHolder = labels.isEmpty() && query.isEmpty(),
            showCreateLabelButton = labels.isEmpty() && query.isNotEmpty(),
            onCreateNew = onCreateNew,
            onSelect = onSelect
        )
    }
}
