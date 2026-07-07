package com.remindrx.reminders

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

object Notifications {
    const val CHANNEL_ID = "remindrx_reminders"

    fun ensureChannel(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val existing = nm.getNotificationChannel(CHANNEL_ID)
        if (existing != null) return

        nm.createNotificationChannel(
            NotificationChannel(
                CHANNEL_ID,
                "Medication reminders",
                NotificationManager.IMPORTANCE_HIGH,
            ).apply {
                description = "Notifications to remind you to take medications and refill them."
            },
        )
    }
}

