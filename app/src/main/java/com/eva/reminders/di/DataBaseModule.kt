package com.eva.reminders.di

import android.content.Context
import com.eva.reminders.data.local.AppDataBase
import com.eva.reminders.data.local.dao.LabelsDao
import com.eva.reminders.data.local.dao.LabelsFtsDao
import com.eva.reminders.data.local.dao.TaskDao
import com.eva.reminders.data.local.dao.TaskLabelRelDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {

    @Provides
    @Singleton
    fun providesAppDatabase(@ApplicationContext context: Context): AppDataBase =
        AppDataBase.buildDataBase(context)

    @Provides
    @Singleton
    fun providesLabelFtsDao(dataBase: AppDataBase): LabelsFtsDao =
        dataBase.labelsFts()

    @Provides
    @Singleton
    fun providesLabelsDao(dataBase: AppDataBase): LabelsDao =
        dataBase.taskLabelDao()

    @Provides
    @Singleton
    fun providesTaskDao(dataBase: AppDataBase): TaskDao =
        dataBase.taskDao()

    @Provides
    @Singleton
    fun providesTaskLabelRelDao(dataBase: AppDataBase): TaskLabelRelDao =
        dataBase.taskLabelRelDao()


}