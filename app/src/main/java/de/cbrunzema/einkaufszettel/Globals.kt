package de.cbrunzema.einkaufszettel

import kotlinx.serialization.Serializable

const val sourceRepoUrl = "https://github.com/replrep/Einkaufszettel"

const val storageFileName = "einkaufszettel.json"

enum class Level { A, B }

@Serializable
data class ShoppingItem(
    val level: Level,
    val label: String = "",
    val singleUse: Boolean = false,
    val selected: Boolean = false,
    val unselectedSortIndex: Int = 0,
    val selectedSortIndex: Int = 0
)

val demoItemStore: Set<ShoppingItem> = setOf(
    ShoppingItem( Level.A,"Demo item 1",),
    ShoppingItem( Level.A,"Demo item 2"),
    ShoppingItem( Level.A,"Demo item 3"),
    ShoppingItem( Level.A,"Demo item 4"),
)
