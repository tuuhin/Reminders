package com.eva.reminders.utils

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.util.Calendar
import kotlin.time.Duration.Companion.days
import kotlin.time.DurationUnit

/**
 * Provides the current [LocalDateTime] instance without considering the milliseconds
 */
fun localDateTimeWithoutMillis(): LocalDateTime {
    val currentTime = LocalTime.now()
    return LocalDateTime.of(
        /* date = */ LocalDate.now(),
        /* time = */ LocalTime.of(currentTime.hour, currentTime.minute, currentTime.second)
    )
}

/**
 * converts millis to [LocalDateTime]
 * @param triggerTime Milliseconds to be converted to
 * @return [LocalDateTime] of [triggerTime]
 */
fun millisToLocalDateTime(triggerTime: Long): LocalDateTime =
    Instant.ofEpochMilli(triggerTime)
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime()

/**
 * Provides the next alarm Time in millis
 * @param compareWith [LocalDateTime] to which it's compared to
 * @return Millis in which the next alarm should be triggered
 */
fun LocalDateTime.nextAlarmTimeInMillis(compareWith: LocalDateTime = localDateTimeWithoutMillis()): Long {
    val daysDifference = if (this < compareWith) {
        (compareWith.dayOfYear - dayOfYear).let { diff ->
            val addExtraDay = if (compareWith.toLocalTime() < LocalTime.now()) 1 else 0
            diff + addExtraDay
        }
    } else 0
    val extraMillis = daysDifference.days.toInt(DurationUnit.MILLISECONDS)
    // println(daysDifference)

    val originalToMillis = Calendar.getInstance().apply {
        set(Calendar.DAY_OF_YEAR, dayOfYear)
        set(Calendar.HOUR, hour)
        set(Calendar.MINUTE, minute)
        set(Calendar.SECOND, second)
    }

    return originalToMillis.timeInMillis + extraMillis
}

/**
 * Provides the nextAlarmTime in compare to the [compareWith] parameter
 * @param compareWith [LocalDateTime] to which it's compared to
 * @return [LocalDateTime] of the next alarm time
 */
fun LocalDateTime.nextAlarmTime(compareWith: LocalDateTime = localDateTimeWithoutMillis()): LocalDateTime {
    val now = LocalDateTime.now()
    if (this < compareWith) {
        (compareWith.dayOfYear - dayOfYear).let { diff ->
            val addExtraDay = if (compareWith.toLocalTime() < LocalTime.now()) 1L else 0L
            now.plusDays(diff + addExtraDay)
        }
    }
    return now
}