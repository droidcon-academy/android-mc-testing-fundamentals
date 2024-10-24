package com.droidcon.bookhaven.ui.book.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.droidcon.bookhaven.R
import com.droidcon.bookhaven.data.model.Book

@Composable
fun BookDetailContent(
    state: BookDetailState,
    bookId: Int,
    modifier: Modifier = Modifier,
    actions: (BookDetailActions) -> Unit,
) {
    LaunchedEffect(bookId) {
        actions(BookDetailActions.LoadBook(bookId))
    }

    Box(modifier = modifier.fillMaxSize()) {
        when {
            state.isLoading -> {
                CircularProgressIndicator(
                    modifier =
                        Modifier
                            .width(64.dp)
                            .align(Alignment.Center),
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
                var book by remember {
                    mutableStateOf(
                        state.book ?: Book(
                            id = bookId,
                            title = state.book?.title ?: "",
                            author = state.book?.author ?: "",
                            year = state.book?.year ?: "",
                            notes = state.book?.notes ?: "",
                            timestamp = state.book?.timestamp ?: 0L,
                        ),
                    )
                }

                Column(modifier = modifier.padding(16.dp)) {
                    TextButton(onClick = {
                        actions(BookDetailActions.DeleteBook(bookId))
                    }, modifier = Modifier.align(Alignment.End)) {
                        Text(text = stringResource(id = R.string.delete))
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = stringResource(id = R.string.title),
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        TextField(
                            value = book.title,
                            onValueChange = { book = book.copy(title = it) },
                            placeholder = {
                                Text(
                                    text = stringResource(id = R.string.title_hint),
                                    style = MaterialTheme.typography.bodySmall,
                                )
                            },
                            colors =
                                TextFieldDefaults.colors().copy(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                ),
                            textStyle = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        )
                    }
                    HorizontalDivider()

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = stringResource(id = R.string.author),
                            style = MaterialTheme.typography.titleMedium,
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        TextField(
                            value = book.author,
                            onValueChange = { book = book.copy(author = it) },
                            placeholder = {
                                Text(
                                    text = stringResource(id = R.string.author_hint),
                                    style = MaterialTheme.typography.bodySmall,
                                )
                            },
                            colors =
                                TextFieldDefaults.colors().copy(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                ),
                            textStyle = MaterialTheme.typography.titleMedium,
                        )
                    }
                    HorizontalDivider()

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = stringResource(id = R.string.date),
                            style = MaterialTheme.typography.titleMedium,
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        TextField(
                            value = book.year,
                            onValueChange = { book = book.copy(year = it) },
                            placeholder = {
                                Text(
                                    text = stringResource(id = R.string.date_hint),
                                    style = MaterialTheme.typography.bodySmall,
                                )
                            },
                            colors =
                                TextFieldDefaults.colors().copy(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                ),
                            textStyle = MaterialTheme.typography.titleMedium,
                        )
                    }
                    HorizontalDivider()

                    Spacer(modifier = Modifier.height(24.dp))

                    TextField(
                        value = book.notes,
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(16.dp),
                        onValueChange = { book = book.copy(notes = it) },
                        placeholder = {
                            Text(
                                text = stringResource(id = R.string.notes_hint),
                                style = MaterialTheme.typography.bodySmall,
                            )
                        },
                        colors =
                            TextFieldDefaults.colors().copy(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                            ),
                        textStyle = MaterialTheme.typography.bodySmall,
                    )
                }

                FloatingActionButton(
                    modifier =
                        Modifier
                            .padding(16.dp)
                            .align(Alignment.BottomEnd),
                    onClick = {
                        val currentDateTime: java.util.Date = java.util.Date()
                        book = book.copy(timestamp = currentDateTime.time)
                        actions(
                            if (bookId != 0) {
                                BookDetailActions.UpdateBook(book)
                            } else {
                                BookDetailActions.SaveBook(book)
                            },
                        )
                    },
                    shape = CircleShape,
                    containerColor = Color.White,
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_save_24),
                        tint = MaterialTheme.colorScheme.surfaceTint,
                        contentDescription = stringResource(id = R.string.save),
                    )
                }
            }
        }
    }
}
