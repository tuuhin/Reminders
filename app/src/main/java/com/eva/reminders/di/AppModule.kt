package com.eva.reminders.di

import android.content.Context
import com.eva.reminders.data.local.AppDataBase
import com.eva.reminders.presentation.utils.SaveUserArrangementPreference
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
    fun provideUserArrangement(@ApplicationContext context: Context): SaveUserArrangementPreference =
        SaveUserArrangementPreference(context)
}