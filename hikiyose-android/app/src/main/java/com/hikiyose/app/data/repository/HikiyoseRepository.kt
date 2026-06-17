package com.hikiyose.app.data.repository

import com.hikiyose.app.data.AppDatabase
import com.hikiyose.app.data.entity.Affirmation
import com.hikiyose.app.data.entity.JournalEntry
import com.hikiyose.app.data.entity.TodoItem
import kotlinx.coroutines.flow.Flow

/** Single entry point to all local data (Room). v1 is fully on-device. */
class HikiyoseRepository(private val db: AppDatabase) {

    // ---- Affirmations ----
    fun affirmations(): Flow<List<Affirmation>> = db.affirmationDao().observeAll()
    suspend fun saveAffirmation(a: Affirmation) = db.affirmationDao().upsert(a)
    suspend fun updateAffirmation(a: Affirmation) = db.affirmationDao().update(a)
    suspend fun deleteAffirmation(a: Affirmation) = db.affirmationDao().delete(a)

    suspend fun seedDefaultAffirmationsIfEmpty() {
        if (db.affirmationDao().count() == 0) {
            DEFAULT_AFFIRMATIONS.forEach { db.affirmationDao().upsert(Affirmation(text = it)) }
        }
    }

    // ---- Journal ----
    fun journalEntries(): Flow<List<JournalEntry>> = db.journalDao().observeAll()
    fun journalByDate(epochDay: Long): Flow<JournalEntry?> = db.journalDao().observeByDate(epochDay)
    suspend fun getJournalByDate(epochDay: Long): JournalEntry? = db.journalDao().getByDate(epochDay)
    suspend fun saveJournal(entry: JournalEntry): Long =
        if (entry.id == 0L) db.journalDao().insert(entry)
        else { db.journalDao().update(entry); entry.id }
    suspend fun deleteJournal(entry: JournalEntry) = db.journalDao().delete(entry)

    // ---- Todo ----
    fun todos(): Flow<List<TodoItem>> = db.todoDao().observeAll()
    suspend fun saveTodo(item: TodoItem) = db.todoDao().upsert(item)
    suspend fun updateTodo(item: TodoItem) = db.todoDao().update(item)
    suspend fun deleteTodo(item: TodoItem) = db.todoDao().delete(item)

    companion object {
        private val DEFAULT_AFFIRMATIONS = listOf(
            "私は日々あらゆる面で、ますます良くなっている。",
            "私は豊かさを受け取るにふさわしい存在だ。",
            "私の願いは、すでに叶いつつある。",
            "私は自分自身を信じ、愛している。",
        )
    }
}
