package com.eva.reminders.data.local

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.eva.reminders.data.local.adapters.ColorEnumAdapter
import com.eva.reminders.data.local.adapters.DateTimeAdapter
import com.eva.reminders.data.local.dao.LabelsDao
import com.eva.reminders.data.local.dao.LabelsFtsDao
import com.eva.reminders.data.local.dao.TaskDao
import com.eva.reminders.data.local.dao.TaskLabelRelDao
import com.eva.reminders.data.local.entity.LabelEntity
import com.eva.reminders.data.local.entity.LabelFtsEntity
import com.eva.reminders.data.local.entity.TaskEntity
import com.eva.reminders.data.local.entity.TaskLabelRel

@Database(
    entities = [
        TaskEntity::class,
        LabelEntity::class,
        TaskLabelRel::class,
        LabelFtsEntity::class
    ],
    version = 2,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 1, to = 2)
    ]
)
@TypeConverters(ColorEnumAdapter::class, DateTimeAdapter::class)
abstract class AppDataBase : RoomDatabase() {

    abstract val taskLabelDao: LabelsDao
    abstract val taskDao: TaskDao
    abstract val labelsFts: LabelsFtsDao
    abstract val taskLabelRelDao: TaskLabelRelDao

    companion object {
        private const val DATABASE_NAME = "REMINDERS_DATABASE"

        fun buildDataBase(context: Context): AppDataBase {
            return Room
                .databaseBuilder(context, AppDataBase::class.java, DATABASE_NAME)
                .addTypeConverter(DateTimeAdapter.instance)
                .addTypeConverter(ColorEnumAdapter.instance)
                .build()
        }
    }
}
