package com.hikiyose.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

/**
 * A thing the user is manifesting ("必ず引き寄せること", wireframe ②).
 * It has a lifecycle: active -> achieved. When achieved (wireframe ④), the
 * achievement date and the user's thoughts are recorded, and it appears in
 * the past-records screen (wireframe ⑤).
 */
@Entity(tableName = "manifestations")
data class Manifestation(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val text: String,
    val isAchieved: Boolean = false,
    val achievedEpochDay: Long? = null,
    val thoughts: String = "",
    val createdAt: Long = System.currentTimeMillis(),
) {
    val achievedDate: LocalDate? get() = achievedEpochDay?.let { LocalDate.ofEpochDay(it) }
}
