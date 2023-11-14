package com.eva.reminders.presentation.feature_create.utils

import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

sealed class ReminderDateOptions(
    val text: String,
    val schedule: LocalDate = LocalDate.now()
) {

    class Today : ReminderDateOptions(
        text = "Today",
        schedule = LocalDate.now()
    )

    class Tomorrow : ReminderDateOptions(
        text = "Tomorrow",
        schedule = LocalDate.now().plusDays(1)
    )

    class NextWeek(
        displayText: String = "Next ${
            LocalDate.now().dayOfWeek.getDisplayName(
                TextStyle.FULL,
                Locale.getDefault()
            )
        }"
    ) : ReminderDateOptions(
        text = displayText,
        schedule = LocalDate.now().plusWeeks(1)
    )

    data class Custom(val date: LocalDate) : ReminderDateOptions(
        text = "Custom", schedule = date
    )

}
