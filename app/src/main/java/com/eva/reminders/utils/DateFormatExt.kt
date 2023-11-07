package com.eva.reminders.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private val dayMonthTimeFormat = DateTimeFormatter.ofPattern("dd MMM,hh:mm a")

fun LocalDateTime.formatToDayMothTime(): String = dayMonthTimeFormat.format(this)