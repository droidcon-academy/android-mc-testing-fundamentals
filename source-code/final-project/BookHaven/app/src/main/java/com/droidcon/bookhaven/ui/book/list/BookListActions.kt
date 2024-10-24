package com.droidcon.bookhaven.ui.book.list

import com.droidcon.bookhaven.data.model.SortType

sealed interface BookListActions {
    data object GetBooks : BookListActions

    data object CreateBook : BookListActions

    data class DeleteBook(
        val id: Int,
    ) : BookListActions

    data class OpenBook(
        val id: Int,
    ) : BookListActions

    data class SortBooks(
        val sortType: SortType,
    ) : BookListActions

    data class UpdateSearchQuery(
        val query: String,
    ) : BookListActions

    data object SearchBooks : BookListActions
}
