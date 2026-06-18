package com.hikiyose.app.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.hikiyose.app.HikiyoseApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/** Re-arms the daily reminder after a device reboot, if it was enabled. */
class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED) return
        val pendingResult = goAsync()
        val app = context.applicationContext as HikiyoseApplication
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val settings = app.settings.reminderSettingsOnce()
                if (settings.enabled) {
                    ReminderScheduler.schedule(context, settings.hour, settings.minute)
                }
            } finally {
                pendingResult.finish()
            }
        }
    }
}
