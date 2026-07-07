package com.remindrx.ui.vm

import androidx.lifecycle.ViewModel
import com.remindrx.data.AppDatabase
import com.remindrx.data.DoseLogEntity
import com.remindrx.data.DoseLogWithMedication
import kotlinx.coroutines.flow.Flow

class HistoryViewModel(
    private val db: AppDatabase,
) : ViewModel() {
    fun observeRecent(userEmail: String): Flow<List<DoseLogWithMedication>> =
        db.doseLogDao().observeRecentWithMedication(userEmail)

    fun observeForMedication(medicationId: String): Flow<List<DoseLogEntity>> =
        db.doseLogDao().observeForMedication(medicationId)
}
