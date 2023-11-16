package com.eva.reminders.presentation.feature_create.composables

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eva.reminders.R
import com.eva.reminders.ui.theme.RemindersTheme

@Composable
fun CreateNewLabelCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onSecondaryContainer,
) {
    Card(
        colors = CardDefaults
            .cardColors(containerColor = containerColor, contentColor = contentColor),
        shape = MaterialTheme.shapes.medium,
    ) {
        Row(
            modifier = modifier
                .padding(horizontal = 4.dp, vertical = 8.dp)
                .fillMaxWidth()
                .clickable(onClick = onClick, role = Role.Button),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.Add,
                contentDescription = stringResource(id = R.string.add_icon_desc),
                modifier = Modifier.weight(.1f),
            )
            Spacer(modifier = Modifier.weight(.1f))
            Text(
                text = stringResource(id = R.string.create_new_label),
                modifier = Modifier.weight(.9f),
                style = MaterialTheme.typography.bodyMedium
            )
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
fun CreateNewLabelCardPreview() = RemindersTheme {
    CreateNewLabelCard(onClick = {})
}