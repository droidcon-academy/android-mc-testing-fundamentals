package com.droidcon.bookhaven.ui.book.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
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

    var showDialog by remember { mutableStateOf(false) }
    var bookIdToDelete by remember { mutableStateOf<Int?>(null) }

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
                BookListView(books = state.books, state.searchQuery, state.sortType, actions) {
                    showDialog = true
                    bookIdToDelete = it
                }
            }
        }

        if (showDialog && bookIdToDelete != null) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                confirmButton = {
                    TextButton(onClick = {
                        bookIdToDelete?.let { actions(BookListActions.DeleteBook(it)) }
                        showDialog = false
                    }) {
                        Text(text = stringResource(id = R.string.confirm))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text(text = stringResource(id = R.string.cancel))
                    }
                },
                text = { Text(text = stringResource(id = R.string.delete_confirmation)) },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BookListView(
    books: List<Book>,
    query: String,
    sortType: SortType,
    actions: (BookListActions) -> Unit,
    showDeleteDialog: (Int) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    )
                },
                actions = {
                    Spacer(modifier = Modifier.width(16.dp))

                    SearchBar(
                        inputField = {
                            SearchBarDefaults.InputField(
                                query = query,
                                onQueryChange = { actions(BookListActions.UpdateSearchQuery(it.trim())) },
                                onSearch = { actions(BookListActions.SearchBooks) },
                                expanded = false,
                                onExpandedChange = {},
                                enabled = true,
                                placeholder = {
                                    Text(
                                        text = stringResource(id = R.string.search_books),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = null,
                                    )
                                },
                            )
                        },
                        shape = RoundedCornerShape(80.dp),
                        expanded = false,
                        onExpandedChange = {},
                        modifier =
                        Modifier
                            .wrapContentHeight()
                            .weight(1f),
                        content = {},
                    )
                    Spacer(modifier = Modifier.width(4.dp))

                    SortDropDown(
                        sortType = sortType,
                        onItemClick = { actions(BookListActions.SortBooks(it)) },
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                })
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(top = 24.dp)
        ) {
            if (books.isEmpty() && query.isBlank()) {
                Text(
                    text = stringResource(id = R.string.empty_text),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.align(Alignment.Center),
                )
            } else {
                if (books.isEmpty()) {
                    Text(
                        text = stringResource(id = R.string.empty_search_text),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                    )
                } else {
                    LazyColumn {
                        items(books) {
                            BookItem(
                                book = it,
                                onBookClicked = {
                                    actions(BookListActions.OpenBook(it.id))
                                },
                                onDeleteClicked = { showDeleteDialog(it.id) },
                            )
                        }
                    }
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
    }
}
