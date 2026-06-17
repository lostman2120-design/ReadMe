package com.hikiyose.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * A TO DO item shown on the home screen.
 * Wireframe ② : TO DO チェックリスト.
 */
@Entity(tableName = "todo_items")
data class TodoItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val isDone: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
)
