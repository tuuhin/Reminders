package com.eva.reminders.presentation.feature_create.utils

import java.time.LocalTime

data class TaskReminderState(
    val time: ReminderTimeOptions = ReminderTimeOptions.allOptions().firstOrNull { it.enable }
        ?: ReminderTimeOptions.Custom(time = LocalTime.of(23, 59)),
    val invalidTime: String? = null,
    val date: ReminderDateOptions = ReminderDateOptions.Today,
    val frequency: ReminderFrequency = ReminderFrequency.DO_NOT_REPEAT
)


sealed class TaskRemindersEvents {
    data class OnDateChanged(val date: ReminderDateOptions) : TaskRemindersEvents()
    data class OnTimeChanged(val time: ReminderTimeOptions) : TaskRemindersEvents()
    data class OnReminderChanged(val frequency: ReminderFrequency) : TaskRemindersEvents()
}
