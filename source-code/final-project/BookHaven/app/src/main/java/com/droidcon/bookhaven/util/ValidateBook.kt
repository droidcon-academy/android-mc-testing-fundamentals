package com.droidcon.bookhaven.util

import com.droidcon.bookhaven.data.model.Book

fun Book.isValid(): Boolean = when {
    ((this.title.trim().length < 2) || (this.author.trim().length < 2) || (this.year.trim().length < 2)) -> false
    else -> true
}