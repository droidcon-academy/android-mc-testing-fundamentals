package com.droidcon.bookhaven.util

import com.droidcon.bookhaven.data.model.Book
import com.google.common.truth.Truth.assertThat
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue

import org.junit.Test

class ValidateBookKtTest {

    @Test
    fun `isValid returns false when title is less than 2 characters`() {
        // Arrange
        val book = Book(
            title = "A", // Invalid title
            author = "Author",
            year = "2020",
            id = 1,
            timestamp = 0L,
            notes = "",
        )

        // Act
        val result = book.isValid()

        // Assert
        assertFalse(result)
    }

    @Test
    fun `isValid returns true for a valid book`() {
        // Arrange
        val book = Book(
            title = "Valid Title",
            author = "Valid Author",
            year = "2020",
            id = 1,
            timestamp = 0L,
            notes = "",
        )

        // Act
        val result = book.isValid()

        // Assert
        assertTrue(result)
    }

    @Test
    fun `isValid returns true for a valid book using Truth assertions`() {
        // Arrange
        val book = Book(
            title = "Valid Title",
            author = "Valid Author",
            year = "2020",
            id = 1,
            timestamp = 0L,
            notes = "",
        )
        // Act
        val result = book.isValid()
        // Assert
        assertThat(book.title.length).isGreaterThan(1)
        assertThat(book.author.length).isGreaterThan(1)
        assertThat(book.year.length).isGreaterThan(1)
        assertThat(result).isTrue()
    }

    @Test
    fun `isValid returns false when Author has 1 character using Truth assertions`() {
        // Arrange
        val book = Book(
            title = "Valid Title",
            author = "V", // Invalid author
            year = "2020",
            id = 1,
            timestamp = 0L,
            notes = "",
        )

        // Act
        val result = book.isValid()

        // Assert
        assertThat(book.author.length).isLessThan(2)
        assertThat(result).isFalse()
    }

    @Test
    fun `isValid returns false when year is two white spaces using Truth assertions`() {
        // Arrange
        val book = Book(
            title = "Valid Title",
            author = "Valid Author",
            year = "  ", // Invalid year
            id = 1,
            timestamp = 0L,
            notes = "",
        )

        // Act
        val result = book.isValid()

        // Assert
        assertThat(book.year.length).isEqualTo(2)
        assertThat(result).isFalse()
    }
}