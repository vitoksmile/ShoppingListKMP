package com.vitoksmile.shoppinglistkmp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

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

