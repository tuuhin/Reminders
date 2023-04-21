package com.eva.reminders.di

import android.content.Context
import com.eva.reminders.data.local.AppDataBase
import com.eva.reminders.data.repository.BootAlarmInitImpl
import com.eva.reminders.domain.repository.BootAlarmInitRepo
import com.eva.reminders.services.AlarmManagerImpl
import com.eva.reminders.services.AlarmManagerRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @Provides
    @Singleton
    fun bootRepo(database: AppDataBase): BootAlarmInitRepo = BootAlarmInitImpl(database.taskDao)

    @Provides
    @Singleton
    fun getAlarmManager(@ApplicationContext context: Context): AlarmManagerRepo =
        AlarmManagerImpl(context)

}