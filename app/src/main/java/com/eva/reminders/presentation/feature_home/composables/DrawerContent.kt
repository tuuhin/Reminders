package com.eva.reminders.presentation.feature_home.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Archive
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.eva.reminders.R
import com.eva.reminders.domain.models.TaskLabelModel
import com.eva.reminders.presentation.utils.NavRoutes

@Composable
fun DrawerContent(
    navController: NavController,
    labels: List<TaskLabelModel>,
    modifier: Modifier = Modifier,
) {
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
                painter = painterResource(id = R.drawable.sticky_notes),
                contentDescription = "Maybe logo"
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.titleLarge
            )
        }
        NavigationDrawerItem(
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Lightbulb,
                    contentDescription = "Notes"
                )
            },
            label = { Text(text = "Notes") },
            selected = true,
            onClick = { /*TODO*/ }
        )
        NavigationDrawerItem(
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = "Reminders"
                )
            },
            label = { Text(text = "Reminders") },
            selected = false,
            onClick = { }
        )
        NavigationDrawerItem(
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Archive,
                    contentDescription = "Archived"
                )
            },
            label = { Text(text = "Archived") },
            selected = false,
            onClick = { /*TODO*/ }
        )
        DrawerLabelItems(
            onEdit = { navController.navigate(NavRoutes.EditLabels.route) },
            labels = labels,
            modifier = Modifier.weight(1f)
        )
    }
}
