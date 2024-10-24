package com.droidcon.bookhaven.ui.book.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch

@Composable
fun BookListScreen(
    viewmodel: BooksViewModel,
    navigateToBookDetail: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val state by viewmodel.state.collectAsStateWithLifecycle()
    val effect by viewmodel.bookListEffect.collectAsStateWithLifecycle(initialValue = BookListEffect.None)
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(effect) {
        when (val booksEffect = effect) {
            BookListEffect.CreateBook -> {
                navigateToBookDetail(0)
                viewmodel.onEffectHandled()
            }

            is BookListEffect.OpenBook -> {
                navigateToBookDetail(booksEffect.id)
                viewmodel.onEffectHandled()
            }

            BookListEffect.None -> Unit
        }
    }
    BookListContent(state, modifier = modifier) { action ->
        coroutineScope.launch {
            viewmodel.userActions.send(action)
        }
    }
}
