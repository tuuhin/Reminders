package com.eva.reminders.data.local.adapters

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@ProvidedTypeConverter
class DateTimeAdapter {

    private val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm:ss a")

    @TypeConverter
    fun toLocalDateTime(time: String?): LocalDateTime? =
        time?.let { LocalDateTime.parse(time, formatter) }

    @TypeConverter
    fun fromLocalDateTime(time: LocalDateTime?): String? = time?.let { formatter.format(time) }

    companion object {
        val instance: DateTimeAdapter = DateTimeAdapter()
    }
}