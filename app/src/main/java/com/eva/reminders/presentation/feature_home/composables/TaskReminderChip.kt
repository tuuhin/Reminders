package com.eva.reminders.presentation.feature_home.composables

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Alarm
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eva.reminders.R
import com.eva.reminders.ui.theme.RemindersTheme
import com.eva.reminders.utils.formatToDayMothTime
import com.eva.reminders.utils.nextAlarmTime
import java.time.LocalDateTime

@Composable
fun TaskReminderChip(
    time: LocalDateTime,
    isRepeating: Boolean,
    isExact: Boolean,
    modifier: Modifier = Modifier,
    containerColor: Color = colorResource(id = R.color.white_overlay),
    contentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant
) {

    val reminderTime by remember(time) {
        derivedStateOf {
            if (isRepeating && time < LocalDateTime.now()) {
                val nextAt = time.nextAlarmTime()
                nextAt.formatToDayMothTime()
            } else
                time.formatToDayMothTime()
        }
    }

    val isCrossed by remember(time) {
        derivedStateOf { !isRepeating && time < LocalDateTime.now() }
    }

    Box(
        modifier = modifier
            .clip(MaterialTheme.shapes.small)
            .background(containerColor)
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 4.dp, horizontal = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (isExact)
                Icon(
                    imageVector = Icons.Outlined.Schedule,
                    contentDescription = null,
                    tint = contentColor
                )
            else
                Icon(
                    imageVector = Icons.Outlined.Alarm,
                    contentDescription = null,
                    tint = contentColor
                )
            Text(
                text = reminderTime,
                textDecoration = if (isCrossed) TextDecoration.LineThrough else null,
                fontStyle = if (isCrossed) FontStyle.Italic else null,
                style = MaterialTheme.typography.labelSmall,
                color = contentColor
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
fun TaskReminderChipPreview() = RemindersTheme {
    TaskReminderChip(
        time = LocalDateTime.now(),
        isRepeating = true,
        isExact = true
    )
}