package com.eva.reminders.di

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.eva.reminders.data.preferences.PreferencesFacadeImpl
import com.eva.reminders.data.preferences.PreferencesKeys
import com.eva.reminders.domain.facades.PreferencesFacade
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton


private val Context.testDatastore by preferencesDataStore(PreferencesKeys.TEST_PREFERENCES_FILE_NAME)

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [PreferencesModule::class]
)
object PreferencesTestModule {

    @Provides
    @Singleton
    fun providesTestPreferences(@ApplicationContext context: Context): PreferencesFacade =
        PreferencesFacadeImpl(context.testDatastore)
}