package com.remindrx.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "users",
)
data class UserEntity(
    @PrimaryKey val email: String,
    val name: String,
    val passwordSaltBase64: String,
    val passwordHashBase64: String,
)

@Entity(
    tableName = "medications",
    indices = [Index("userEmail")],
)
data class MedicationEntity(
    @PrimaryKey val medicationId: String,
    val userEmail: String,
    val name: String,
    val dosage: String,
    /**
     * Simple v1 schedule: 24h "HH:mm" local time.
     * (SRS allows richer patterns; we can extend later.)
     */
    val dailyTime: String,
    val specialInstructions: String,
    val remainingDoses: Int,
    val refillThreshold: Int,
    val isActive: Boolean,
)

@Entity(
    tableName = "dose_logs",
    indices = [Index("medicationId"), Index("userEmail")],
)
data class DoseLogEntity(
    @PrimaryKey val logId: String,
    val medicationId: String,
    val userEmail: String,
    val timestampEpochMillis: Long,
    val status: String, // "TAKEN"
)

