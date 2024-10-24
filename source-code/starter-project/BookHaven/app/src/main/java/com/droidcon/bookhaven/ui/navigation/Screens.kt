package com.droidcon.bookhaven.ui.navigation

import kotlinx.serialization.Serializable


@Serializable
object BookList

@Serializable
data class BookDetail(val bookId: Int)