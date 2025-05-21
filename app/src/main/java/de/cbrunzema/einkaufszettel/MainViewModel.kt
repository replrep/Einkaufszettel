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

    fun load() {
        viewModelScope.launch {
            try {
                if (!Einkaufszettel.dataFile.canRead()) {
                    items.value = demoItemStore
                } else {
                    items.value = Json.decodeFromString<Set<ShoppingItem>>(
                        Einkaufszettel.dataFile.bufferedReader(Charsets.UTF_8).use(
                            BufferedReader::readText
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
        items.value = items.value.filterNot { x -> x.label == item.label }.plus(item).toSet()
    }

    fun deleteItem(item: ShoppingItem) {
        items.value = items.value.minus(item)
    }
}
