package com.hikiyose.app.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.hikiyose.app.data.entity.Affirmation
import kotlinx.coroutines.flow.Flow

@Dao
interface AffirmationDao {
    @Query("SELECT * FROM affirmations ORDER BY createdAt DESC")
    fun observeAll(): Flow<List<Affirmation>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(affirmation: Affirmation): Long

    @Update
    suspend fun update(affirmation: Affirmation)

    @Delete
    suspend fun delete(affirmation: Affirmation)

    @Query("SELECT COUNT(*) FROM affirmations")
    suspend fun count(): Int
}
