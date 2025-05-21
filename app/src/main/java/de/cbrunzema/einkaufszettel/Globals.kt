package de.cbrunzema.einkaufszettel

import kotlinx.serialization.Serializable

const val sourceRepoUrl = "https://github.com/replrep/Einkaufszettel"

const val storageFileName = "einkaufszettel.json"

enum class Level { A, B }

@Serializable
data class ShoppingItem(
    val label: String, val level: Level, val singleUse: Boolean, val selected: Boolean
)

val demoItemStore: Set<ShoppingItem> = setOf(
    ShoppingItem("Demo item 1", Level.A, false, false),
    ShoppingItem("Demo item 2", Level.A, false, false)
)
