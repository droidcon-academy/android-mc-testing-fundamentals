package com.droidcon.bookhaven.ui.book.list

import com.droidcon.bookhaven.data.model.Book
import com.droidcon.bookhaven.data.model.SortType
import com.droidcon.bookhaven.data.repository.FakeBookRepository
import com.droidcon.bookhaven.data.usecase.DeleteBook
import com.droidcon.bookhaven.data.usecase.GetBooks
import com.droidcon.bookhaven.data.usecase.SearchBooks
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first

class BooksViewModelTest {
    private lateinit var booksViewModel: BooksViewModel
    private lateinit var fakeBookRepository: FakeBookRepository
    private lateinit var getBooks: GetBooks
    private lateinit var deleteBook: DeleteBook
    private lateinit var searchBooks: SearchBooks
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeBookRepository = FakeBookRepository()
        getBooks = GetBooks(testDispatcher, fakeBookRepository)
        deleteBook = DeleteBook(testDispatcher, fakeBookRepository)
        searchBooks = SearchBooks(testDispatcher, fakeBookRepository)

        booksViewModel = BooksViewModel(getBooks, deleteBook, searchBooks)
    }

    @Test
    fun `GetBooks sorts by date when no SortType is specified`() = runTest {
        // Arrange
        val books = listOf(
            Book(1, "Book A", "Author", "2024", "", 1000L),
            Book(2, "Book B", "Author", "2024", "", 2000L),
            Book(3, "Book C", "Author", "2024", "", 500L),
        )
        books.forEach { fakeBookRepository.insertBook(it) }

        // Act
        booksViewModel.userActions.send(BookListActions.GetBooks)

        // Assert
        assertThat(booksViewModel.state.value.isLoading).isFalse()
        assertThat(booksViewModel.state.value.books.size).isEqualTo(3)
        assertThat(booksViewModel.state.value.books.first().id).isEqualTo(2) // Sorted by timestamp (DATE_ADDED)
    }

    @Test
    fun `SortBooks fetches and sorts books according to SortType when called`() = runTest {
        // Arrange
        val books = listOf(
            Book(1, "Book A", "Author", "2024", "", 1000L),
            Book(2, "Book B", "Author", "2024", "", 2000L),
            Book(3, "Book C", "Author", "2024", "", 500L),
        )
        books.forEach { fakeBookRepository.insertBook(it) }

        // Act
        booksViewModel.userActions.send(BookListActions.SortBooks(SortType.NAME))


        // Assert
        assertThat(booksViewModel.state.value.isLoading).isFalse()
        assertThat(booksViewModel.state.value.books.size).isEqualTo(3)
        assertThat(booksViewModel.state.value.books.first().id,).isEqualTo(1) // "Book A" should be first
        assertThat(booksViewModel.state.value.books.last().id).isEqualTo(3) // "Book C" should be last
    }

    @Test
    fun `searchBooks fetches and filters books based on the provided search term`() = runTest {
        // Arrange
        val books = listOf(
            Book(1, "Book A", "Author", "2024", "", 1000L),
            Book(2, "Book B", "Author", "2024", "", 2000L),
            Book(3, "Book C", "Author", "2024", "", 500L),
        )
        books.forEach { fakeBookRepository.insertBook(it) }

        // Act
        booksViewModel.userActions.send(BookListActions.UpdateSearchQuery("C"))
        booksViewModel.userActions.send(BookListActions.SearchBooks)

        // Assert
        assertThat(booksViewModel.state.value.isLoading).isFalse()
        assertThat(booksViewModel.state.value.books.size).isEqualTo(1)
        assertThat(booksViewModel.state.value.books.first().title).contains("C")
        assertThat(booksViewModel.state.value.books.first().id).isEqualTo(3)
    }

    @Test
    fun `delete book removes the book from the list`() = runTest {
        // Arrange
        fakeBookRepository.insertBook(Book(1, "A Book", "Author", "2024", "", 1000L))

        // Act
        booksViewModel.userActions.send(BookListActions.DeleteBook(1))
        val state = booksViewModel.state.first()

        // Assert
        assertThat(state.books).isEmpty()
    }


    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}