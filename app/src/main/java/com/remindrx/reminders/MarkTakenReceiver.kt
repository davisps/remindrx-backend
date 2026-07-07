package com.remindrx.reminders

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.remindrx.data.AppDatabase
import com.remindrx.data.DoseLogEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID

class MarkTakenReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val medicationId = intent.getStringExtra(EXTRA_MEDICATION_ID) ?: return
        val pendingResult = goAsync()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val db = AppDatabase.get(context)
                val med = db.medicationDao().findById(medicationId) ?: return@launch
                if (!med.isActive) return@launch

                val newRemaining = (med.remainingDoses - 1).coerceAtLeast(0)
                db.medicationDao().upsert(med.copy(remainingDoses = newRemaining))

                db.doseLogDao().insert(
                    DoseLogEntity(
                        logId = UUID.randomUUID().toString(),
                        medicationId = medicationId,
                        userEmail = med.userEmail,
                        timestampEpochMillis = System.currentTimeMillis(),
                        status = "TAKEN",
                    ),
                )

                // Update next reminder (e.g., user might have edited time).
                ReminderScheduler.scheduleNextDailyReminder(context, med)
            } finally {
                pendingResult.finish()
            }
        }
    }

    companion object {
        const val EXTRA_MEDICATION_ID = "extra_medication_id"
    }
}

