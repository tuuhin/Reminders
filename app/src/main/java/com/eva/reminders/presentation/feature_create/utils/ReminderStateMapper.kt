package com.eva.reminders.presentation.feature_create.utils

import com.eva.reminders.domain.models.TaskReminderModel
import java.time.LocalDate
import java.time.LocalTime

fun TaskReminderState.toModel(isPresent: Boolean): TaskReminderModel = TaskReminderModel(
    at = if (isPresent) date.schedule.atTime(time.schedule) else null,
    isRepeating = frequency.isRepeating
)

fun TaskReminderModel.toState(isExact: Boolean): TaskReminderState = at?.let { dateTime ->
    TaskReminderState(
        frequency = if (isRepeating) ReminderFrequency.DAILY else ReminderFrequency.DO_NOT_REPEAT,
        time = when (dateTime.toLocalTime()) {
            LocalTime.of(6, 0) -> ReminderTimeOptions.Morning()
            LocalTime.of(15, 0) -> ReminderTimeOptions.AfterNoon()
            LocalTime.of(18, 0) -> ReminderTimeOptions.Evening()
            LocalTime.of(21, 0) -> ReminderTimeOptions.Night()
            else -> ReminderTimeOptions.Custom(dateTime.toLocalTime())
        },
        date = when (dateTime.toLocalDate()) {
            LocalDate.now() -> ReminderDateOptions.Today()
            LocalDate.now().plusDays(1) -> ReminderDateOptions.Tomorrow()
            LocalDate.now().plusWeeks(1) -> ReminderDateOptions.NextWeek()
            else -> ReminderDateOptions.Custom(dateTime.toLocalDate())
        },
        isExact = isExact
    )
} ?: TaskReminderState()