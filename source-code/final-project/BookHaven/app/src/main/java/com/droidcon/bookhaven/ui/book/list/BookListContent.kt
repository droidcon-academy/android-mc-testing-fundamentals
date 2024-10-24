package com.droidcon.bookhaven.ui.book.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.droidcon.bookhaven.R
import com.droidcon.bookhaven.data.model.Book
import com.droidcon.bookhaven.data.model.SortType

@Composable
fun BookListContent(
    state: BookListState,
    modifier: Modifier = Modifier,
    actions: (BookListActions) -> Unit,
) {
    LaunchedEffect(Unit) {
        actions(BookListActions.GetBooks)
    }

    Box(modifier = modifier.fillMaxSize()) {
        when {
            state.isLoading -> {
                CircularProgressIndicator(
                    modifier =
                        Modifier
                            .width(64.dp)
                            .align(Alignment.Center)
                            .testTag(stringResource(id = R.string.loading)),

                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            }

            state.isError -> {
                Text(
                    text = stringResource(id = R.string.error_text),
                    style =
                        MaterialTheme.typography.bodyMedium.copy(
                            Color.Red,
                        ),
                    modifier = Modifier.align(Alignment.Center),
                )
            }

            else -> {
                BookListView(books = state.books, state.searchQuery, state.sortType, actions)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BoxScope.BookListView(
    books: List<Book>,
    query: String,
    sortType: SortType,
    actions: (BookListActions) -> Unit,
) {
    if (books.isEmpty() && query.isBlank()) {
        Text(
            text = stringResource(id = R.string.empty_text),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.align(Alignment.Center),
        )
    } else {
        Column(Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(id = R.string.app_name),
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                )
                Spacer(modifier = Modifier.weight(1f))
                SortDropDown(
                    sortType = sortType,
                    onItemClick = { actions(BookListActions.SortBooks(it)) },
                )
            }
            SearchBar(
                inputField = {
                    SearchBarDefaults.InputField(
                        query = query,
                        onQueryChange = { actions(BookListActions.UpdateSearchQuery(it.trim())) },
                        onSearch = { actions(BookListActions.SearchBooks) },
                        expanded = true,
                        onExpandedChange = {},
                        enabled = true,
                        placeholder = {
                            Text(text = stringResource(id = R.string.search_books))
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                tint = MaterialTheme.colorScheme.onSurface,
                                contentDescription = null,
                            )
                        },
                        trailingIcon = {},
                        interactionSource = null,
                    )
                },
                expanded = true,
                onExpandedChange = {},
                modifier =
                    Modifier
                        .wrapContentHeight()
                        .weight(1f),
                shape = SearchBarDefaults.inputFieldShape,
                colors = SearchBarDefaults.colors(containerColor = Color.Transparent),
                tonalElevation = 0.dp,
                shadowElevation = SearchBarDefaults.ShadowElevation,
                windowInsets = SearchBarDefaults.windowInsets,
                content = {
                    if (books.isEmpty()) {
                        Text(
                            text = stringResource(id = R.string.empty_search_text),
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth().padding(24.dp),
                        )
                    } else {
                        LazyColumn {
                            items(books) {
                                BookItem(
                                    book = it,
                                    onBookClicked = {
                                        actions(BookListActions.OpenBook(it.id))
                                    },
                                    onDeleteClicked = { actions(BookListActions.DeleteBook(it.id)) },
                                )
                            }
                        }
                    }
                },
            )
        }
    }
    FloatingActionButton(
        modifier =
            Modifier
                .padding(16.dp)
                .align(Alignment.BottomEnd),
        onClick = { actions(BookListActions.CreateBook) },
    ) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_add_24),
            contentDescription = stringResource(id = R.string.add_book),
        )
    }
}
