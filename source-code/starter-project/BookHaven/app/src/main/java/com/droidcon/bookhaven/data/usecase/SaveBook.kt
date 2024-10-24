package com.droidcon.bookhaven.data.usecase

import com.droidcon.bookhaven.data.di.IoDispatcher
import com.droidcon.bookhaven.data.model.Book
import com.droidcon.bookhaven.data.repository.BookRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SaveBook @Inject constructor(
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    private val repository: BookRepository
) {
    suspend operator fun invoke(book: Book) {
        withContext(dispatcher) {
            repository.insertBook(book)
        }
    }
}