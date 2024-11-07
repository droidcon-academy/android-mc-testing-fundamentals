package com.droidcon.bookhaven.ui.book.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.droidcon.bookhaven.R
import com.droidcon.bookhaven.data.model.SortType

@Composable
fun SortDropDown(
    sortType: SortType,
    onItemClick: (SortType) -> Unit,
    modifier: Modifier = Modifier,
) {
    val sortItems = listOf(SortType.DATE_ADDED, SortType.NAME)

    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = modifier.clickable { expanded = !expanded },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_sort_24),
            contentDescription = stringResource(id = R.string.sort_by)
        )
        Spacer(modifier = Modifier.width(2.dp))
        Text(
            text = sortType.value,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            sortItems.forEach { item ->
                DropdownMenuItem(
                    text = { Text(text = item.value) },
                    onClick = { onItemClick(item) },
                )
            }
        }
    }
}