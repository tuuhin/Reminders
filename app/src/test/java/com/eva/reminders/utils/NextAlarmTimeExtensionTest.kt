package com.eva.reminders.utils

import java.time.LocalDateTime
import java.util.Calendar
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.days
import kotlin.time.DurationUnit


class NextAlarmTimeExtensionTest {

    private val oneDayMillis = 1.days.toInt(DurationUnit.MILLISECONDS).toLong()


    // Most of the tests were flaky with [LocalDateTime.now] thus used [localDateTimeWithoutMillis]
    // this will act the same but without millis

    private fun dateTimeToMillis(dateTime: LocalDateTime): Long {
        val calender = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_YEAR, dateTime.dayOfYear)
            set(Calendar.HOUR, dateTime.hour)
            set(Calendar.MINUTE, dateTime.minute)
            set(Calendar.SECOND, dateTime.second)
        }
        return calender.timeInMillis
    }


    @Test
    fun `check one day difference if alarm time is 2 days 5 hours earlier therefore it will be just 3 days`() {
        val alarmTime = localDateTimeWithoutMillis().minusDays(2).minusHours(5)

        val nextAlarmTime = alarmTime.nextAlarmTimeInMillis()

        val expectedSeconds = (dateTimeToMillis(alarmTime) + oneDayMillis * 3)

        assertEquals(
            expected = expectedSeconds,
            actual = nextAlarmTime,
            message = "This is a correct one",
        )
    }

    @Test
    fun `check if the alarm time is later in the day suppose 30 minutes therefore there is no extra`() {
        val alarmTime = localDateTimeWithoutMillis().plusMinutes(30)

        val nextAlarmTime = alarmTime.nextAlarmTimeInMillis()

        val expectedSeconds = dateTimeToMillis(alarmTime)

        assertEquals(
            expected = expectedSeconds,
            actual = nextAlarmTime,
            message = "This is a correct one",
        )

    }

    @Test
    fun `check if the alarm time is later lets consider 2 days from now`() {
        val alarmTime = localDateTimeWithoutMillis().plusDays(2)

        val nextAlarmTime = alarmTime.nextAlarmTimeInMillis()

        val expectedSeconds = dateTimeToMillis(alarmTime)

        assertEquals(
            expected = expectedSeconds,
            actual = nextAlarmTime,
            message = "This is a correct one",
        )
    }

    @Test
    fun `check if the alarm time is at 5 days and 12 hours earlier therefore extra should be 6 days `() {
        val alarmTime = localDateTimeWithoutMillis().minusDays(5).minusHours(12)

        val nextAlarmTime = alarmTime.nextAlarmTimeInMillis()

        val expectedSeconds = dateTimeToMillis(alarmTime) + oneDayMillis * 6

        assertEquals(
            expected = expectedSeconds,
            actual = nextAlarmTime,
            message = "This is a correct one",
        )
    }

}