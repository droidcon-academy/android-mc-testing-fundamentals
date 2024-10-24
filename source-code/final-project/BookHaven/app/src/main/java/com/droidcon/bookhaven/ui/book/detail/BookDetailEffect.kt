package com.droidcon.bookhaven.ui.book.detail

sealed interface BookDetailEffect {
    data object NavigateBack : BookDetailEffect

    data object ShowInvalidBookToast : BookDetailEffect

    data object None : BookDetailEffect
}
