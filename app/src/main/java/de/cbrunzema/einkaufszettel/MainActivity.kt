package de.cbrunzema.einkaufszettel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import de.cbrunzema.einkaufszettel.ui.theme.EinkaufszettelTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

val fontSize = 30.sp
val lineHeight = 54.sp
val padding = 10.dp

data class ShoppingItem(val label: String, val selected: Boolean, val singleUse: Boolean)

val itemStoreDefault: Set<ShoppingItem> =
    (1..5).map { ShoppingItem("name $it", false, false) }.toSet()

sealed interface UiEvent {
    data class Select(val item: ShoppingItem) : UiEvent
    data class Unselect(val item: ShoppingItem) : UiEvent
}

class EinkaufszettelViewModel : ViewModel() {
    private val _items = MutableStateFlow(itemStoreDefault)
    val items = _items.asStateFlow()

    fun onEvent(event: UiEvent) {
        when (event) {
            is UiEvent.Select -> {
                _items.update { it.minus(event.item).plus(event.item.copy(selected = true)) }
            }

            is UiEvent.Unselect -> {
                _items.update { it.minus(event.item).plus(event.item.copy(selected = false)) }
            }
        }
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EinkaufszettelTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainView(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun LeftPanel(items: Set<ShoppingItem>, onEvent: (UiEvent) -> Unit) {
    for (item in items.filter { !it.selected }.sortedBy { it.label }) {
        Text(
            item.label,
            fontSize = fontSize,
            lineHeight = lineHeight,
            modifier = Modifier.clickable { onEvent(UiEvent.Select(item)) })
    }
}

@Composable
fun RightPanel(items: Set<ShoppingItem>, onEvent: (UiEvent) -> Unit) {
    for (item in items.filter { it.selected }.sortedBy { it.label }) {
        Text(
            item.label,
            fontSize = fontSize,
            lineHeight = lineHeight,
            modifier = Modifier.clickable { onEvent(UiEvent.Unselect(item)) })
    }
}

@Composable
fun MainView(modifier: Modifier = Modifier) {
    val leftScrollState = rememberScrollState()
    val rightScrollState = rememberScrollState()
    val einkaufszettelViewModel = remember { EinkaufszettelViewModel() }
    val items by einkaufszettelViewModel.items.collectAsState()

    Row(
        modifier
            .fillMaxSize()
            .padding(horizontal = padding)
    ) {
        Column(
            modifier = Modifier
                .weight(1.0f)
                .verticalScroll(leftScrollState)
        ) {
            LeftPanel(items, einkaufszettelViewModel::onEvent)
        }
        Column(
            modifier = Modifier
                .weight(1.0f)
                .verticalScroll(rightScrollState)
        ) {
            RightPanel(items, einkaufszettelViewModel::onEvent)
        }
    }
}
