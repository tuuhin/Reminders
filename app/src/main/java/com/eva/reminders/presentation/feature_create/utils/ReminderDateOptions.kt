package com.eva.reminders.presentation.feature_create.utils

import java.time.LocalDate

sealed class ReminderDateOptions(val text: String, val schedule: LocalDate) {

    class Today : ReminderDateOptions(
        text = "Today",
        schedule = LocalDate.now()
    )

    class Tomorrow : ReminderDateOptions(
        text = "Tomorrow",
        schedule = LocalDate.now().plusDays(1)
    )

    class NextWeek : ReminderDateOptions(
        text = "Next ${LocalDate.now().dayOfWeek.name.replaceFirstChar { it.uppercaseChar() }}",
        schedule = LocalDate.now().plusWeeks(1)
    )

    data class Custom(val date: LocalDate) : ReminderDateOptions(
        text = "Custom", schedule = date
    )

}
