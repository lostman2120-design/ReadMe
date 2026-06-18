package com.hikiyose.app.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.hikiyose.app.data.MethodsData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "settings")

/**
 * App preferences:
 * - [selectedMethodId] : the 〇〇式 selected on the home screen (①)
 */
class SettingsRepository(private val context: Context) {

    val selectedMethodId: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[KEY_METHOD] ?: MethodsData.default.id
    }

    suspend fun setSelectedMethod(id: String) {
        context.dataStore.edit { it[KEY_METHOD] = id }
    }

    companion object {
        private val KEY_METHOD = stringPreferencesKey("selected_method_id")
    }
}
