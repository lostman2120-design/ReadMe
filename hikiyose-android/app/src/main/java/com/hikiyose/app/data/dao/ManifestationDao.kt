package com.hikiyose.app.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.hikiyose.app.data.entity.Manifestation
import kotlinx.coroutines.flow.Flow

@Dao
interface ManifestationDao {
    @Query("SELECT * FROM manifestations WHERE isAchieved = 0 ORDER BY createdAt DESC")
    fun observeActive(): Flow<List<Manifestation>>

    @Query("SELECT * FROM manifestations WHERE isAchieved = 1 ORDER BY achievedEpochDay DESC, createdAt DESC")
    fun observeAchieved(): Flow<List<Manifestation>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(item: Manifestation): Long

    @Update
    suspend fun update(item: Manifestation)

    @Delete
    suspend fun delete(item: Manifestation)
}
