package com.droidcon.bookhaven.data.source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.droidcon.bookhaven.data.model.Book

@Database(entities = [Book::class], version = 1, exportSchema = false)
abstract class BookDatabase : RoomDatabase() {
    abstract val bookDao: BookDao

    companion object {
        const val DATABASE_NAME: String = "book_database"
    }
}