package com.droidcon.bookhaven.data.usecase

import com.droidcon.bookhaven.data.model.Book
import com.droidcon.bookhaven.data.model.SortType
import com.droidcon.bookhaven.data.repository.FakeBookRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.util.Date

@OptIn(ExperimentalCoroutinesApi::class)
class SearchBooksTest {
    private lateinit var searchBooks: SearchBooks
    private lateinit var fakeBookRepository: FakeBookRepository
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        // Initialize test dependencies before each test
        fakeBookRepository = FakeBookRepository()
        searchBooks = SearchBooks(testDispatcher, fakeBookRepository)
    }

    @Test
    fun `searchBooks with query and sort by name returns books sorted by name`() = runTest {
        // Arrange - Insert books into the repository
        repeat(5) { index ->
            fakeBookRepository.insertBook(
                Book(
                    index,
                    "Title $index",
                    "Author $index",
                    "2024",
                    "",
                    Date().time.plus(index),
                ),
            )
        }

        // Act - Perform a search sorted by name
        val books = searchBooks("Title", SortType.NAME).first()

        // Assert - Verify books are sorted alphabetically
        assertThat(books.first().title).isEqualTo("Title 0")
        assertThat(books.last().title).isEqualTo("Title 4")
    }

    @Test
    fun `searchBooks with query and sort by date returns books sorted by date`() = runTest {
        // Arrange - Insert books with different timestamps
        repeat(5) { index ->
            fakeBookRepository.insertBook(
                Book(
                    index,
                    "Title $index",
                    "Author $index",
                    "2024",
                    "",
                    Date().time.plus(index),
                ),
            )
        }

        // Act - Perform a search sorted by date added
        val books = searchBooks("Title", SortType.DATE_ADDED).first()

        // Assert - Verify books are sorted by timestamp in descending order
        assertThat(books.first().id).isEqualTo(4) // Newest book
        assertThat(books.last().id).isEqualTo(0) // Oldest book
        assertThat(books.first().timestamp).isGreaterThan(books.last().timestamp)
    }
}