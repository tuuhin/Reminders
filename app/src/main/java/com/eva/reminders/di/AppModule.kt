package com.eva.reminders.di

import android.content.Context
import com.eva.reminders.data.local.AppDataBase
import com.eva.reminders.data.repository.TaskLabelsRepoImpl
import com.eva.reminders.domain.repository.TaskLabelsRepository
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
    fun getDataBase(@ApplicationContext context: Context):AppDataBase{
        return AppDataBase.buildDataBase(context)
    }

    @Provides
    @Singleton
    fun getTaskDao(database: AppDataBase):TaskLabelsRepository{
        return TaskLabelsRepoImpl(database.taskLabelDao)
    }
}