package com.droidcon.bookhaven.data.usecase

import com.droidcon.bookhaven.data.model.Book
import com.droidcon.bookhaven.data.model.SortType
import com.droidcon.bookhaven.data.repository.FakeBookRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import java.util.Date

@OptIn(ExperimentalCoroutinesApi::class)
class GetBooksTest {
    private lateinit var getBooks: GetBooks
    private lateinit var fakeBookRepository: FakeBookRepository
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        fakeBookRepository = FakeBookRepository()
        getBooks = GetBooks(testDispatcher, fakeBookRepository)
    }

    @Test
    fun `getBooks should return books sorted by date added in descending order`() = runTest {
        // Arrange
        repeat(5) { index ->
            fakeBookRepository.insertBook(
                Book(
                    index,
                    "title $index",
                    "author $index",
                    "2024",
                    "",
                    Date().time.plus(index),
                ),
            )
        }

        // Act
        val books = getBooks(SortType.DATE_ADDED).first()

        // Assert
        assertThat(books.size).isEqualTo(5)
        assertThat(books.first().id).isEqualTo(4) // Last added book should be first
        assertThat(books.last().id).isEqualTo(0)  // First added book should be last
        assertThat(books.first().timestamp).isGreaterThan(books.last().timestamp)
    }

    @Test
    fun `getBooks should return books sorted by name in descending order`() = runTest {
        // Arrange
        repeat(5) { index ->
            fakeBookRepository.insertBook(
                Book(
                    index,
                    "title $index",
                    "author $index",
                    "2024",
                    "",
                    Date().time.plus(index),
                ),
            )
        }

        // Act
        val books = getBooks(SortType.NAME).first()

        // Assert
        assertThat(books.size).isEqualTo(5)
        assertThat(books.first().title).contains("0")
        assertThat(books.last().title).contains("4")
        assertThat(books[0].title).isLessThan(books[1].title)
    }
}