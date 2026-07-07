package com.remindrx.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(user: UserEntity)

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun findByEmail(email: String): UserEntity?
}

@Dao
interface MedicationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(medication: MedicationEntity)

    @Update
    suspend fun update(medication: MedicationEntity)

    @Delete
    suspend fun delete(medication: MedicationEntity)

    @Query("SELECT * FROM medications WHERE userEmail = :userEmail ORDER BY name ASC")
    fun observeForUser(userEmail: String): Flow<List<MedicationEntity>>

    @Query("SELECT * FROM medications WHERE medicationId = :medicationId LIMIT 1")
    suspend fun findById(medicationId: String): MedicationEntity?
}

data class DoseLogWithMedication(
    val logId: String,
    val medicationId: String,
    val userEmail: String,
    val timestampEpochMillis: Long,
    val status: String,
    val medicationName: String?
)

@Dao
interface DoseLogDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(log: DoseLogEntity)

    @Query("""
        SELECT dl.*, m.name as medicationName 
        FROM dose_logs dl 
        LEFT JOIN medications m ON dl.medicationId = m.medicationId 
        WHERE dl.userEmail = :userEmail 
        ORDER BY dl.timestampEpochMillis DESC 
        LIMIT :limit
    """)
    fun observeRecentWithMedication(userEmail: String, limit: Int = 200): Flow<List<DoseLogWithMedication>>

    @Query("SELECT * FROM dose_logs WHERE userEmail = :userEmail ORDER BY timestampEpochMillis DESC LIMIT :limit")
    fun observeRecent(userEmail: String, limit: Int = 200): Flow<List<DoseLogEntity>>

    @Query("SELECT * FROM dose_logs WHERE medicationId = :medicationId ORDER BY timestampEpochMillis DESC LIMIT :limit")
    fun observeForMedication(medicationId: String, limit: Int = 200): Flow<List<DoseLogEntity>>
}
