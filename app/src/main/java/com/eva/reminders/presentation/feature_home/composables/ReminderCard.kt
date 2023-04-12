package com.eva.reminders.presentation.feature_home.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.eva.reminders.domain.models.TaskModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderCard(
    taskModel: TaskModel,
    modifier: Modifier = Modifier,
) {
    val reminderTime = remember {
        derivedStateOf {
            val pattern = DateTimeFormatter.ofPattern("dd MMMM , hh:mm a")
            taskModel.time.format(pattern)
        }
    }
    Card(
        modifier = modifier.padding(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Text(text = taskModel.title, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = taskModel.content,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(vertical = 4.dp)
                    .border(BorderStroke(1.dp, Color.Black), shape = MaterialTheme.shapes.small)
            ) {
                Text(
                    text = reminderTime.value,
                    modifier = Modifier.padding(vertical = 6.dp, horizontal = 12.dp)
                )
            }
        }
    }
}


private class ReminderCardPreviewParams : PreviewParameterProvider<TaskModel> {
    override val values: Sequence<TaskModel> = sequenceOf(
        TaskModel(
            id = 0,
            title = "Something",
            content = "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English. Many desktop publishing packages and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ipsum' will uncover many web sites still in their infancy. Various versions have evolved over the years, sometimes by accident, sometimes on purpose (injected humour and the like).",
            pinned = false,
            colorHex = "#ff00ff",
            time = LocalDateTime.now(),
            isArchived = false
        )
    )

}

@Composable
@Preview
private fun ReminderCardPreview(
    @PreviewParameter(ReminderCardPreviewParams::class) task: TaskModel
) {
    ReminderCard(task)
}
