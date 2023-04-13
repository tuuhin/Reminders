package com.eva.reminders.data.local.adapters

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.eva.reminders.domain.enums.TaskColorEnum

@ProvidedTypeConverter
class ColorEnumAdapter {

    @TypeConverter
    fun toColorEnum(enum: String): TaskColorEnum = TaskColorEnum.valueOf(enum)

    @TypeConverter
    fun fromColorEnum(enum: TaskColorEnum): String = enum.name

    companion object {
        val instance: ColorEnumAdapter = ColorEnumAdapter()
    }
}