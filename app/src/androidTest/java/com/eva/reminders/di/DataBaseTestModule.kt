package com.eva.reminders.di

import android.content.Context
import com.eva.reminders.data.local.AppDataBase
import com.eva.reminders.data.local.dao.LabelsDao
import com.eva.reminders.data.local.dao.LabelsFtsDao
import com.eva.reminders.data.local.dao.TaskDao
import com.eva.reminders.data.local.dao.TaskLabelRelDao
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DataBaseModule::class]
)
object DataBaseTestModule {

    @Provides
    @Singleton
    fun providesAppDatabase(
        @ApplicationContext context: Context
    ): AppDataBase = AppDataBase.buildMockDatabase(context)

    @Provides
    @Singleton
    fun providesLabelFtsDao(
        dataBase: AppDataBase
    ): LabelsFtsDao = dataBase.labelsFts()

    @Provides
    @Singleton
    fun providesLabelsDao(
        dataBase: AppDataBase
    ): LabelsDao = dataBase.taskLabelDao()

    @Provides
    @Singleton
    fun providesTaskDao(
        dataBase: AppDataBase
    ): TaskDao = dataBase.taskDao()

    @Provides
    @Singleton
    fun providesTaskLabelRelDao(
        dataBase: AppDataBase
    ): TaskLabelRelDao = dataBase.taskLabelRelDao()

}