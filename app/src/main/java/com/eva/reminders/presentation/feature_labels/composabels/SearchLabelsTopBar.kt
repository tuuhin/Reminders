package com.eva.reminders.presentation.feature_labels.composabels


import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.eva.reminders.presentation.utils.noColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchLabelsTopBar(
    query: String,
    onSearch: (String) -> Unit,
    navigationIcon: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            TextField(
                value = query,
                onValueChange = onSearch,
                colors = TextFieldDefaults.noColor(),
                textStyle = MaterialTheme.typography.bodyMedium,
                placeholder = {
                    Text(
                        text = "Enter label name",
                        color = MaterialTheme.colorScheme.outline,
                    )
                },
                maxLines = 1,
            )
        },
        navigationIcon = navigationIcon,
        modifier = modifier
    )
}