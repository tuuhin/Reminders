package com.eva.reminders.presentation.feature_home.composables

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.eva.reminders.domain.enums.TaskColorEnum
import com.eva.reminders.domain.models.TaskLabelModel
import com.eva.reminders.presentation.feature_home.utils.SearchType

@Composable
fun SearchResultsNoResults(
    labels: List<TaskLabelModel>,
    colors: List<TaskColorEnum>,
    onSearchType: (SearchType) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        contentPadding = PaddingValues(8.dp),
        modifier = modifier
    ) {
        if (labels.isNotEmpty()) {
            item {
                Text(
                    text = "Labels",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }
            item {
                SearchOptionLabels(
                    labels = labels,
                    onLabelClick = { onSearchType(SearchType.LabelSearch(it)) },
                    modifier = Modifier.fillParentMaxWidth()
                )
            }
        }
        if (labels.isNotEmpty()) {
            item {
                Text(
                    text = "Colors",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }
            item {
                SearchOptionColor(
                    colors = colors,
                    onColorSelect = { onSearchType(SearchType.ColorSearch(it)) },
                    modifier = Modifier.fillParentMaxWidth()
                )
            }
        }
    }
}