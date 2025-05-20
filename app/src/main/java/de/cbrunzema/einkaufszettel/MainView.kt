@file:OptIn(ExperimentalMaterial3Api::class)

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun MainView(modifier: Modifier = Modifier, snackbarLauncher: (String) -> Unit) {
    val leftScrollState = rememberScrollState()
    val rightScrollState = rememberScrollState()
    val mainViewModel: MainViewModel = viewModel()
    val items by mainViewModel.items
    val level by mainViewModel.level

    val openCreateDialog = remember { mutableStateOf(false) }
    val itemForEditDialog = remember { mutableStateOf<ShoppingItem?>(null) }

    Column(
        modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp)
    ) {
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
                    IconButton(onClick = { openCreateDialog.value = true }) {
                        Icon(Icons.Default.Add, stringResource(R.string.add))
                    }
                    IconButton(onClick = { Log.e("AAA", "Settings clicked") }) {
                        Icon(Icons.Default.Settings, stringResource(R.string.settings))
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
            title = stringResource(R.string.create),
            item = ShoppingItem("", Level.A, false, false),
            onDismissRequest = { openCreateDialog.value = false },
            onConfirmation = {
                openCreateDialog.value = false
                mainViewModel.addItem(it)
                snackbarLauncher(it.label + " " + Einkaufszettel.res.getString(R.string.created))
            })
    }

    if (itemForEditDialog.value != null) {
        EditDialog(
            title = stringResource(R.string.edit),
            item = itemForEditDialog.value!!,
            onDismissRequest = { itemForEditDialog.value = null },
            onConfirmation = {
                mainViewModel.deleteItem(itemForEditDialog.value!!)
                itemForEditDialog.value = null
                mainViewModel.addItem(it)
                snackbarLauncher(it.label + " " + Einkaufszettel.res.getString(R.string.updated))
            },
            onDeleteRequest = {
                val deletedLabel = itemForEditDialog.value!!.label
                mainViewModel.deleteItem(itemForEditDialog.value!!)
                itemForEditDialog.value = null
                snackbarLauncher(deletedLabel + " " + Einkaufszettel.res.getString(R.string.deleted))
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
    val cachedItems = remember(items, level) {
        items.filter { !it.selected && it.level == level }.sortedBy { it.label }
    }

    for (item in cachedItems) {
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
    val cachedItems = remember(items) { items.filter { it.selected }.sortedBy { it.label } }

    for (item in cachedItems) {
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
