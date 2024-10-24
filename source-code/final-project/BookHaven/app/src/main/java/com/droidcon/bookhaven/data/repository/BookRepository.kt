package com.droidcon.bookhaven.data.repository

import com.droidcon.bookhaven.data.model.Book
import kotlinx.coroutines.flow.Flow

interface BookRepository {
    fun getAllBooks(): Flow<List<Book>>

    suspend fun getBookById(id: Int): Book

    suspend fun deleteBook(id: Int)

    suspend fun insertBook(book: Book)

    fun searchBooks(query: String): Flow<List<Book>>
}
