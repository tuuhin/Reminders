package com.eva.reminders.presentation.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.eva.reminders.presentation.feature_home.utils.TaskArrangementStyle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

const val PREFERENCE_NAME = "USER_PREFERENCES"

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCE_NAME)

class SaveUserArrangementPreference(
    private val context: Context
) {

    private val arrangementInfo = booleanPreferencesKey("IS_ARRANGEMENT_LIST")

    val readArrangementStyle: Flow<TaskArrangementStyle> = context.dataStore.data.map { prefs ->
        if (prefs[arrangementInfo] == true) TaskArrangementStyle.BLOCK_STYLE else TaskArrangementStyle.GRID_STYLE
    }

    suspend fun updateArrangementStyle(arrangementStyle: TaskArrangementStyle) {
        context.dataStore.edit { prefs ->
            prefs[arrangementInfo] = when (arrangementStyle) {
                TaskArrangementStyle.GRID_STYLE -> false
                TaskArrangementStyle.BLOCK_STYLE -> true
            }
        }
    }
}