package de.cbrunzema.einkaufszettel

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
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
    var label by remember { mutableStateOf(item.label) }
    var level by remember { mutableStateOf(item.level) }
    var singleUse by remember { mutableStateOf(item.singleUse) }

    fun confirm() {
        if (label.isBlank()) {
            onDismissRequest()
        } else {
            onConfirmation(
                ShoppingItem(
                    label = label.trim(),
                    level = level,
                    singleUse = singleUse,
                    selected = item.selected || singleUse, // select single use items immediately
                    unselectedSortIndex = (if (item.level == level) {
                        item.unselectedSortIndex
                    } else {
                        Int.MAX_VALUE
                    }),
                    selectedSortIndex = item.selectedSortIndex
                )
            )
        }
    }

    Dialog(
        onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                IconButton(onClick = {
                    onDismissRequest()
                }, modifier = Modifier.weight(1.0f)) {
                    Icon(Icons.Default.Close, stringResource(R.string.cancel))
                }
                if (needsDeleteButton) {
                    IconButton(
                        onClick = { onDeleteRequest() }, modifier = Modifier.weight(1.0f)
                    ) {
                        Icon(Icons.Default.Delete, stringResource(R.string.delete))
                    }
                }
                IconButton(onClick = {
                    confirm()
                }, modifier = Modifier.weight(1.0f)) {
                    Icon(Icons.Default.Done, stringResource(R.string.done))
                }
            }
            HorizontalDivider()
            Spacer(Modifier.height(8.dp))
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(title, style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    OutlinedTextField(
                        value = label,
                        onValueChange = { label = it },
                        label = { Text(stringResource(R.string.label)) },
                        singleLine = true,
                        modifier = Modifier.focusRequester(focusRequester),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done,
                            capitalization = KeyboardCapitalization.Sentences,
                            showKeyboardOnFocus = true
                        ),
                        keyboardActions = KeyboardActions(onDone = { confirm() })
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(stringResource(R.string.level_a))
                    RadioButton(
                        selected = (level == Level.A), onClick = { level = Level.A })
                    Text(stringResource(R.string.level_b))
                    RadioButton(
                        selected = (level == Level.B), onClick = { level = Level.B })
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(stringResource(R.string.single_use))
                    Checkbox(checked = singleUse, onCheckedChange = { singleUse = it })
                }
            }
        }
    }
    LaunchedEffect(Unit) { focusRequester.requestFocus() }
}
