package de.cbrunzema.einkaufszettel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.json.Json
import java.io.BufferedReader

class MainViewModel : ViewModel() {
    val dataFileMutex = Mutex()
    val items = mutableStateOf<Set<ShoppingItem>>(setOf())
    val level = mutableStateOf(Level.A)

    fun renumberSortingIndices(items: Collection<ShoppingItem>): Set<ShoppingItem> {
        return items.sortedWith(compareBy<ShoppingItem> { it.unselectedSortIndex }.thenBy { it.label })
            .mapIndexed { i, item -> item.copy(unselectedSortIndex = (i + 1) * 10) }
            .sortedWith(compareBy<ShoppingItem> { it.selectedSortIndex }.thenBy { it.label })
            .mapIndexed { i, item -> item.copy(selectedSortIndex = (i + 1) * 10) }.toSet()
    }

    fun load() {
        viewModelScope.launch {
            try {
                dataFileMutex.withLock {
                    if (!Einkaufszettel.dataFile.canRead()) {
                        items.value = renumberSortingIndices(demoItemStore)
                    } else {
                        items.value = renumberSortingIndices(
                            Json.decodeFromString<Set<ShoppingItem>>(
                                Einkaufszettel.dataFile.bufferedReader(Charsets.UTF_8).use(
                                    BufferedReader::readText
                                )
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e("Einkaufszettel I/O", "load error", e)
            }
        }
    }

    fun save() {
        viewModelScope.launch {
            try {
                dataFileMutex.withLock {
                    Einkaufszettel.dataFile.bufferedWriter(Charsets.UTF_8).use {
                        it.write(Json.encodeToString(items.value))
                    }
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
            renumberSortingIndices(items.value.filterNot { x -> x.label == item.label }.plus(item))
    }

    fun deleteItem(item: ShoppingItem) {
        items.value = items.value.minus(item)
    }

    fun replaceItem(oldItem: ShoppingItem, newItem: ShoppingItem) {
        items.value = renumberSortingIndices(items.value.minus(oldItem).plus(newItem))
    }

    fun sortUnselected() {
        items.value = renumberSortingIndices(items.value.map { it.copy(unselectedSortIndex = 0) })
    }

    fun sortSelected() {
        items.value = renumberSortingIndices(items.value.map { it.copy(selectedSortIndex = 0) })
    }

    fun unselectAll() {
        items.value = items.value.map { it.copy(selected = false) }.toSet()
    }
}
