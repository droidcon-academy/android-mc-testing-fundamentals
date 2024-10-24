package com.droidcon.bookhaven.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.droidcon.bookhaven.ui.book.detail.BookDetailScreen
import com.droidcon.bookhaven.ui.book.list.BookListScreen

@Composable
fun BookHavenNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = BookList) {
        composable<BookList> {
            BookListScreen(navigateToBookDetail = { bookId ->
                navController.navigate(BookDetail(bookId))
            }, viewmodel = hiltViewModel())
        }

        composable<BookDetail> {
            val bookId = it.toRoute<BookDetail>().bookId

            BookDetailScreen(
                bookId = bookId,
                viewmodel = hiltViewModel(),
                navigateBack = {
                    navController.popBackStack()
                },
            )
        }
    }
}
