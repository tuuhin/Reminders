package com.eva.reminders.presentation.feature_create.composables

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eva.reminders.R
import com.eva.reminders.presentation.feature_create.utils.SelectLabelState
import com.eva.reminders.presentation.feature_create.utils.toSelectLabelState
import com.eva.reminders.presentation.feature_home.utils.PreviewTaskModels
import com.eva.reminders.ui.theme.RemindersTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TaskLabelPicker(
    labels: List<SelectLabelState>,
    showPlaceHolder: Boolean,
    showCreateLabelButton: Boolean,
    onCreateNew: () -> Unit,
    onSelect: (SelectLabelState) -> Unit,
    modifier: Modifier = Modifier
) {


    Crossfade(
        targetState = showPlaceHolder,
        label = "Is there are no labels",
        modifier = modifier, animationSpec = tween(easing = FastOutSlowInEasing)
    ) { placeholder ->
        when {
            placeholder -> Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(id = R.string.no_search_results),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            else -> LazyColumn(
                verticalArrangement = Arrangement.spacedBy(2.dp),
                contentPadding = PaddingValues(8.dp)
            ) {
                val enterTransition = fadeIn() + expandVertically()
                val exitTransition = fadeOut() + shrinkVertically()

                item(key = -1) {
                    AnimatedVisibility(
                        visible = showCreateLabelButton,
                        enter = enterTransition,
                        exit = exitTransition,
                    ) {
                        CreateNewLabelCard(
                            onClick = onCreateNew,
                            modifier = Modifier.animateItemPlacement()
                        )
                    }
                }
                itemsIndexed(
                    items = labels,
                    key = { _, item -> item.model.id }
                ) { _, item ->
                    SelectTaskLabelOption(
                        item = item,
                        onSelect = { onSelect(item) },
                        modifier = Modifier.animateItemPlacement()
                    )
                }
            }
        }
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun TaskLabelPickerPreview() = RemindersTheme {
    Surface(color = MaterialTheme.colorScheme.surface) {
        TaskLabelPicker(
            labels = PreviewTaskModels.taskLabelModelList.map { it.toSelectLabelState() },
            showPlaceHolder = false,
            showCreateLabelButton = true,
            onCreateNew = {},
            onSelect = {}
        )
    }
}