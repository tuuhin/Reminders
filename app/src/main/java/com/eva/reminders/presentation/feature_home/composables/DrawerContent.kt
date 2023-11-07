package com.eva.reminders.presentation.feature_home.composables

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eva.reminders.R
import com.eva.reminders.domain.models.TaskLabelModel
import com.eva.reminders.presentation.feature_home.utils.PreviewTaskModels
import com.eva.reminders.presentation.utils.HomeTabs
import com.eva.reminders.ui.theme.RemindersTheme

@Composable
fun DrawerContent(
    labels: List<TaskLabelModel>,
    tab: HomeTabs,
    onTabChange: (HomeTabs) -> Unit,
    onEdit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val allTabs = remember {
        listOf(
            HomeTabs.AllReminders,
            HomeTabs.NonScheduled,
            HomeTabs.Scheduled,
            HomeTabs.Archived
        )
    }

    Column(
        modifier = modifier.windowInsetsPadding(WindowInsets.statusBars)
    ) {
        Row(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_reminder_logo),
                contentDescription = stringResource(id = R.string.app_name),
                modifier = Modifier.size(28.dp),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimaryContainer)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
        allTabs.forEach { currentTab ->
            NavigationDrawerItem(
                icon = {
                    Icon(
                        imageVector = if (tab == currentTab) currentTab.filledIcon else currentTab.icon,
                        contentDescription = stringResource(currentTab.textRes)
                    )
                },
                label = { Text(text = stringResource(currentTab.textRes)) },
                selected = tab == currentTab,
                onClick = { onTabChange(currentTab) },
                colors = NavigationDrawerItemDefaults.colors(
                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    selectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    unselectedTextColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    unselectedIconColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
            )

        }
        Divider(color = MaterialTheme.colorScheme.outlineVariant)
        DrawerLabelItems(
            onEdit = onEdit,
            labels = labels,
            modifier = Modifier.weight(1f)
        )
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun DrawerContentPreview() = RemindersTheme {
    Surface(color = MaterialTheme.colorScheme.surfaceVariant) {
        DrawerContent(
            labels = PreviewTaskModels.taskLabelModelList,
            tab = HomeTabs.AllReminders,
            onTabChange = {},
            onEdit = { }
        )
    }
}