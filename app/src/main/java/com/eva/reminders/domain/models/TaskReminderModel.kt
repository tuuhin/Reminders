package com.eva.reminders.domain.models

import java.time.LocalDateTime

data class TaskReminderModel(
    val at: LocalDateTime? = null,
    val isRepeating: Boolean = false
)
