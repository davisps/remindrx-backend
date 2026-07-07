package com.remindrx.ui.vm

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.remindrx.data.AppDatabase
import com.remindrx.data.DoseLogEntity
import com.remindrx.data.MedicationEntity
import com.remindrx.reminders.ReminderScheduler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.UUID

class MedicationsViewModel(
    private val db: AppDatabase,
) : ViewModel() {
    fun observeMedications(userEmail: String): Flow<List<MedicationEntity>> =
        db.medicationDao().observeForUser(userEmail)

    suspend fun loadMedication(medicationId: String): MedicationEntity? =
        db.medicationDao().findById(medicationId)

    fun upsertMedication(context: Context, medication: MedicationEntity) {
        viewModelScope.launch {
            db.medicationDao().upsert(medication)
            ReminderScheduler.scheduleNextDailyReminder(context, medication)
        }
    }

    fun deleteMedication(context: Context, medication: MedicationEntity) {
        viewModelScope.launch {
            db.medicationDao().delete(medication)
            ReminderScheduler.cancel(context, medication.medicationId)
        }
    }

    fun markTaken(context: Context, medicationId: String) {
        viewModelScope.launch {
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
            ReminderScheduler.scheduleNextDailyReminder(context, med)
        }
    }
}

