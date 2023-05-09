package com.eva.reminders.domain.usecase

import com.eva.reminders.domain.models.CreateTaskModel
import com.eva.reminders.domain.models.TaskModel
import java.time.LocalDateTime

class TaskValidator {

    fun updateValidator(task: TaskModel): Validator {
        return if (task.title.isEmpty() || task.title.length < 3) Validator(
            isValid = false,
            error = "Title is either empty or small ,add some proper title"
        ) else Validator(isValid = true)
    }

    fun createValidator(task: CreateTaskModel): Validator {
        return if (task.title.isEmpty() || task.title.length < 3) Validator(
            isValid = false,
            error = "Title cannot be this small"
        ) else if (task.time?.at != null && task.time.at < LocalDateTime.now()) Validator(
            isValid = false,
            error = "Time has already passed, you can remove the reminder or change the time"
        )
        else Validator(isValid = true)
    }
}