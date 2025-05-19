package de.cbrunzema.einkaufszettel

import android.util.Log
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun MainView(modifier: Modifier = Modifier) {
    val leftScrollState = rememberScrollState()
    val rightScrollState = rememberScrollState()
    val mainViewModel: MainViewModel = viewModel()
    val items by mainViewModel.items.collectAsStateWithLifecycle()
    val level by mainViewModel.level

    val openCreateDialog = remember { mutableStateOf(false) }
    val itemForEditDialog = remember { mutableStateOf<ShoppingItem?>(null) }

    Column(
        modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.weight(1.0f)
            ) {
                Row {
                    TextButton(onClick = { mainViewModel.setLevel(Level.A) }) {
                        Text(Level.A.name)
                    }
                    TextButton(onClick = { mainViewModel.setLevel(Level.B) }) {
                        Text(Level.B.name)
                    }
                }
            }
            Column(
                modifier = Modifier.weight(1.0f)
            ) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    IconButton(onClick = { openCreateDialog.value = true }) {
                        Icon(Icons.Default.Add, "TODO")
                    }
                    IconButton(onClick = { Log.e("AAA", "Settings clicked") }) {
                        Icon(Icons.Default.Settings, "TODO")
                    }
                }
            }
        }

        Row {
            Column(
                modifier = Modifier
                    .weight(1.0f)
                    .verticalScroll(leftScrollState)
            ) {
                LeftPanel(
                    items = items,
                    level = level,
                    onClick = { mainViewModel.select(it) },
                    onLongClick = {
                        itemForEditDialog.value = it
                    })
            }
            Column(
                modifier = Modifier
                    .weight(1.0f)
                    .verticalScroll(rightScrollState)
            ) {
                RightPanel(items = items, onClick = { mainViewModel.unselect(it) }, onLongClick = {
                    itemForEditDialog.value = it
                })

            }
        }
    }

    if (openCreateDialog.value) {
        EditDialog(
            title = "Create", //TODO
            item = ShoppingItem("", Level.A, false, false),
            onDismissRequest = { openCreateDialog.value = false },
            onConfirmation = {
                openCreateDialog.value = false
                mainViewModel.addItem(it)
            })
    }

    if (itemForEditDialog.value != null) {
        EditDialog(
            title = "Edit", //TODO
            item = itemForEditDialog.value!!,
            onDismissRequest = { itemForEditDialog.value = null },
            onConfirmation = {
                mainViewModel.deleteItem(itemForEditDialog.value!!)
                itemForEditDialog.value = null
                mainViewModel.addItem(it)
            },
            onDeleteRequest = {
                mainViewModel.deleteItem(itemForEditDialog.value!!)
                itemForEditDialog.value = null
            })
    }
}


@Composable
fun LeftPanel(
    items: Set<ShoppingItem>,
    level: Level,
    onClick: (ShoppingItem) -> Unit,
    onLongClick: (ShoppingItem) -> Unit
) {
    // TODO caching
    for (item in items.filter { !it.selected && it.level == level }.sortedBy { it.label }) {
        key(item.label) {
            Text(
                item.label,
                style = MaterialTheme.typography.titleLarge,
                lineHeight = MaterialTheme.typography.titleLarge.lineHeight.times(2),
                modifier = Modifier.combinedClickable(
                    onClick = { onClick(item) },
                    onLongClick = { onLongClick(item) })
            )
        }
    }
}

@Composable
fun RightPanel(
    items: Set<ShoppingItem>, onClick: (ShoppingItem) -> Unit, onLongClick: (ShoppingItem) -> Unit
) {
    // TODO caching
    for (item in items.filter { it.selected }.sortedBy { it.label }) {
        key(item.label) {
            Text(
                item.label,
                style = MaterialTheme.typography.titleLarge,
                lineHeight = MaterialTheme.typography.titleLarge.lineHeight.times(2),
                fontStyle = if (item.singleUse) {
                    FontStyle.Italic
                } else {
                    FontStyle.Normal
                },
                modifier = Modifier.combinedClickable(
                    onClick = { onClick(item) },
                    onLongClick = { onLongClick(item) })
            )
        }
    }
}
