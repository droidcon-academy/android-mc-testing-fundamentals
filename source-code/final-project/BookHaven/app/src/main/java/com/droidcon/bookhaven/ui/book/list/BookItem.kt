package com.droidcon.bookhaven.ui.book.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.droidcon.bookhaven.R
import com.droidcon.bookhaven.data.model.Book

@Composable
fun BookItem(
    book: Book,
    modifier: Modifier = Modifier,
    onDeleteClicked: () -> Unit,
    onBookClicked: () -> Unit,
) {
    Box {
        Column(
            modifier =
                modifier
                    .clickable { onBookClicked() }
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = book.title,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "By ${book.author}",
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.height(12.dp))

            HorizontalDivider(
                thickness = 1.dp,
            )
        }

        IconButton(
            onClick = { onDeleteClicked() },
            modifier =
                Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterEnd),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_delete_forever_24),
                tint = MaterialTheme.colorScheme.surfaceTint,
                contentDescription =
                    stringResource(
                        id = R.string.delete,
                    ),
            )
        }
    }
}