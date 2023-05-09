package com.eva.reminders.presentation.feature_home.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.eva.reminders.R
import com.eva.reminders.domain.models.TaskLabelModel
import com.eva.reminders.presentation.utils.HomeTabs
import com.eva.reminders.presentation.utils.NavRoutes

@Composable
fun DrawerContent(
    navController: NavController,
    labels: List<TaskLabelModel>,
    tab: HomeTabs,
    onTabChange: (HomeTabs) -> Unit,
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
                painter = painterResource(id = R.drawable.notification_small_logo),
                contentDescription = "Maybe logo"
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.titleLarge
            )
        }
        allTabs.forEach { currentTab ->
            NavigationDrawerItem(
                icon = {
                    Icon(
                        imageVector = if (tab == currentTab) currentTab.filledIcon else currentTab.icon,
                        contentDescription = currentTab.text
                    )
                },
                label = { Text(text = currentTab.text) },
                selected = tab == currentTab,
                onClick = {onTabChange(currentTab)}
            )

        }
        DrawerLabelItems(
            onEdit = { navController.navigate(NavRoutes.EditLabels.route) },
            labels = labels,
            modifier = Modifier.weight(1f)
        )
    }
}
