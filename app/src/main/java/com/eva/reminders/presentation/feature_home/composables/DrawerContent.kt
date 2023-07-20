package com.eva.reminders.presentation.feature_home.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
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
        modifier = modifier
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
                contentDescription = "Maybe logo",
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
                        imageVector = if (tab == currentTab)
                            currentTab.filledIcon
                        else currentTab.icon,
                        contentDescription = currentTab.text
                    )
                },
                label = { Text(text = currentTab.text) },
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

@Preview(showBackground = true)
@Composable
fun DrawerContentPreview() {
    DrawerContent(
        labels = PreviewTaskModels.taskLabelModelList,
        tab = HomeTabs.AllReminders,
        onTabChange = {},
        onEdit = { }
    )
}