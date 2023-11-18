package com.eva.reminders.utils

import java.time.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.days
import kotlin.time.DurationUnit


class NextAlarmTimeExtensionTest {

    private val oneDayMillis = 1.days.toInt(DurationUnit.MILLISECONDS)


    @Test
    fun `check one day difference if alarm time is 2 days 5 hours earlier therefore it will be just 3 days`() {
        val alarmTime = LocalDateTime.now().minusDays(2).minusHours(5)

        val nextAlarmTime = alarmTime.nextAlarmTimeInMillis()

        val expectedSeconds = (alarmTime.toMilliSeconds() + oneDayMillis * 3)

        assertEquals(
            expected = millisToLocalDateTime(nextAlarmTime),
            actual = millisToLocalDateTime(expectedSeconds),
            message = "This is a correct one",
        )
    }

    @Test
    fun `check if the alarm time is later in the day suppose 30 minutes therefore there is no extra`() {
        val alarmTime = localDateTimeWithoutMillis().plusMinutes(30)

        val nextAlarmTime = alarmTime.nextAlarmTimeInMillis()

        val expectedSeconds = alarmTime.toMilliSeconds()

        assertEquals(
            expected = millisToLocalDateTime(nextAlarmTime),
            actual = millisToLocalDateTime(expectedSeconds),
            message = ""
        )

    }

    @Test
    fun `check if the alarm time is later lets consider 2 days from now`() {
        val alarmTime = localDateTimeWithoutMillis().plusDays(2)

        val nextAlarmTime = alarmTime.nextAlarmTimeInMillis()

        val expectedSeconds = alarmTime.toMilliSeconds()

        assertEquals(
            expected = millisToLocalDateTime(nextAlarmTime),
            actual = millisToLocalDateTime(expectedSeconds),
            message = "This is a correct one",
        )
    }

    @Test
    fun `check if the alarm time is at 5 days and 12 hours earlier therefore extra should be 6 days `() {
        val alarmTime = localDateTimeWithoutMillis().minusDays(5).minusHours(12)

        val nextAlarmTime = alarmTime.nextAlarmTimeInMillis()

        val expectedSeconds = alarmTime.toMilliSeconds() + oneDayMillis * 6

        assertEquals(
            expected = millisToLocalDateTime(nextAlarmTime),
            actual = millisToLocalDateTime(expectedSeconds),
            message = "This is a correct one",
        )
    }

}