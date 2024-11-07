package com.droidcon.bookhaven.ui.book.detail

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.droidcon.bookhaven.R
import com.droidcon.bookhaven.data.model.Book
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
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
    var showDatePicker by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = { actions(BookDetailActions.Back) }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.back)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { actions(BookDetailActions.DeleteBook(bookId)) }) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = stringResource(id = R.string.delete)
                        )
                    }
                }
            )
        },
        content = { paddingValues ->

            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
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

                        Column(Modifier.padding(horizontal = 16.dp)) {
                            OutlinedTextField(
                                label = {
                                    Text(text = stringResource(id = R.string.title))
                                },
                                value = book.title,
                                onValueChange = { book = book.copy(title = it) },
                                placeholder = {
                                    Text(
                                        text = stringResource(id = R.string.title_hint),
                                        style = MaterialTheme.typography.bodySmall,
                                    )
                                },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                label = {
                                    Text(text = stringResource(id = R.string.author))
                                },
                                value = book.author,
                                onValueChange = { book = book.copy(author = it) },
                                placeholder = {
                                    Text(
                                        text = stringResource(id = R.string.author_hint),
                                        style = MaterialTheme.typography.bodySmall,
                                    )
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = book.year,
                                onValueChange = { },
                                label = { Text(text = stringResource(id = R.string.date)) },
                                readOnly = true,
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.DateRange,
                                        contentDescription = stringResource(id = R.string.select_date)
                                    )
                                },
                                modifier = Modifier.fillMaxWidth()
                                    .pointerInput(book.year) {
                                        awaitEachGesture {
                                            awaitFirstDown(pass = PointerEventPass.Initial)
                                            val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                                            if (upEvent != null) {
                                                showDatePicker = true
                                            }
                                        }
                                    }
                            )

                            if (showDatePicker) {
                                BookDatePickerDialog(
                                    onDateSelected = { book = book.copy(year = it) },
                                    onDismiss = { showDatePicker = false }
                                )
                            }


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
                                val currentDateTime = Date()
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
    )
}
