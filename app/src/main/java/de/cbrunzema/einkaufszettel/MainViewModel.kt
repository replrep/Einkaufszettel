package de.cbrunzema.einkaufszettel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainViewModel : ViewModel() {
    private val _items = MutableStateFlow(itemStoreDefault)
    val items = _items.asStateFlow()
    val level = mutableStateOf(Level.A)

    fun onEvent(event: UiEvent) {
        when (event) {
            is UiEvent.Select -> {
                _items.update { it.minus(event.item).plus(event.item.copy(selected = true)) }
            }

            is UiEvent.Unselect -> {
                _items.update { it.minus(event.item).plus(event.item.copy(selected = false)) }
            }

            is UiEvent.LevelA -> {
                level.value = Level.A
            }

            is UiEvent.LevelB -> {
                level.value = Level.B
            }
        }
    }

    fun addItem(item: ShoppingItem) {
        _items.update { it.plus(item) }
    }
}
