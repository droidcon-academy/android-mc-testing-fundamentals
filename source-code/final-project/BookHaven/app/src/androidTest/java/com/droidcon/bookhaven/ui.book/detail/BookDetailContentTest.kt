package com.droidcon.bookhaven.ui.book.detail

import android.content.Context
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import com.droidcon.bookhaven.R
import com.droidcon.bookhaven.data.model.Book
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

/* Exercise: write tests for the BookDetailContent composable. Here's an outline of how to approach it:
1. Ad test tag to CircularProgressIndicator in BookDetailContent and Test Loading State
2. Test Error State
3. Test Save Action
4. Test Delete Action
*/

class BookDetailContentTest {
    @get:Rule
    val composeTestRule = createComposeRule()
    private val context = ApplicationProvider.getApplicationContext<Context>()

    @Test
    fun displayLoadingIndicator_when_stateIsLoading() {
        // Arrange
        val fakeState = BookDetailState(isLoading = true)
        val bookId = 1

        // Act
        composeTestRule.setContent {
            BookDetailContent(
                state = fakeState,
                bookId = bookId,
                actions = {},
            )
        }

        // Assert
        composeTestRule.onNodeWithTag(context.getString(R.string.loading)).assertExists()
    }

    @Test
    fun displayErrorText_when_stateIsError() {
        // Arrange
        val fakeState = BookDetailState(isError = true, isLoading = false)
        val bookId = 1

        // Act
        composeTestRule.setContent {
            BookDetailContent(
                state = fakeState,
                bookId = bookId,
                actions = {},
            )
        }

        // Assert
        composeTestRule.onNodeWithText(context.getString(R.string.error_text)).assertExists()
    }

    @Test
    fun displayBookDetails_when_stateHasBook() {
        // Arrange
        val fakeBook = Book(1, "Book 1", "Author 1", "2024", "Some notes", 1000L)
        val fakeState = BookDetailState(book = fakeBook, isLoading = false)
        val bookId = 1

        // Act
        composeTestRule.setContent {
            BookDetailContent(
                state = fakeState,
                bookId = bookId,
                actions = {},
            )
        }

        // Assert
        composeTestRule.onNodeWithText(fakeBook.title).assertExists()
        composeTestRule.onNodeWithText(fakeBook.author).assertExists()
        composeTestRule.onNodeWithText(fakeBook.year).assertExists()
        composeTestRule.onNodeWithText(fakeBook.notes).assertExists()
    }

    @Test
    fun invokeDeleteBookAction_when_deleteButtonIsClicked() {
        // Arrange
        var isDeleteClicked = false
        val bookId = 1
        val fakeBook = Book(bookId, "Book 1", "Author 1", "2024", "Some notes", 1000L)
        val fakeState = BookDetailState(book = fakeBook, isLoading = false)

        // Act
        composeTestRule.setContent {
            BookDetailContent(
                state = fakeState,
                bookId = bookId,
                actions = {
                    if (it == BookDetailActions.DeleteBook(bookId)) {
                        isDeleteClicked = true
                    }
                },
            )
        }

        // Assert
        composeTestRule
            .onNodeWithContentDescription(context.getString(R.string.delete))
            .performClick()
        assertTrue(isDeleteClicked)
    }

    @Test
    fun invokeSaveBookAction_when_saveButtonIsClickedForNewBook() {
        // Arrange
        var isSaveClicked = false
        val bookId = 0 // For a new book
        val fakeBook = Book(0, "New Book", "New Author", "2024", "New notes", 1000L)
        val fakeState = BookDetailState(book = fakeBook, isLoading = false)

        // Act
        composeTestRule.setContent {
            BookDetailContent(
                state = fakeState,
                bookId = bookId,
                actions = {
                    if (it is BookDetailActions.SaveBook) {
                        isSaveClicked = true
                    }
                },
            )
        }

        // Assert
        composeTestRule
            .onNodeWithContentDescription(context.getString(R.string.save))
            .performClick()
        assertTrue(isSaveClicked)
    }

    @Test
    fun invokeUpdateBookAction_when_saveButtonIsClicked() {
        // Arrange
        var isUpdateClicked = false
        val bookId = 1
        val fakeBook =
            Book(bookId, "Updated Book", "Updated Author", "2025", "Updated notes", 1000L)
        val fakeState = BookDetailState(book = fakeBook, isLoading = false)

        // Act
        composeTestRule.setContent {
            BookDetailContent(
                state = fakeState,
                bookId = bookId,
                actions = {
                    if (it is BookDetailActions.UpdateBook) {
                        isUpdateClicked = true
                    }
                },
            )
        }

        // Assert
        composeTestRule
            .onNodeWithContentDescription(context.getString(R.string.save))
            .performClick()
        assertTrue(isUpdateClicked)
    }
}
