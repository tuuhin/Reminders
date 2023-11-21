package com.eva.reminders.utils

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
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

fun LocalDateTime.toMilliSeconds(): Long {
    return this.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
}

/**
 * Provides the next alarm Time in millis
 * @param compareWith [LocalDateTime] to which it's compared to
 * @return Millis in which the next alarm should be triggered
 */
fun LocalDateTime.nextAlarmTimeInMillis(compareWith: LocalDateTime = localDateTimeWithoutMillis()): Long {
    val daysDifference = if (compareWith > this) {
        (compareWith.dayOfYear - dayOfYear).let { diff ->
            println("THe day difference is $this $compareWith $diff ")
            val addExtraDay = if (compareWith.toLocalTime() > toLocalTime()) 1 else 0
            diff + addExtraDay
        }
    } else 0
    val extraMillis = daysDifference.days.toInt(DurationUnit.MILLISECONDS)
    val originalToMillis = toMilliSeconds()

    return originalToMillis + extraMillis
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