package com.vitoksmile.shoppinglistkmp

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vitoksmile.shoppinglistkmp.data.RepositoryImpl
import com.vitoksmile.shoppinglistkmp.domain.Repository
import com.vitoksmile.shoppinglistkmp.domain.model.Item
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class MainViewModel : ViewModel() {

    private val repository: Repository = RepositoryImpl()

    val uiState: StateFlow<MainUiState> =
        repository.observe()
            .map<List<Item>, MainUiState> { items ->
                MainUiState.Success(items = items.toImmutableList())
            }
            .catch { error ->
                emit(MainUiState.Error(reason = error.toString()))
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = MainUiState.Loading,
            )
}

@Immutable
sealed interface MainUiState {

    data object Loading : MainUiState

    data class Success(val items: ImmutableList<Item>) : MainUiState

    data class Error(val reason: String) : MainUiState
}
