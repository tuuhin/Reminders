package com.eva.reminders.presentation.feature_create.utils

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

sealed class ReminderTimeOptions(
    val text: String,
    val enable: Boolean,
    val schedule: LocalTime
) {
    data class Morning(
        val date: LocalDate = LocalDate.now()
    ) :
        ReminderTimeOptions(
            text = "Morning",
            enable = LocalDateTime.now() < date.atTime(6, 0),
            schedule = date.atTime(6, 0).toLocalTime()
        )

    data class AfterNoon(
        val date: LocalDate = LocalDate.now()
    ) : ReminderTimeOptions(
        text = "Afternoon",
        enable = LocalDateTime.now() < date.atTime(15, 0),
        schedule = date.atTime(15, 0).toLocalTime()
    )

    data class Evening(
        val date: LocalDate = LocalDate.now()
    ) : ReminderTimeOptions(
        text = "Evening",
        enable = LocalDateTime.now() < date.atTime(18, 0),
        schedule = date.atTime(18, 0).toLocalTime()
    )

    data class Night(
        val date: LocalDate = LocalDate.now()
    ) : ReminderTimeOptions(
        text = "Night",
        enable = LocalDateTime.now() < date.atTime(21, 0),
        schedule = date.atTime(21, 0).toLocalTime()
    )

    data class Custom(val time: LocalTime) : ReminderTimeOptions(
        text = "Custom",
        enable = true,
        schedule = time
    )

    companion object {
        fun allOptions(date: LocalDate = LocalDate.now()): List<ReminderTimeOptions> =
            listOf(Morning(date), AfterNoon(date), Evening(date), Night(date))
    }
}

