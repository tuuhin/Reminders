package com.eva.reminders.utils

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.Calendar
import kotlin.time.Duration.Companion.days
import kotlin.time.DurationUnit

fun localDateTimeWithoutMillis(): LocalDateTime {
    val currentTime = LocalTime.now()
    return LocalDateTime.of(
        LocalDate.now(),
        LocalTime.of(currentTime.hour, currentTime.minute, currentTime.second)
    )
}

fun LocalDateTime.nextAlarmTimeInMillis(compareWith: LocalDateTime = localDateTimeWithoutMillis()): Long {
    val daysDifference = if (this < compareWith) {
        (compareWith.dayOfYear - dayOfYear).let { diff ->
            println(diff)
            val addExtraDay = if (compareWith.toLocalTime() < LocalTime.now()) 1 else 0
            diff + addExtraDay
        }
    } else 0
    val extraMillis = daysDifference.days.toInt(DurationUnit.MILLISECONDS)

    val originalToMillis = Calendar.getInstance().apply {
        add(Calendar.SECOND, second)
    }
    return originalToMillis.timeInMillis + extraMillis
}