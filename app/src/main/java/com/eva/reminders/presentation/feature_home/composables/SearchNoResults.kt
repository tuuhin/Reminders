package com.eva.reminders.presentation.feature_home.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.eva.reminders.R
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
    if (labels.isEmpty() && colors.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.menu),
                contentDescription = "No options found",
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.surfaceTint),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(id = R.string.no_search_results),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.secondary,
                textAlign = TextAlign.Center
            )
        }
    } else
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
            if (colors.isNotEmpty()) {
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