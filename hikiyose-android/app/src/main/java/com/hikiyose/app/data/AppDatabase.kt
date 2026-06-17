package com.hikiyose.app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hikiyose.app.data.dao.AffirmationDao
import com.hikiyose.app.data.dao.JournalDao
import com.hikiyose.app.data.dao.TodoDao
import com.hikiyose.app.data.entity.Affirmation
import com.hikiyose.app.data.entity.JournalEntry
import com.hikiyose.app.data.entity.TodoItem

@Database(
    entities = [Affirmation::class, JournalEntry::class, TodoItem::class],
    version = 1,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun affirmationDao(): AffirmationDao
    abstract fun journalDao(): JournalDao
    abstract fun todoDao(): TodoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun get(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "hikiyose.db",
                ).build().also { INSTANCE = it }
            }
    }
}
