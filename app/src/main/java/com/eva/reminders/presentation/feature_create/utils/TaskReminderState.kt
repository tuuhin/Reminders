package com.eva.reminders.presentation.feature_create.utils

import java.time.LocalTime

data class TaskReminderState(
    val time: ReminderTimeOptions = ReminderTimeOptions.allOptionsExceptCustom().firstOrNull { it.enable }
        ?: ReminderTimeOptions.Custom(time = LocalTime.of(23, 59)),
    val invalidTime: String? = null,
    val date: ReminderDateOptions = ReminderDateOptions.Today(),
    val frequency: ReminderFrequency = ReminderFrequency.DO_NOT_REPEAT,
    val isExact: Boolean = false
)


sealed interface TaskRemindersEvents {
    data class OnDateChanged(val date: ReminderDateOptions) : TaskRemindersEvents
    data class OnTimeChanged(val time: ReminderTimeOptions) : TaskRemindersEvents
    data class OnReminderChanged(val frequency: ReminderFrequency) : TaskRemindersEvents

    data class OnIsExactChange(val isExact: Boolean) : TaskRemindersEvents
}
