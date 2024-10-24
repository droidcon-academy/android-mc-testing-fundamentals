package com.droidcon.bookhaven.ui.book.list

import android.content.Context
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onFirst
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

class BookListContentTest {
    @get:Rule
    val composeTestRule = createComposeRule()
    private val context = ApplicationProvider.getApplicationContext<Context>()

    @Test
    fun displayLoadingIndicator_when_stateIsLoading() {
        // Arrange
        val fakeState = BookListState(isLoading = true)

        // Act
        composeTestRule.setContent {
            BookListContent(
                state = fakeState,
                actions = {},
            )
        }

        // Assert
        composeTestRule.onNodeWithTag(context.getString(R.string.loading)).assertExists()
        composeTestRule.onNodeWithText(context.getString(R.string.error_text)).assertDoesNotExist()
        composeTestRule
            .onNodeWithContentDescription(context.getString(R.string.add_book))
            .assertDoesNotExist()
    }

    // Your Turn: Write a test to assert that the error Text is shown when view state is error
    @Test
    fun displayErrorText_when_stateIsError() {
        // Arrange
        val fakeState = BookListState(isError = true, isLoading = false)

        // Act
        composeTestRule.setContent {
            BookListContent(
                state = fakeState,
                actions = {},
            )
        }

        // Assert
        composeTestRule.onNodeWithText(context.getString(R.string.error_text)).assertExists()
        composeTestRule.onNodeWithTag(context.getString(R.string.loading)).assertDoesNotExist()
        composeTestRule
            .onNodeWithContentDescription(context.getString(R.string.add_book))
            .assertDoesNotExist()
    }

    @Test
    fun displayBookList_when_booksIsNotEmpty() {
        // Arrange
        val fakeBooks =
            listOf(
                Book(1, "Book 1", "Author 1", "2024", "", 1000L),
                Book(2, "Book 2", "Author 2", "2024", "", 2000L),
            )
        val fakeState = BookListState(books = fakeBooks, isLoading = false)

        // Act
        composeTestRule.setContent {
            BookListContent(
                state = fakeState,
                actions = {},
            )
        }

        // Assert
        composeTestRule.onNodeWithText("Book 1").assertExists()
        composeTestRule.onNodeWithText("Book 2").assertExists()
        composeTestRule.onNodeWithText(context.getString(R.string.sort_by)).assertExists()
        composeTestRule.onNodeWithText(context.getString(R.string.app_name)).assertExists()
        composeTestRule
            .onNodeWithContentDescription(context.getString(R.string.add_book))
            .assertExists()
    }

    @Test
    fun displayEmptyText_when_booksIsEmptyAndSearchQueryIsEmpty() {
        // Arrange
        val fakeState = BookListState(books = emptyList(), searchQuery = "", isLoading = false)

        // Act
        composeTestRule.setContent {
            BookListContent(
                state = fakeState,
                actions = {},
            )
        }

        // Assert
        composeTestRule.onNodeWithText(context.getString(R.string.empty_text)).assertExists()
        composeTestRule
            .onNodeWithContentDescription(context.getString(R.string.add_book))
            .assertExists()
    }

    @Test
    fun displayEmptySearchResultText_when_searchReturnsNoResults() {
        // Arrange
        val fakeState =
            BookListState(books = emptyList(), searchQuery = "Nonexistent book", isLoading = false)

        // Act
        composeTestRule.setContent {
            BookListContent(
                state = fakeState,
                actions = {},
            )
        }

        // Assert
        composeTestRule.onNodeWithText(context.getString(R.string.empty_search_text)).assertExists()
    }

    @Test
    fun invokeAction_when_floatingActionButtonIsClicked() {
        // Arrange
        var isFabClicked = false
        val fakeState = BookListState(isLoading = false)

        // Act
        composeTestRule.setContent {
            BookListContent(
                state = fakeState,
                actions = {
                    if (it is BookListActions.CreateBook) {
                        isFabClicked = true
                    }
                },
            )
        }

        // Assert
        composeTestRule
            .onNodeWithContentDescription(context.getString(R.string.add_book))
            .performClick()
        assertTrue(isFabClicked)
    }

    // Your Turn: Test that DeleteBook action is sent when delete icon is clicked on a book item.
    @Test
    fun invokeAction_when_deleteButtonIsClicked() {
        // Arrange
        val fakeBooks =
            listOf(
                Book(1, "Book 1", "Author 1", "2024", "", 1000L),
                Book(2, "Book 2", "Author 2", "2024", "", 2000L),
            )
        val fakeState = BookListState(books = fakeBooks, isLoading = false)
        var isDeleteClicked = false

        // Act
        composeTestRule.setContent {
            BookListContent(
                state = fakeState,
                actions = {
                    if (it is BookListActions.DeleteBook) {
                        isDeleteClicked = true
                    }
                },
            )
        }

        // Assert
        composeTestRule.onNodeWithText("Book 1").assertExists()
        composeTestRule.onNodeWithText("Book 2").assertExists()
        composeTestRule
            .onAllNodesWithContentDescription(context.getString(R.string.delete))
            .onFirst()
            .performClick()
        assertTrue(isDeleteClicked)
    }

    // Your Turn: Test that OpenBook action is sent when a bookItem is clicked
    @Test
    fun invokeAction_when_bookItemIsClicked() {
        // Arrange
        val fakeBooks =
            listOf(
                Book(1, "Book 1", "Author 1", "2024", "", 1000L),
                Book(2, "Book 2", "Author 2", "2024", "", 2000L),
            )
        val fakeState = BookListState(books = fakeBooks, isLoading = false)
        var isOpenBookClicked = false

        // Act
        composeTestRule.setContent {
            BookListContent(
                state = fakeState,
                actions = {
                    if (it is BookListActions.OpenBook) {
                        isOpenBookClicked = true
                    }
                },
            )
        }

        // Assert
        composeTestRule.onNodeWithText("Book 1").assertExists()
        composeTestRule.onNodeWithText("Book 2").assertExists()
        composeTestRule.onNodeWithText("Book 2").assertExists().performClick()
        assertTrue(isOpenBookClicked)
    }
}
