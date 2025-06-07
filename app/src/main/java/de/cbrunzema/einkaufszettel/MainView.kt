@file:OptIn(ExperimentalMaterial3Api::class)

package de.cbrunzema.einkaufszettel

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import sh.calvin.reorderable.ReorderableColumn

@Composable
fun MainView(
    mainViewModel: MainViewModel, modifier: Modifier, snackbarLauncher: (String) -> Unit
) {
    val ctx = LocalContext.current
    val level by mainViewModel.level

    var openCreateDialog by remember { mutableStateOf(false) }
    var openInfoDialog by remember { mutableStateOf(false) }
    var itemForEditDialog by remember { mutableStateOf<ShoppingItem?>(null) }

    Column(
        modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp)
    ) {
        TopRow(
            mainViewModel,
            onCreateClick = { openCreateDialog = true },
            onInfoClick = { openInfoDialog = true })
        UnselectedAndSelectedLists(mainViewModel, onLongClick = {
            itemForEditDialog = it
        })
    }

    if (openCreateDialog) {
        EditDialog(
            title = stringResource(R.string.create),
            item = ShoppingItem(level),
            onDismissRequest = { openCreateDialog = false },
            onConfirmation = {
                openCreateDialog = false
                mainViewModel.addItem(it)
                mainViewModel.setLevel(it.level)
                snackbarLauncher(it.label + " " + ctx.getString(R.string.created))
            })
    }

    if (openInfoDialog) {
        InfoDialog(
            onDismissRequest = { openInfoDialog = false },
        )
    }

    if (itemForEditDialog != null) {
        EditDialog(
            title = stringResource(R.string.edit),
            item = itemForEditDialog!!,
            onDismissRequest = { itemForEditDialog = null },
            onConfirmation = {
                mainViewModel.deleteItem(itemForEditDialog!!)
                itemForEditDialog = null
                mainViewModel.addItem(it)
                mainViewModel.setLevel(it.level)
                snackbarLauncher(it.label + " " + ctx.getString(R.string.updated))
            },
            onDeleteRequest = {
                val deletedLabel = itemForEditDialog!!.label
                mainViewModel.deleteItem(itemForEditDialog!!)
                itemForEditDialog = null
                snackbarLauncher(deletedLabel + " " + ctx.getString(R.string.deleted))
            })
    }
}

@Composable
fun TopRow(
    mainViewModel: MainViewModel, onCreateClick: () -> Unit, onInfoClick: () -> Unit
) {
    val level by mainViewModel.level

    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Column(
            modifier = Modifier.weight(1.0f)
        ) {
            PrimaryTabRow(selectedTabIndex = level.ordinal, indicator = {
                TabRowDefaults.PrimaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(
                        level.ordinal, matchContentSize = false
                    ), width = Dp.Unspecified, color = MaterialTheme.colorScheme.onSurface
                )
            }) {
                Level.entries.forEachIndexed { index, currentLevel ->
                    Tab(
                        selected = level == currentLevel,
                        onClick = { mainViewModel.setLevel(currentLevel) },
                        selectedContentColor = Color.Unspecified
                    ) {
                        Text(
                            currentLevel.name, style = MaterialTheme.typography.titleLarge
                        )
                    }
                }
            }
        }
        Column(
            modifier = Modifier.weight(1.0f)
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                IconButton(onClick = onCreateClick) {
                    Icon(Icons.Default.Add, stringResource(R.string.add))
                }
                IconButton(onClick = onInfoClick) {
                    Icon(Icons.Default.Info, stringResource(R.string.info))
                }
            }
        }
    }
}

@Composable
fun UnselectedAndSelectedLists(
    mainViewModel: MainViewModel, onLongClick: (ShoppingItem) -> Unit
) {
    val leftScrollState = rememberScrollState()
    val rightScrollState = rememberScrollState()
    val items by mainViewModel.items
    val level by mainViewModel.level

    val unselectedList = remember(items, level) {
        items.filter { !it.selected && it.level == level }.sortedBy { it.unselectedSortIndex }
    }
    val selectedList = remember(items) {
        items.filter { it.selected }.sortedBy { it.selectedSortIndex }
    }

    Row {
        ReorderableColumn(
            onSettle = { fromIndex, toIndex ->
                val fromItem = unselectedList[fromIndex]
                val toItem = unselectedList[toIndex]
                if (toIndex == 0) {
                    mainViewModel.replaceItem(fromItem, fromItem.copy(unselectedSortIndex = 0))
                } else if (toIndex == unselectedList.size - 1) {
                    mainViewModel.replaceItem(
                        fromItem, fromItem.copy(unselectedSortIndex = Int.MAX_VALUE)
                    )
                } else if (toIndex == fromIndex + 1) {
                    mainViewModel.replaceItem(
                        fromItem,
                        fromItem.copy(unselectedSortIndex = toItem.unselectedSortIndex + 1)
                    )
                } else {
                    mainViewModel.replaceItem(
                        fromItem,
                        fromItem.copy(unselectedSortIndex = toItem.unselectedSortIndex - 1)
                    )
                }
            },
            list = unselectedList,
            modifier = Modifier
                .weight(1.0f)
                .verticalScroll(leftScrollState)
        ) { _, item, _ ->
            key(item.label) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        item.label,
                        style = MaterialTheme.typography.titleLarge,
                        lineHeight = MaterialTheme.typography.titleLarge.lineHeight.times(1.5),
                        modifier = Modifier.combinedClickable(
                            onClick = { mainViewModel.select(item) },
                            onLongClick = { onLongClick(item) })
                    )
                    Spacer(Modifier.weight(1.0f))
                    TextButton(
                        modifier = Modifier
                            .draggableHandle()
                            .requiredWidth(32.dp), onClick = {}) {
                        Text("\u2a75")
                    }
                }
            }
        }
        Spacer(Modifier.width(8.dp))
        ReorderableColumn(
            onSettle = { fromIndex, toIndex ->
                val fromItem = selectedList[fromIndex]
                val toItem = selectedList[toIndex]
                if (toIndex == 0) {
                    mainViewModel.replaceItem(fromItem, fromItem.copy(selectedSortIndex = 0))
                } else if (toIndex == selectedList.size - 1) {
                    mainViewModel.replaceItem(
                        fromItem, fromItem.copy(selectedSortIndex = Int.MAX_VALUE)
                    )
                } else if (toIndex == fromIndex + 1) {
                    mainViewModel.replaceItem(
                        fromItem, fromItem.copy(selectedSortIndex = toItem.selectedSortIndex + 1)
                    )
                } else {
                    mainViewModel.replaceItem(
                        fromItem, fromItem.copy(selectedSortIndex = toItem.selectedSortIndex - 1)
                    )
                }
            },
            list = selectedList,
            modifier = Modifier
                .weight(1.0f)
                .verticalScroll(rightScrollState)
        ) { _, item, _ ->
            key(item.label) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        item.label,
                        style = MaterialTheme.typography.titleLarge,
                        lineHeight = MaterialTheme.typography.titleLarge.lineHeight.times(1.5),
                        fontStyle = if (item.singleUse) {
                            FontStyle.Italic
                        } else {
                            FontStyle.Normal
                        },
                        modifier = Modifier.combinedClickable(
                            onClick = {
                            mainViewModel.unselect(item)
                        },
                            onLongClick = { onLongClick(item) })
                    )
                    Spacer(Modifier.weight(1.0f))
                    TextButton(
                        modifier = Modifier
                            .draggableHandle()
                            .requiredWidth(32.dp), onClick = {},
                    ) {
                        Text("\u2a75")
                    }
                }
            }
        }
    }
}
