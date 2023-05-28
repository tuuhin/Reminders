package com.eva.reminders.presentation.feature_home.utils

import com.eva.reminders.domain.enums.TaskColorEnum
import com.eva.reminders.domain.models.TaskLabelModel
import com.eva.reminders.domain.models.TaskModel
import com.eva.reminders.domain.models.TaskReminderModel
import java.time.LocalDateTime

val taskModelList = sequenceOf(
    TaskModel(
        id = 0,
        title = "Something",
        content = "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English. Many desktop publishing packages and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ipsum' will uncover many web sites still in their infancy. Various versions have evolved over the years, sometimes by accident, sometimes on purpose (injected humour and the like).",
        pinned = false,
        color = TaskColorEnum.TRANSPARENT,
        reminderAt = TaskReminderModel(),
        isArchived = false,
        updatedAt = LocalDateTime.now(),
        isExact = true, labels = emptyList()

    ), TaskModel(
        id = 1,
        title = "Something",
        content = "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English. Many desktop publishing packages and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ipsum' will uncover many web sites still in their infancy. Various versions have evolved over the years, sometimes by accident, sometimes on purpose (injected humour and the like).",
        pinned = true,
        color = TaskColorEnum.PURPLE,
        reminderAt = TaskReminderModel(at = LocalDateTime.now()),
        isArchived = false,
        updatedAt = LocalDateTime.now().plusDays(1),
        isExact = false,
        labels = emptyList()
    ), TaskModel(
        id = 2,
        title = "Something",
        content = "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English. Many desktop publishing packages and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ipsum' will uncover many web sites still in their infancy. Various versions have evolved over the years, sometimes by accident, sometimes on purpose (injected humour and the like).",
        pinned = false,
        color = TaskColorEnum.GREEN,
        reminderAt = TaskReminderModel(
            at = LocalDateTime.of(2023, 4, 28, 20, 40), isRepeating = true
        ),
        isArchived = false,
        updatedAt = LocalDateTime.now(),
        isExact = false,
        labels = listOf(TaskLabelModel(0, "One"), TaskLabelModel(1, "Two"))
    ), TaskModel(
        id = 4,
        title = "Something",
        content = "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English. Many desktop publishing packages and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ipsum' will uncover many web sites still in their infancy. Various versions have evolved over the years, sometimes by accident, sometimes on purpose (injected humour and the like).",
        pinned = true,
        color = TaskColorEnum.ROSE,
        reminderAt = TaskReminderModel(at = LocalDateTime.now()),
        isArchived = false,
        updatedAt = LocalDateTime.of(2023, 6, 1, 10, 40),
        isExact = true,
        labels = listOf(
            TaskLabelModel(0, "One"),
            TaskLabelModel(1, "Two"),
            TaskLabelModel(2, "Three"),
            TaskLabelModel(3, "Four"),
            TaskLabelModel(4, "Five"),
            TaskLabelModel(5, "Six")
        )
    )
)