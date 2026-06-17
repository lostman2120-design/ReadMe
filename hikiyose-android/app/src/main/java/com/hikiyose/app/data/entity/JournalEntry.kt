package com.hikiyose.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

/**
 * A single journaling entry tied to a calendar date.
 * Wireframe ③ : 日付・今日の出来事に感謝・本文。書式(templateId)で見出し構成が変わる。
 *
 * [date] is stored as epoch-day (Long) for easy ordering and one-entry-per-day lookup.
 */
@Entity(tableName = "journal_entries")
data class JournalEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val dateEpochDay: Long,
    val templateId: String,
    val gratitude: String = "",
    val body: String = "",
    val mood: Int = 0,
    val updatedAt: Long = System.currentTimeMillis(),
) {
    val date: LocalDate get() = LocalDate.ofEpochDay(dateEpochDay)
}
