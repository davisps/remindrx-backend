package com.remindrx.reminders

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.app.PendingIntent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.remindrx.MainActivity
import com.remindrx.data.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val medicationId = intent.getStringExtra(EXTRA_MEDICATION_ID) ?: return
        val pendingResult = goAsync()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val db = AppDatabase.get(context)
                val medication = db.medicationDao().findById(medicationId) ?: return@launch
                if (!medication.isActive) return@launch

                val openAppIntent = Intent(context, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                }
                val contentPi = PendingIntent.getActivity(
                    context,
                    medicationId.hashCode(),
                    openAppIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
                )

                val takenIntent = Intent(context, MarkTakenReceiver::class.java)
                    .putExtra(MarkTakenReceiver.EXTRA_MEDICATION_ID, medicationId)
                val takenPi = PendingIntent.getBroadcast(
                    context,
                    ("taken_$medicationId").hashCode(),
                    takenIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
                )

                val notification = NotificationCompat.Builder(context, Notifications.CHANNEL_ID)
                    .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                    .setContentTitle("Time to take ${medication.name}")
                    .setContentText("${medication.dosage}${medication.specialInstructions.takeIf { it.isNotBlank() }?.let { " • $it" } ?: ""}")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true)
                    .setContentIntent(contentPi)
                    .addAction(0, "Taken", takenPi)
                    .build()

                with(NotificationManagerCompat.from(context)) {
                    notify(medicationId.hashCode(), notification)
                }

                ReminderScheduler.scheduleNextDailyReminder(context, medication)
            } finally {
                pendingResult.finish()
            }
        }
    }

    companion object {
        const val EXTRA_MEDICATION_ID = "extra_medication_id"
    }
}

