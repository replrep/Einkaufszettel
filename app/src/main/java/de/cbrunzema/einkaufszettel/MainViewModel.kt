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


    fun select(item: ShoppingItem) {
        _items.update { it.minus(item).plus(item.copy(selected = true)) }
    }

    fun unselect(item: ShoppingItem) {
        if (item.singleUse) {
            deleteItem(item)
        } else {
            _items.update { it.minus(item).plus(item.copy(selected = false)) }
        }
    }

    fun setLevel(newLevel: Level) {
        level.value = newLevel
    }

    fun addItem(item: ShoppingItem) {
        _items.update { it.plus(item) }
    }

    fun deleteItem(item: ShoppingItem) {
        _items.update { it.minus(item) }
    }
}
