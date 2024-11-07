package com.droidcon.bookhaven.ui.book.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.droidcon.bookhaven.data.model.Book
import com.droidcon.bookhaven.data.usecase.DeleteBook
import com.droidcon.bookhaven.data.usecase.GetBookById
import com.droidcon.bookhaven.data.usecase.SaveBook
import com.droidcon.bookhaven.util.isValid
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookDetailViewmodel
    @Inject
    constructor(
        private val saveBookUseCase: SaveBook,
        private val deleteBookUseCase: DeleteBook,
        private val getBookById: GetBookById,
    ) : ViewModel() {
        private val _state = MutableStateFlow(BookDetailState(isLoading = true))
        val state = _state.asStateFlow()

        val userActions = Channel<BookDetailActions>(UNLIMITED)

        private val _effect = MutableStateFlow<BookDetailEffect>(BookDetailEffect.None)
        val effect = _effect.asStateFlow()

        init {
            handleAction()
        }

        private fun handleAction() {
            viewModelScope.launch {
                userActions.consumeEach { action ->
                    when (action) {
                        is BookDetailActions.LoadBook -> loadBook(action.bookId)
                        is BookDetailActions.DeleteBook -> deleteBook(action.id)
                        is BookDetailActions.SaveBook -> saveBook(action.book)
                        is BookDetailActions.UpdateBook -> saveBook(action.book)
                        BookDetailActions.Back -> { _effect.emit(BookDetailEffect.NavigateBack) }
                    }
                }
            }
        }

        private fun loadBook(bookId: Int) {
            if (bookId != 0) {
                viewModelScope.launch {
                    val book = getBookById(bookId)
                    _state.value = state.value.copy(isLoading = false, book = book)
                }
            } else {
                _state.value = state.value.copy(isLoading = false)
            }
        }

        private fun saveBook(book: Book) {
            _state.value = state.value.copy(isLoading = true)
            viewModelScope.launch {
                if (book.isValid()) {
                    saveBookUseCase(book)
                    _state.value = state.value.copy(isLoading = false, book = book)
                    _effect.emit(BookDetailEffect.NavigateBack)
                } else {
                    _state.value = _state.value.copy(isLoading = false)
                    _effect.emit(BookDetailEffect.ShowInvalidBookToast)
                }
            }
        }

        private fun deleteBook(bookId: Int) {
            _state.value = _state.value.copy(isLoading = true)
            viewModelScope.launch {
                deleteBookUseCase(bookId)
                _state.value = _state.value.copy(isLoading = false)
                _effect.emit(BookDetailEffect.NavigateBack)
            }
        }

        suspend fun onEffectHandled() {
            if (effect.value != BookDetailEffect.None) {
                _effect.emit(BookDetailEffect.None)
            }
        }
    }
