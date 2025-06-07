package de.cbrunzema.einkaufszettel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.io.BufferedReader

class MainViewModel : ViewModel() {
    val items = mutableStateOf<Set<ShoppingItem>>(setOf())
    val level = mutableStateOf(Level.A)

    fun cleanupSorting(items: Collection<ShoppingItem>): Set<ShoppingItem> {
        return items.sortedWith(compareBy<ShoppingItem> { it.unselectedSortIndex }.thenBy { it.label })
            .mapIndexed { i, item -> item.copy(unselectedSortIndex = (i + 1) * 10) }
            .sortedWith(compareBy<ShoppingItem> { it.selectedSortIndex }.thenBy { it.label })
            .mapIndexed { i, item -> item.copy(selectedSortIndex = (i + 1) * 10) }.toSet()
    }

    fun load() {
        viewModelScope.launch {
            try {
                if (!Einkaufszettel.dataFile.canRead()) {
                    items.value = cleanupSorting(demoItemStore)
                } else {
                    items.value = cleanupSorting(
                        Json.decodeFromString<Set<ShoppingItem>>(
                            Einkaufszettel.dataFile.bufferedReader(Charsets.UTF_8).use(
                                BufferedReader::readText
                            )
                        )
                    )
                }
            } catch (e: Exception) {
                Log.e("Einkaufszettel I/O", "load error", e)
            }
        }
    }

    fun save() {
        viewModelScope.launch {
            try {
                Einkaufszettel.dataFile.bufferedWriter(Charsets.UTF_8).use {
                    it.write(Json.encodeToString(items.value))
                }
            } catch (e: Exception) {
                Log.e("Einkaufszettel I/O", "save error", e)
            }
        }
    }

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
        items.value =
            cleanupSorting(items.value.filterNot { x -> x.label == item.label }.plus(item))
    }

    fun deleteItem(item: ShoppingItem) {
        items.value = items.value.minus(item)
    }

    fun replaceItem(oldItem: ShoppingItem, newItem: ShoppingItem) {
        items.value = cleanupSorting(items.value.minus(oldItem).plus(newItem))
    }
}
