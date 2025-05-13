package de.cbrunzema.einkaufszettel

data class ShoppingItem(val label: String, val selected: Boolean, val singleUse: Boolean)

val itemStoreDefault: Set<ShoppingItem> =
    (1..5).map { ShoppingItem("name $it", false, false) }.toSet()

sealed interface UiEvent {
    data class Select(val item: ShoppingItem) : UiEvent
    data class Unselect(val item: ShoppingItem) : UiEvent
}
