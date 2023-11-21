package com.eva.reminders.utils

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

private val dayMonthTimeFormat = DateTimeFormatter.ofPattern("dd MMM,hh:mm a")

private val dayMonthFormat = DateTimeFormatter.ofPattern("dd MMMM")

private val dayMonthShortFormat = DateTimeFormatter.ofPattern("d MMM")

private val currentTimeFormat = DateTimeFormatter.ofPattern("h:mm a")

fun LocalDateTime.formatToDayMothTime(): String = dayMonthTimeFormat.format(this)

fun LocalDate.formatToDayMonth(): String = dayMonthFormat.format(this)

fun LocalDate.formatToDayMonthShort(): String = dayMonthShortFormat.format(this)

fun LocalTime.toCurrentDateTime(): String = currentTimeFormat.format(this)



