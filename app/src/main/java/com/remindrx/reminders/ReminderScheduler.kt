package com.remindrx.reminders

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.remindrx.data.MedicationEntity
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId

object ReminderScheduler {
    private const val TAG = "ReminderScheduler"

    fun scheduleNextDailyReminder(context: Context, medication: MedicationEntity) {
        if (!medication.isActive) {
            cancel(context, medication.medicationId)
            return
        }

        val nextTriggerMillis = computeNextTriggerMillis(medication.dailyTime)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pi = reminderPendingIntent(context, medication.medicationId)

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, nextTriggerMillis, pi)
                } else {
                    // Fallback to inexact if permission not granted
                    alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, nextTriggerMillis, pi)
                    Log.w(TAG, "Exact alarm permission not granted, using inexact alarm for ${medication.name}")
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, nextTriggerMillis, pi)
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, nextTriggerMillis, pi)
            }
        } catch (e: SecurityException) {
            Log.e(TAG, "Failed to schedule exact alarm for ${medication.name}", e)
            // Final fallback
            alarmManager.set(AlarmManager.RTC_WAKEUP, nextTriggerMillis, pi)
        }
    }

    fun cancel(context: Context, medicationId: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(reminderPendingIntent(context, medicationId))
    }

    private fun reminderPendingIntent(context: Context, medicationId: String): PendingIntent {
        val intent = Intent(context, ReminderReceiver::class.java).putExtra(ReminderReceiver.EXTRA_MEDICATION_ID, medicationId)
        return PendingIntent.getBroadcast(
            context,
            medicationId.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
    }

    private fun computeNextTriggerMillis(dailyTime: String): Long {
        val now = LocalDateTime.now()
        val time = runCatching { LocalTime.parse(dailyTime) }.getOrElse { LocalTime.of(9, 0) }
        val today = LocalDate.now()
        val candidate = LocalDateTime.of(today, time)
        val next = if (candidate.isAfter(now)) candidate else candidate.plusDays(1)
        return next.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }
}
