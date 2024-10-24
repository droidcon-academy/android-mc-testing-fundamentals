package com.droidcon.bookhaven.ui.book.detail

import com.droidcon.bookhaven.data.model.Book

data class BookDetailState(
    val isLoading: Boolean = false,
    val book: Book? = null,
    val isError: Boolean = false,
)
