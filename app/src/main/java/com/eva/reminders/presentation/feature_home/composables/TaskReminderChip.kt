package com.eva.reminders.presentation.feature_home.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AvTimer
import androidx.compose.material.icons.outlined.Timer
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eva.reminders.R
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun TaskReminderChip(
    time: LocalDateTime,
    isRepeating: Boolean,
    isExact: Boolean,
    modifier: Modifier = Modifier
) {

    val reminderTime by remember(time) {
        derivedStateOf {
            val pattern = DateTimeFormatter.ofPattern("dd MMMM,hh:mm a")
            if (isRepeating && time < LocalDateTime.now()) {
                val daysDiff = (LocalDateTime.now().dayOfYear - time.dayOfYear).toLong()
                time.plusDays(daysDiff).format(pattern)
            } else
                time.format(pattern)
        }
    }

    val isCrossed by remember(time) {
        derivedStateOf { !isRepeating && time < LocalDateTime.now() }
    }

    Box(
        modifier = modifier
            .clip(MaterialTheme.shapes.small)
            .background(colorResource(id = R.color.white_overlay))
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 4.dp, horizontal = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (isExact) Icons.Outlined.AvTimer else Icons.Outlined.Timer,
                contentDescription = "Timer"
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = reminderTime,
                textDecoration = if (isCrossed) TextDecoration.LineThrough else null,
                fontStyle = if (isCrossed) FontStyle.Italic else null,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@Preview
@Composable
fun TaskReminderChipPreview() {
    TaskReminderChip(time = LocalDateTime.now(), isRepeating = true, isExact = true)
}