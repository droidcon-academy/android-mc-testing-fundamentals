package com.droidcon.bookhaven.data.usecase

import com.droidcon.bookhaven.data.di.IoDispatcher
import com.droidcon.bookhaven.data.model.Book
import com.droidcon.bookhaven.data.model.SortType
import com.droidcon.bookhaven.data.model.SortType.DATE_ADDED
import com.droidcon.bookhaven.data.model.SortType.NAME
import com.droidcon.bookhaven.data.repository.BookRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SearchBooks @Inject constructor(
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    private val repository: BookRepository,
) {
    operator fun invoke(
        query: String,
        sortType: SortType,
    ): Flow<List<Book>> {
        return repository.searchBooks(query).map { books ->
            when (sortType) {
                NAME -> books.sortedBy { it.title.lowercase() }
                DATE_ADDED -> books.sortedByDescending { it.timestamp }
            }
        }.flowOn(dispatcher)
    }
}