package com.vitoksmile.shoppinglistkmp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Stable
sealed interface AddItemState {

    fun show()

    fun hide()
}

@Stable
private class AddItemStateImpl(
    val onAdd: (text: String) -> Unit,
) : AddItemState {

    var isShowing: Boolean by mutableStateOf(false)
    var text: String by mutableStateOf("")

    override fun show() {
        isShowing = true
    }

    override fun hide() {
        isShowing = false
        text = ""
    }
}

@Composable
fun rememberAddItemState(
    onAdd: (text: String) -> Unit,
): AddItemState = remember { AddItemStateImpl(onAdd) }

@Composable
fun AddItemView(state: AddItemState) {
    require(state is AddItemStateImpl)

    val focusRequester = remember { FocusRequester() }

    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true,
    )
    LaunchedEffect(sheetState.currentValue) {
        if (sheetState.currentValue == ModalBottomSheetValue.Hidden) {
            state.hide()
        }
    }
    LaunchedEffect(state.isShowing) {
        if (state.isShowing) {
            sheetState.show()
            focusRequester.requestFocus()
        } else {
            sheetState.hide()
        }
    }

    val isButtonEnabled by remember {
        derivedStateOf {
            state.text.isNotBlank()
        }
    }

    ModalBottomSheetLayout(
        sheetState = sheetState,
        content = {},
        sheetContent = {
            Column(
                modifier = Modifier.fillMaxWidth(),
            ) {
                TopAppBar(
                    title = {
                        Text("Add new item")
                    },
                    navigationIcon = {
                        IconButton(onClick = state::hide) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = null,
                            )
                        }
                    }
                )

                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth()
                            .focusRequester(focusRequester),
                        value = state.text,
                        onValueChange = { state.text = it },
                    )

                    OutlinedButton(
                        enabled = isButtonEnabled,
                        onClick = {
                            state.onAdd(state.text)
                            state.hide()
                        }
                    ) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = "Add",
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
        },
    )
}
