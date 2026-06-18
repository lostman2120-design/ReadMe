package com.hikiyose.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

/**
 * A daily journal entry tied to a calendar date (wireframe ③).
 *
 * - [idealDay]  : 「今日はどんな日になれば最高？」への答え
 * - [body]      : 中央のリスト形式メモ
 * - [goodThings]: 「今日よかった・嬉しかったこと」
 *
 * [dateEpochDay] is stored as epoch-day for ordering and one-entry-per-day lookup.
 */
@Entity(tableName = "journal_entries")
data class JournalEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val dateEpochDay: Long,
    val idealDay: String = "",
    val body: String = "",
    val goodThings: String = "",
    val updatedAt: Long = System.currentTimeMillis(),
) {
    val date: LocalDate get() = LocalDate.ofEpochDay(dateEpochDay)
}
