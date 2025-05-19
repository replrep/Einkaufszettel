package de.cbrunzema.einkaufszettel

import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

val fontSize = 30.sp
val lineHeight = 54.sp
val padding = 10.dp

@Composable
fun MainView(modifier: Modifier = Modifier) {
    val leftScrollState = rememberScrollState()
    val rightScrollState = rememberScrollState()
    val mainViewModel: MainViewModel = viewModel()
    val items by mainViewModel.items.collectAsStateWithLifecycle()
    val level by mainViewModel.level

    val openCreateDialog = remember { mutableStateOf(false) }

    Column(
        modifier
            .fillMaxSize()
            .padding(horizontal = padding)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.weight(1.0f)
            ) {
                Row {
                    TextButton(onClick = { mainViewModel.onEvent(UiEvent.LevelA) }) {
                        Text(Level.A.name)
                    }
                    TextButton(onClick = { mainViewModel.onEvent(UiEvent.LevelB) }) {
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
                    IconButton(onClick = { mainViewModel.onEvent(UiEvent.LevelB) }) {
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
                LeftPanel(items, level, mainViewModel::onEvent)
            }
            Column(
                modifier = Modifier
                    .weight(1.0f)
                    .verticalScroll(rightScrollState)
            ) {
                RightPanel(items, mainViewModel::onEvent)
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
}


@Composable
fun LeftPanel(items: Set<ShoppingItem>, level: Level, onEvent: (UiEvent) -> Unit) {
    // TODO caching
    for (item in items.filter { !it.selected && it.level == level }.sortedBy { it.label }) {
        key(item.label) {
            Text(
                item.label,
                fontSize = fontSize,
                lineHeight = lineHeight,
                modifier = Modifier.clickable { onEvent(UiEvent.Select(item)) })
        }
    }
}

@Composable
fun RightPanel(items: Set<ShoppingItem>, onEvent: (UiEvent) -> Unit) {
    // TODO caching
    for (item in items.filter { it.selected }.sortedBy { it.label }) {
        key(item.label) {
            Text(
                item.label,
                fontSize = fontSize,
                lineHeight = lineHeight,
                modifier = Modifier.clickable { onEvent(UiEvent.Unselect(item)) })
        }
    }
}
