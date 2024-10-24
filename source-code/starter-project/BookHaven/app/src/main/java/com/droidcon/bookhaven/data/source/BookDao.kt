package com.droidcon.bookhaven.data.source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.droidcon.bookhaven.data.model.Book
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(book: Book)

    @Query("DELETE FROM book WHERE id = :id")
    suspend fun delete(id: Int)

    @Query("SELECT * FROM book WHERE id = :id")
    suspend fun getBook(id: Int): Book

    @Query("SELECT * FROM book")
    fun getBooks(): Flow<List<Book>>

    @Query("SELECT * FROM book WHERE title LIKE '%' || :searchQuery || '%' " +
            "OR author LIKE '%' || :searchQuery || '%'")
    fun searchBooks(searchQuery: String): Flow<List<Book>>
}
