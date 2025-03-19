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
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class BookListContentTest {
    @get:Rule
    val composeTestRule = createComposeRule()
    private val context = ApplicationProvider.getApplicationContext<Context>()

    @Test
    fun displayLoadingIndicator_when_stateIsLoading() {
        // Arrange: Create a fake state where the app is loading
        val fakeState = BookListState(isLoading = true)

        // Act: Render the UI inside the test environment
        composeTestRule.setContent {
            BookListContent(
                state = fakeState,  // Pass in the fake loading state
                actions = {}        // No actions needed for this test
            )
        }

        // Assert: Check if the loading indicator is visible
        composeTestRule.onNodeWithTag(context.getString(R.string.loading)).assertExists()

        // Assert: Check that the error message is NOT shown
        composeTestRule.onNodeWithText(context.getString(R.string.error_text)).assertDoesNotExist()

        // Assert: Check that the FAB is NOT shown
        composeTestRule.onNodeWithContentDescription(context.getString(R.string.add_book))
            .assertDoesNotExist()
    }

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
        // Arrange: Create a list of books and a fake state representing the UI.
        val fakeBooks = listOf(
            Book(1, "Book 1", "Author 1", "2024", "", 1000L),
            Book(2, "Book 2", "Author 2", "2024", "", 2000L),
        )
        val fakeState = BookListState(books = fakeBooks, isLoading = false)

        // Act: Render the BookListContent composable with our fake state.
        composeTestRule.setContent {
            BookListContent(
                state = fakeState,
                actions = {},
            )
        }

        // Assert: Verify that book titles, the sort button, and search bar exist.
        composeTestRule.onNodeWithText("Book 1").assertExists()
        composeTestRule.onNodeWithText("Book 2").assertExists()
        composeTestRule.onNodeWithContentDescription(context.getString(R.string.sort_by))
            .assertExists()
        composeTestRule.onNodeWithContentDescription(context.getString(R.string.add_book))
            .assertExists()
    }

    @Test
    fun displayEmptyText_when_booksIsEmptyAndSearchQueryIsEmpty() {
        // Arrange: Create a state where there are no books and no active search.
        val fakeState = BookListState(books = emptyList(), searchQuery = "", isLoading = false)

        // Act: Render the BookListContent composable.
        composeTestRule.setContent {
            BookListContent(
                state = fakeState,
                actions = {},
            )
        }

        // Assert: Verify that the empty state text and "Add Book" button are visible.
        composeTestRule.onNodeWithText(context.getString(R.string.empty_text)).assertExists()
        composeTestRule.onNodeWithContentDescription(context.getString(R.string.add_book))
            .assertExists()
    }

    @Test
    fun displayEmptySearchResultText_when_searchReturnsNoResults() {
        // Arrange: Simulate a search query with no matching results.
        val fakeState = BookListState(
            books = emptyList(),
            searchQuery = "Nonexistent book",
            isLoading = false
        )

        // Act: Render the BookListContent composable with the fake state.
        composeTestRule.setContent {
            BookListContent(
                state = fakeState,
                actions = {},
            )
        }

        // Assert: Verify that the "No book matches your search…" message is displayed.
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
                actions = { action ->
                    if (action is BookListActions.CreateBook) {
                        isFabClicked = true  // Mark FAB as clicked when CreateBook is triggered
                    }
                },
            )
        }

        composeTestRule
            .onNodeWithContentDescription(context.getString(R.string.add_book))
            .performClick()

        assertTrue(isFabClicked)
    }

    @Test
    fun invokeAction_when_deleteButtonIsClicked() {
        // Arrange
        val fakeBooks = listOf(
            Book(1, "Book 1", "Author 1", "2024", "", 1000L),
            Book(2, "Book 2", "Author 2", "2024", "", 2000L),
        )
        val fakeState = BookListState(books = fakeBooks, isLoading = false)
        var isDeleteClicked = false
        var deletedBookId: Int? = null

        // Act
        composeTestRule.setContent {
            BookListContent(
                state = fakeState,
                actions = { action ->
                    if (action is BookListActions.DeleteBook) {
                        isDeleteClicked = true
                        deletedBookId = action.id
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

        composeTestRule.onNodeWithText(context.getString(R.string.delete_confirmation)).assertExists()
        composeTestRule.onNodeWithText(context.getString(R.string.confirm)).assertExists()
        composeTestRule.onNodeWithText(context.getString(R.string.cancel)).assertExists()

        composeTestRule
            .onNodeWithText(context.getString(R.string.confirm))
            .performClick()

        assertTrue(isDeleteClicked)
        assertEquals(fakeBooks.first().id, deletedBookId)
    }

    @Test
    fun invokeAction_when_bookItemIsClicked() {
        // Arrange
        val fakeBooks = listOf(
            Book(1, "Book 1", "Author 1", "2024", "", 1000L),
            Book(2, "Book 2", "Author 2", "2024", "", 2000L),
        )
        val fakeState = BookListState(books = fakeBooks, isLoading = false)
        var isOpenBookClicked = false

        // Act
        composeTestRule.setContent {
            BookListContent(
                state = fakeState,
                actions = { action ->
                    if (action is BookListActions.OpenBook) {
                        isOpenBookClicked = true
                    }
                },
            )
        }

        // Assert
        composeTestRule.onNodeWithText("Book 1").assertExists()
        composeTestRule.onNodeWithText("Book 2").assertExists()
        composeTestRule.onNodeWithText("Book 2").performClick()
        assertTrue(isOpenBookClicked)
    }
}