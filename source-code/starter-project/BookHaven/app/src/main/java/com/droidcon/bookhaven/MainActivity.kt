package com.droidcon.bookhaven

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.droidcon.bookhaven.ui.navigation.BookHavenNavHost
import com.droidcon.bookhaven.ui.theme.BookHavenTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BookHavenTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background,
                    modifier = Modifier.safeDrawingPadding(),
                ) {
                    BookHavenNavHost()
                }
            }
        }
    }
}
