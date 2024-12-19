package com.vitoksmile.shoppinglistkmp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Checkbox
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vitoksmile.shoppinglistkmp.domain.model.Item
import kotlinx.collections.immutable.ImmutableList
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.format.char

@Composable
fun SuccessView(
    items: ImmutableList<Item>,
    onComplete: (Item) -> Unit,
    modifier: Modifier = Modifier,
) {
    val lazyListState = rememberLazyListState()
    LaunchedEffect(items.size) {
        lazyListState.animateScrollToItem(0)
    }

    LazyColumn(
        modifier = modifier,
        state = lazyListState,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(items = items, key = { it.createdAt.toString() }) { item ->
            ItemView(
                modifier = Modifier.fillMaxWidth(),
                item = item,
                onComplete = { onComplete(item) },
            )
        }
    }
}

@Composable
private fun ItemView(
    item: Item,
    onComplete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .clickable(enabled = !item.isCompleted) { onComplete() },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            checked = item.isCompleted,
            enabled = !item.isCompleted,
            onCheckedChange = { isChecked ->
                if (isChecked) onComplete()
            },
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                modifier = modifier,
                text = item.text,
                style = MaterialTheme.typography.body1,
            )

            Text(
                modifier = modifier,
                text = "Created at ${format(item.createdAt)}",
                style = MaterialTheme.typography.caption,
            )

            item.completedAt?.let { completedAt ->
                Text(
                    modifier = modifier,
                    text = "Completed at ${format(completedAt)}",
                    style = MaterialTheme.typography.caption,
                )
            }
        }
    }
}

@Composable
private fun format(instant: Instant): String = remember(instant) {
    val customFormat = DateTimeComponents.Format {
        hour();char(':');minute()
        char(' ')
        date(LocalDate.Formats.ISO)
    }
    instant.format(customFormat)
}
