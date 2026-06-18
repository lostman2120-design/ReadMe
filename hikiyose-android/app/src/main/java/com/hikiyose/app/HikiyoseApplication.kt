package com.hikiyose.app

import android.app.Application
import com.hikiyose.app.data.AppDatabase
import com.hikiyose.app.data.repository.HikiyoseRepository
import com.hikiyose.app.data.repository.SettingsRepository
import com.hikiyose.app.notification.AffirmationNotifier

/**
 * Application with simple manual dependency wiring (no Hilt needed for v1).
 * Holds the singletons the ViewModels depend on.
 */
class HikiyoseApplication : Application() {
    val repository: HikiyoseRepository by lazy { HikiyoseRepository(AppDatabase.get(this)) }
    val settings: SettingsRepository by lazy { SettingsRepository(this) }

    override fun onCreate() {
        super.onCreate()
        AffirmationNotifier.createChannel(this)
    }
}
