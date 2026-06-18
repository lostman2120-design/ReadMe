package com.hikiyose.app.data.repository

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.hikiyose.app.data.MethodsData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "settings")

/** Snapshot of the morning-affirmation reminder settings. */
data class ReminderSettings(
    val enabled: Boolean = false,
    val hour: Int = 7,
    val minute: Int = 0,
)

/**
 * App preferences:
 * - [selectedMethodId]  : the 〇〇式 selected on the home screen (①)
 * - reminder settings   : 毎朝のアファメーション通知の ON/OFF と時刻
 */
class SettingsRepository(private val context: Context) {

    val selectedMethodId: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[KEY_METHOD] ?: MethodsData.default.id
    }

    suspend fun setSelectedMethod(id: String) {
        context.dataStore.edit { it[KEY_METHOD] = id }
    }

    val reminderSettings: Flow<ReminderSettings> = context.dataStore.data.map { prefs ->
        ReminderSettings(
            enabled = prefs[KEY_REMINDER_ENABLED] ?: false,
            hour = prefs[KEY_REMINDER_HOUR] ?: 7,
            minute = prefs[KEY_REMINDER_MINUTE] ?: 0,
        )
    }

    /** One-shot read, used by alarm/boot receivers outside Compose. */
    suspend fun reminderSettingsOnce(): ReminderSettings = reminderSettings.first()

    suspend fun setReminder(enabled: Boolean, hour: Int, minute: Int) {
        context.dataStore.edit { prefs ->
            prefs[KEY_REMINDER_ENABLED] = enabled
            prefs[KEY_REMINDER_HOUR] = hour
            prefs[KEY_REMINDER_MINUTE] = minute
        }
    }

    companion object {
        private val KEY_METHOD = stringPreferencesKey("selected_method_id")
        private val KEY_REMINDER_ENABLED = booleanPreferencesKey("reminder_enabled")
        private val KEY_REMINDER_HOUR = intPreferencesKey("reminder_hour")
        private val KEY_REMINDER_MINUTE = intPreferencesKey("reminder_minute")
    }
}
