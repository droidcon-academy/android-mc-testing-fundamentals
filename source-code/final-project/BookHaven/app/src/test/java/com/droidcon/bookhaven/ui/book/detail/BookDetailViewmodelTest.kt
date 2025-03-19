package com.droidcon.bookhaven.ui.book.detail

import com.droidcon.bookhaven.data.model.Book
import com.droidcon.bookhaven.data.usecase.DeleteBook
import com.droidcon.bookhaven.data.usecase.GetBookById
import com.droidcon.bookhaven.data.usecase.SaveBook
import com.google.common.truth.Truth.assertThat
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class BookDetailViewmodelTest {
    private lateinit var bookDetailViewModel: BookDetailViewmodel
    private val testDispatcher = UnconfinedTestDispatcher()
    // Mock use cases
    private val saveBookUseCase: SaveBook = mockk()
    private val deleteBookUseCase: DeleteBook = mockk()
    private val getBookById: GetBookById = mockk()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher) // Replace Main dispatcher
        bookDetailViewModel = BookDetailViewmodel(saveBookUseCase, deleteBookUseCase, getBookById)
    }


    @Test
    fun `loadBook sets book in state when valid id is provided`() = runTest {
        // Arrange
        val book = Book(1, "A Book", "Author", "2024", "", 1000L)
        coEvery { getBookById(1) } returns book // Stub the method to return the book

        // Act
        bookDetailViewModel.userActions.send(BookDetailActions.LoadBook(1))
        val state = bookDetailViewModel.state.first()

        // Assert
        assertThat(state.book).isEqualTo(book)
        assertThat(state.isLoading).isFalse()
        coVerify { getBookById(1) } // Verify that the function was called
    }

    @Test
    fun `saveBook emits NavigateBack when book is valid`() = runTest {
        // Arrange
        val book = Book(1, "A Book", "Author", "2024", "", 1000L)
        coEvery { saveBookUseCase(any()) } just Runs

        // Act
        bookDetailViewModel.userActions.send(BookDetailActions.SaveBook(book))
        val effect = bookDetailViewModel.effect.first()

        // Assert
        assertThat(effect).isEqualTo(BookDetailEffect.NavigateBack)
        coVerify { saveBookUseCase(book) }
    }

    @Test
    fun `deleteBook emits NavigateBack after deletion`() = runTest {
        // Arrange
        coEvery { deleteBookUseCase(1) } just Runs

        // Act
        bookDetailViewModel.userActions.send(BookDetailActions.DeleteBook(1))
        val effect = bookDetailViewModel.effect.first()

        // Assert
        assertThat(effect).isEqualTo(BookDetailEffect.NavigateBack)
        coVerify { deleteBookUseCase(1) }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // Restore original dispatcher
    }
}