package com.eva.reminders.di

import android.content.Context
import com.eva.reminders.data.local.AppDataBase
import com.eva.reminders.data.repository.TaskLabelsRepoImpl
import com.eva.reminders.data.repository.TaskRepoImpl
import com.eva.reminders.domain.repository.TaskLabelsRepository
import com.eva.reminders.domain.repository.TaskRepository
import com.eva.reminders.presentation.utils.SaveUserArrangementPreference
import com.eva.reminders.services.AlarmManagerRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun getDataBase(@ApplicationContext context: Context): AppDataBase =
        AppDataBase.buildDataBase(context)


    @Provides
    @Singleton
    fun getTaskLabelDao(database: AppDataBase): TaskLabelsRepository =
        TaskLabelsRepoImpl(
            labelDao = database.taskLabelDao,
            labelFts = database.labelsFts
        )

    @Provides
    @Singleton
    fun getTaskDao(database: AppDataBase, alarmRepo: AlarmManagerRepo): TaskRepository =
        TaskRepoImpl(
            taskDao = database.taskDao,
            taskLabelRel = database.taskLabelRelDao,
            alarmRepo = alarmRepo
        )

    @Provides
    fun provideUserArrangement(@ApplicationContext context: Context): SaveUserArrangementPreference =
        SaveUserArrangementPreference(context)
}