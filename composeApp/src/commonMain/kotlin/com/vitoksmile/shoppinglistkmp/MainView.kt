package com.vitoksmile.shoppinglistkmp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Checkbox
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vitoksmile.shoppinglistkmp.domain.model.Item
import kotlinx.collections.immutable.ImmutableList

@Composable
fun MainView() {
    val viewModel: MainViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MainView(
        events = viewModel,
        state = uiState,
    )
}

@Composable
private fun MainView(
    events: MainUiEvents,
    state: MainUiState,
) {
    Scaffold {
        when (state) {
            is MainUiState.Loading -> {
                LoadingView()
            }

            is MainUiState.Error -> {
                ErrorView(
                    modifier = Modifier.fillMaxSize(),
                    error = state.reason,
                )
            }

            is MainUiState.Success -> {
                SuccessView(
                    modifier = Modifier.fillMaxSize(),
                    items = state.items,
                    onComplete = {
                        events.completeItem(it)
                    },
                )
            }
        }
    }
}

@Composable
private fun LoadingView(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorView(
    error: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Failed to load items",
            style = MaterialTheme.typography.body1,
        )
        Text(
            text = "Reason $error",
            style = MaterialTheme.typography.body2,
        )
    }
}

@Composable
private fun SuccessView(
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
