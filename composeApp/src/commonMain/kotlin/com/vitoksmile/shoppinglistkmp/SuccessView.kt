package com.vitoksmile.shoppinglistkmp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Checkbox
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vitoksmile.shoppinglistkmp.domain.model.Item
import kotlinx.collections.immutable.ImmutableList

@Composable
fun SuccessView(
    items: ImmutableList<Item>,
    onComplete: (Item) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
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

            item.completedAt?.let { completedAt ->
                Text(
                    modifier = modifier,
                    text = completedAt.toString(),
                    style = MaterialTheme.typography.caption,
                )
            }
        }
    }
}
