package com.eva.reminders.di

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.eva.reminders.data.preferences.PreferencesFacadeImpl
import com.eva.reminders.data.preferences.PreferencesKeys
import com.eva.reminders.domain.facades.PreferencesFacade
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private val Context.datastore by preferencesDataStore(PreferencesKeys.PREFERENCES_FILE_NAME)

@Module
@InstallIn(SingletonComponent::class)
object PreferencesModule {

    @Provides
    @Singleton
    fun providesUserPreferences(@ApplicationContext context: Context): PreferencesFacade =
        PreferencesFacadeImpl(context.datastore)
}