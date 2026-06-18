package com.hikiyose.app.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.hikiyose.app.HikiyoseApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/** Fires each morning: picks a random affirmation and posts the notification. */
class ReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val pendingResult = goAsync()
        val app = context.applicationContext as HikiyoseApplication
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val affirmations = app.repository.affirmationsOnce()
                val text = affirmations.randomOrNull()?.text ?: DEFAULT_MESSAGE
                AffirmationNotifier.show(context, text)
            } finally {
                pendingResult.finish()
            }
        }
    }

    companion object {
        private const val DEFAULT_MESSAGE =
            "私は日々あらゆる面で、ますます良くなっている。"
    }
}
