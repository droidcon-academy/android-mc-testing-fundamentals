package com.droidcon.bookhaven.data.usecase

import com.droidcon.bookhaven.data.model.Book
import com.droidcon.bookhaven.data.model.SortType
import com.google.common.truth.Truth.assertThat
import com.droidcon.bookhaven.data.repository.FakeBookRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
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
    fun `getBooks should return books sorted by date added in descending order`() =
        runTest {
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
            assertThat(books.first().id).isEqualTo(4)
            assertThat(books.last().id).isEqualTo(0)
            assertThat(books.first().timestamp).isGreaterThan(books.last().timestamp)
        }

    @Test
    fun `getBooks should return books sorted by name in descending order`() =
        runTest {
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
            assertThat(books.first().title).contains("0")
            assertThat(books.first().author).contains("0")
            assertThat(books.last().title).contains("4")
            assertThat(books.last().author).contains("4")
            assertThat(books[0].title).isLessThan(books[1].title)
        }
}
