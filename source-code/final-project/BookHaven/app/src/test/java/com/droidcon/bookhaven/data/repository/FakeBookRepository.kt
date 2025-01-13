package com.droidcon.bookhaven.data.repository

import com.droidcon.bookhaven.data.model.Book
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeBookRepository : BookRepository {
    private val books = mutableListOf<Book>()

    override fun getAllBooks(): Flow<List<Book>> = flow { emit(books) }

    override suspend fun getBookById(id: Int): Book = books.first { it.id == id }

    override suspend fun deleteBook(id: Int) {
        books.removeIf { it.id == id }
    }

    override suspend fun insertBook(book: Book) {
        books.add(book)
    }

    override fun searchBooks(query: String): Flow<List<Book>> =
        flow {
            emit(books.filter { it.title.contains(query, true) || it.author.contains(query, true) })
        }
}
