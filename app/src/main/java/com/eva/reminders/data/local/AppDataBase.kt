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
    version = 3,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3)
    ]
)
@TypeConverters(
    ColorEnumAdapter::class,
    DateTimeAdapter::class
)
abstract class AppDataBase : RoomDatabase() {

    // task label dao
    abstract fun taskLabelDao(): LabelsDao

    // task dao
    abstract fun taskDao(): TaskDao

    // label full text search fts
    abstract fun labelsFts(): LabelsFtsDao

    // task label relation dao
    abstract fun taskLabelRelDao(): TaskLabelRelDao

    companion object {
        private const val DATABASE_NAME = "REMINDERS_DATABASE"

        fun buildDataBase(context: Context): AppDataBase {
            return Room
                .databaseBuilder(context, AppDataBase::class.java, DATABASE_NAME)
                .addTypeConverter(DateTimeAdapter.instance)
                .addTypeConverter(ColorEnumAdapter.instance)
                .build()
        }

        fun buildMockDatabase(context: Context): AppDataBase {
            return Room.inMemoryDatabaseBuilder(context, AppDataBase::class.java)
                .allowMainThreadQueries()
                .addTypeConverter(DateTimeAdapter.instance)
                .addTypeConverter(ColorEnumAdapter.instance)
                .build()
        }
    }
}
