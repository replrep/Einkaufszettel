package de.cbrunzema.einkaufszettel

enum class Level { A, B }

data class ShoppingItem(
    val label: String, val level: Level, val singleUse: Boolean, val selected: Boolean
)

val itemStoreDefault: Set<ShoppingItem> = (1..10).map {
    if (it < 5) {
        ShoppingItem("nameA $it", Level.A, false, false)
    } else {
        ShoppingItem("nameB $it", Level.B, false, false)
    }
}.toSet()

sealed interface UiEvent {
    data class Select(val item: ShoppingItem) : UiEvent
    data class Unselect(val item: ShoppingItem) : UiEvent
    object LevelA : UiEvent
    object LevelB : UiEvent
}
