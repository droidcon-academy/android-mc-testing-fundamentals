package com.droidcon.bookhaven.ui.book.list

sealed interface BookListEffect {
    data class OpenBook(
        val id: Int,
    ) : BookListEffect

    data object CreateBook : BookListEffect

    data object None : BookListEffect
}
