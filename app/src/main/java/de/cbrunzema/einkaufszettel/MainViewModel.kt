package de.cbrunzema.einkaufszettel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    val items = mutableStateOf(itemStoreDefault)
    val level = mutableStateOf(Level.A)

    fun select(item: ShoppingItem) {
        items.value = items.value.minus(item).plus(item.copy(selected = true))
    }

    fun unselect(item: ShoppingItem) {
        if (item.singleUse) {
            deleteItem(item)
        } else {
            items.value = items.value.minus(item).plus(item.copy(selected = false))
        }
    }

    fun setLevel(newLevel: Level) {
        level.value = newLevel
    }

    fun addItem(item: ShoppingItem) {
        items.value = items.value.filterNot { x -> x.label == item.label }.plus(item).toSet()
    }

    fun deleteItem(item: ShoppingItem) {
        items.value = items.value.minus(item)
    }
}
