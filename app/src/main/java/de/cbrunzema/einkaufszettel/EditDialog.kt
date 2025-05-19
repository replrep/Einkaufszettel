package de.cbrunzema.einkaufszettel

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun EditDialog(
    title: String,
    item: ShoppingItem,
    onDismissRequest: () -> Unit,
    onDeleteRequest: () -> Unit = {},
    onConfirmation: (ShoppingItem) -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    val needsDeleteButton = remember { !item.label.isEmpty() }
    val label = remember { mutableStateOf(item.label) }
    val level = remember { mutableStateOf(item.level) }
    val singleUse = remember { mutableStateOf(item.singleUse) }

    Dialog(
        onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
        ) {
            Text(title)
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                IconButton(onClick = {
                    onDismissRequest()
                }, modifier = Modifier.weight(1.0f)) {
                    Icon(Icons.Default.Close, "TODO")
                }
                if (needsDeleteButton) {
                    IconButton(
                        onClick = { onDeleteRequest() }, modifier = Modifier.weight(1.0f)
                    ) {
                        Icon(Icons.Default.Delete, "TODO")
                    }
                }
                IconButton(onClick = {
                    if (label.value.isBlank()) {
                        onDismissRequest()
                    } else {
                        onConfirmation(
                            ShoppingItem(
                                label = label.value.trim(),
                                level = level.value,
                                singleUse = singleUse.value,
                                selected = singleUse.value // always select single use items immediately
                            )
                        )
                    }
                }, modifier = Modifier.weight(1.0f)) {
                    Icon(Icons.Default.Done, "TODO")
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                OutlinedTextField(
                    value = label.value,
                    onValueChange = { label.value = it },
                    label = { Text("Label") }, //TODO
                    singleLine = true,
                    modifier = Modifier.focusRequester(focusRequester)
                )
            }
            Row {
                Text("Level A") //TODO
                RadioButton(
                    selected = (level.value == Level.A), onClick = { level.value = Level.A })
                Text("Level B") //TODO
                RadioButton(
                    selected = (level.value == Level.B), onClick = { level.value = Level.B })
            }
            Row {
                Text("Single use") //TODO
                Checkbox(checked = singleUse.value, onCheckedChange = { singleUse.value = it })
            }
        }
    }
    LaunchedEffect(Unit) { focusRequester.requestFocus() }
}
