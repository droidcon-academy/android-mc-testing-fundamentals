package com.droidcon.bookhaven.ui.book.detail

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.droidcon.bookhaven.R
import kotlinx.coroutines.launch

@Composable
fun BookDetailScreen(
    bookId: Int,
    viewmodel: BookDetailViewmodel,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()
    val state by viewmodel.state.collectAsStateWithLifecycle()
    val effect by viewmodel.effect.collectAsStateWithLifecycle(initialValue = BookDetailEffect.None)
    val context = LocalContext.current
    LaunchedEffect(effect) {
        when (effect) {
            BookDetailEffect.NavigateBack -> {
                navigateBack()
                viewmodel.onEffectHandled()
            }

            BookDetailEffect.ShowInvalidBookToast -> {
                Toast
                    .makeText(
                        context,
                        R.string.invalid_book_prompt,
                        Toast.LENGTH_SHORT,
                    ).show()
                viewmodel.onEffectHandled()
            }

            BookDetailEffect.None -> Unit
        }
    }

    BookDetailContent(
        bookId = bookId,
        state = state,
        modifier = modifier,
    ) { action ->
        coroutineScope.launch { viewmodel.userActions.send(action) }
    }
}
