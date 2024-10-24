package com.droidcon.bookhaven.ui.book.detail

import com.droidcon.bookhaven.data.model.Book

sealed interface BookDetailActions {
    data class LoadBook(
        val bookId: Int,
    ) : BookDetailActions

    data class DeleteBook(
        val id: Int,
    ) : BookDetailActions

    data class SaveBook(
        val book: Book,
    ) : BookDetailActions

    data class UpdateBook(
        val book: Book,
    ) : BookDetailActions
}
