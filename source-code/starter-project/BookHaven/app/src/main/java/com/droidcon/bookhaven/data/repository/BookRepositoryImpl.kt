package com.droidcon.bookhaven.data.repository

import com.droidcon.bookhaven.data.model.Book
import com.droidcon.bookhaven.data.source.BookDao
import kotlinx.coroutines.flow.Flow

class BookRepositoryImpl(
    private val dao: BookDao,
) : BookRepository {
    override fun getAllBooks(): Flow<List<Book>> = dao.getBooks()

    override suspend fun getBookById(id: Int): Book = dao.getBook(id)

    override suspend fun deleteBook(id: Int) = dao.delete(id)

    override suspend fun insertBook(book: Book) = dao.insert(book)

    override fun searchBooks(query: String): Flow<List<Book>> = dao.searchBooks(query)
}
