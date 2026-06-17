package com.hikiyose.app.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.hikiyose.app.data.entity.JournalEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface JournalDao {
    @Query("SELECT * FROM journal_entries ORDER BY dateEpochDay DESC")
    fun observeAll(): Flow<List<JournalEntry>>

    @Query("SELECT * FROM journal_entries WHERE dateEpochDay = :epochDay LIMIT 1")
    fun observeByDate(epochDay: Long): Flow<JournalEntry?>

    @Query("SELECT * FROM journal_entries WHERE dateEpochDay = :epochDay LIMIT 1")
    suspend fun getByDate(epochDay: Long): JournalEntry?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: JournalEntry): Long

    @Update
    suspend fun update(entry: JournalEntry)

    @Delete
    suspend fun delete(entry: JournalEntry)
}
