package com.hikiyose.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * A user-written affirmation ("I am ...") shown daily on the home screen.
 * Wireframe ①/② : アファメーション（毎日）.
 */
@Entity(tableName = "affirmations")
data class Affirmation(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val text: String,
    val isFavorite: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
)
