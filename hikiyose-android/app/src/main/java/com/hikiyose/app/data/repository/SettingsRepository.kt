package com.hikiyose.app.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.hikiyose.app.data.JournalTemplates
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "settings")

/** App preferences. Currently: the journaling template (書式) selected on screen ④. */
class SettingsRepository(private val context: Context) {

    val selectedTemplateId: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[KEY_TEMPLATE] ?: JournalTemplates.default.id
    }

    suspend fun setSelectedTemplate(id: String) {
        context.dataStore.edit { it[KEY_TEMPLATE] = id }
    }

    companion object {
        private val KEY_TEMPLATE = stringPreferencesKey("selected_template_id")
    }
}
