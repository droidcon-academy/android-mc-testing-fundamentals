package com.droidcon.bookhaven.ui.book.list

import com.droidcon.bookhaven.data.model.Book
import com.droidcon.bookhaven.data.model.SortType

data class BookListState(
    val books: List<Book> = emptyList(),
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val searchQuery: String = "",
    val sortType: SortType = SortType.DATE_ADDED,
)
