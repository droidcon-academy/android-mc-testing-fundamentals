package com.droidcon.bookhaven.ui.book.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.droidcon.bookhaven.data.model.SortType
import com.droidcon.bookhaven.data.usecase.DeleteBook
import com.droidcon.bookhaven.data.usecase.GetBooks
import com.droidcon.bookhaven.data.usecase.SearchBooks
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BooksViewModel
    @Inject
    constructor(
        private val getBooks: GetBooks,
        private val deleteBook: DeleteBook,
        private val searchBooks: SearchBooks,
    ) : ViewModel() {
        private val _state = MutableStateFlow(BookListState())
        val state = _state.asStateFlow()

        val userActions = Channel<BookListActions>(Channel.UNLIMITED)

        private val _bookListEffect = MutableStateFlow<BookListEffect>(BookListEffect.None)
        val bookListEffect = _bookListEffect.asStateFlow()

        init {
            handleAction()
        }

        private fun handleAction() {
            viewModelScope.launch {
                userActions.consumeEach { action ->
                    when (action) {
                        is BookListActions.DeleteBook -> deleteBook(action.id)
                        BookListActions.GetBooks -> getBooks()
                        is BookListActions.SortBooks -> updateSortLogic(action.sortType)
                        is BookListActions.CreateBook -> _bookListEffect.emit(BookListEffect.CreateBook)
                        is BookListActions.OpenBook ->
                            _bookListEffect.emit(
                                BookListEffect.OpenBook(
                                    action.id,
                                ),
                            )

                        BookListActions.SearchBooks -> searchBooks()
                        is BookListActions.UpdateSearchQuery -> updateSearchQuery(action.query)
                    }
                }
            }
        }

        private fun updateSearchQuery(query: String) {
            if(query.isBlank()){
                getBooks()
            }
            _state.value = state.value.copy(searchQuery = query)
        }

        private fun searchBooks() {
            _state.value = state.value.copy(isLoading = true, isError = false)
            viewModelScope.launch {
                val books = searchBooks(state.value.searchQuery, state.value.sortType).firstOrNull()
                _state.value =
                    if (books != null) {
                        state.value.copy(books = books, isLoading = false)
                    } else {
                        state.value.copy(isError = true, isLoading = false)
                    }
            }
        }

        private fun deleteBook(id: Int) {
            viewModelScope.launch {
                deleteBook.invoke(id)
                _state.value = state.value.copy(books = state.value.books.filterNot { it.id == id })
            }
        }

        private fun updateSortLogic(sortType: SortType) {
            _state.value = state.value.copy(sortType = sortType, isLoading = true)
            getBooks(sortType)
        }

        private fun getBooks(order: SortType = state.value.sortType) {
            if (state.value.searchQuery.isNotBlank()) {
                searchBooks()
            } else {
                _state.value = state.value.copy(isLoading = true, isError = false)
                viewModelScope.launch {
                    val books = getBooks.invoke(order).firstOrNull()

                    _state.value =
                        if (books != null) {
                            state.value.copy(books = books, isLoading = false)
                        } else {
                            state.value.copy(isError = true, isLoading = false)
                        }
                }
            }
        }

        suspend fun onEffectHandled() {
            if (bookListEffect.value != BookListEffect.None) {
                _bookListEffect.emit(BookListEffect.None)
            }
        }
    }
