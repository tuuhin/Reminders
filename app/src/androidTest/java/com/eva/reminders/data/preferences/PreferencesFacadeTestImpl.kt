package com.eva.reminders.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.eva.reminders.domain.facades.PreferencesFacade
import com.eva.reminders.domain.models.ArrangementStyle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PreferencesFacadeTestImpl(
    private val dataStore: DataStore<Preferences>
) : PreferencesFacade {

    private val arrangementKey = booleanPreferencesKey(PreferencesKeys.ARRANGEMENT_KEY)

    override val arrangementStyle: Flow<ArrangementStyle>
        get() = dataStore.data.map { prefs ->
            if (prefs[arrangementKey] == true)
                ArrangementStyle.BLOCK_STYLE
            else ArrangementStyle.GRID_STYLE
        }

    override suspend fun updateStyle(style: ArrangementStyle) {
        dataStore.edit { prefs ->
            prefs[arrangementKey] = when (style) {
                ArrangementStyle.GRID_STYLE -> false
                ArrangementStyle.BLOCK_STYLE -> true
            }
        }
    }

    suspend fun clearTestPreferences() = dataStore.edit { it.clear() }

}