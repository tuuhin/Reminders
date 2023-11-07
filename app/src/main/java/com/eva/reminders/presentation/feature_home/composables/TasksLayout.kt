package com.eva.reminders.presentation.feature_home.composables

import android.content.res.Configuration
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.IntOffset
import com.eva.reminders.R
import com.eva.reminders.domain.models.ArrangementStyle
import com.eva.reminders.domain.models.TaskModel
import com.eva.reminders.presentation.feature_home.utils.ArrangementStyleParameter
import com.eva.reminders.presentation.feature_home.utils.PreviewTaskModels
import com.eva.reminders.ui.theme.RemindersTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TasksLayout(
    modifier: Modifier = Modifier,
    tasks: List<TaskModel>,
    style: ArrangementStyle,
    onTaskSelect: (TaskModel) -> Unit
) {
    val columns = remember(style) {
        when (style) {
            ArrangementStyle.GRID_STYLE -> StaggeredGridCells.Fixed(2)
            ArrangementStyle.BLOCK_STYLE -> StaggeredGridCells.Fixed(1)
        }
    }

    val showSubHeadings by remember(tasks) {
        derivedStateOf { tasks.any { it.pinned } }
    }

    val pinnedTasks by remember(tasks) {
        derivedStateOf { tasks.filter { it.pinned } }
    }

    val unPinnedTask by remember(tasks) {
        derivedStateOf { tasks.filter { !it.pinned } }
    }

    val springSpec: SpringSpec<IntOffset> = spring(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessMediumLow
    )


    LazyVerticalStaggeredGrid(
        columns = columns,
        contentPadding = PaddingValues(all = dimensionResource(id = R.dimen.scaffold_padding)),
        verticalItemSpacing = dimensionResource(id = R.dimen.reminder_card_spacing),
        horizontalArrangement = Arrangement
            .spacedBy(space = dimensionResource(id = R.dimen.reminder_card_spacing)),
        modifier = modifier.imePadding()
    ) {
        if (showSubHeadings) {
            item(key = R.string.pinned_text_subheading, span = StaggeredGridItemSpan.FullLine) {
                Text(
                    text = stringResource(id = R.string.pinned_text_subheading),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .padding(vertical = dimensionResource(id = R.dimen.reminders_sub_heading_v_padding))
                )
            }
            itemsIndexed(pinnedTasks, key = { _, item -> item.id }) { _, item ->
                ReminderCard(
                    taskModel = item,
                    onTap = { onTaskSelect(item) },
                    modifier = Modifier
                        .animateContentSize()
                        .animateItemPlacement(springSpec)
                        .fillMaxWidth()
                )
            }
            item(key = R.string.un_pinned_text_subheading, span = StaggeredGridItemSpan.FullLine) {
                Text(
                    text = stringResource(id = R.string.un_pinned_text_subheading),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .padding(vertical = dimensionResource(id = R.dimen.reminders_sub_heading_v_padding))
                        .animateItemPlacement(springSpec),
                )
            }
            itemsIndexed(unPinnedTask, key = { _, item -> item.id }) { _, item ->
                ReminderCard(
                    taskModel = item,
                    onTap = { onTaskSelect(item) },
                    modifier = Modifier
                        .animateContentSize()
                        .animateItemPlacement(springSpec)
                        .fillMaxWidth()
                )
            }
        } else {
            itemsIndexed(tasks, key = { _, item -> item.id }) { _, item ->
                ReminderCard(
                    taskModel = item,
                    onTap = { onTaskSelect(item) },
                    modifier = Modifier
                        .animateContentSize()
                        .animateItemPlacement(springSpec)
                        .fillMaxWidth()
                )
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
fun TaskLayoutPreview(
    @PreviewParameter(ArrangementStyleParameter::class)
    style: ArrangementStyle
) = RemindersTheme {
    Surface(color = MaterialTheme.colorScheme.background) {
        TasksLayout(
            tasks = PreviewTaskModels.taskModelsList,
            style = style,
            onTaskSelect = {}
        )
    }
}