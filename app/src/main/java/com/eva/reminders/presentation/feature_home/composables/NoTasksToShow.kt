package com.eva.reminders.presentation.feature_home.composables

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eva.reminders.R
import com.eva.reminders.ui.theme.RemindersTheme

@Composable
fun NoTasksToShow(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_reminder_logo),
            contentDescription = stringResource(id = R.string.no_reminders_to_show),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.surfaceTint)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(id = R.string.no_reminders_to_show),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL)
@Composable
fun NoTaskToShowPreview() = RemindersTheme {
    Surface(color = MaterialTheme.colorScheme.background) {
        NoTasksToShow()

    }
}