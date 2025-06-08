package de.cbrunzema.einkaufszettel

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun InfoDialog(
    onDismissRequest: () -> Unit
) {
    Dialog(
        onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Spacer(modifier = Modifier.weight(1.0f))
                IconButton(onClick = {
                    onDismissRequest()
                }, modifier = Modifier.weight(1.0f)) {
                    Icon(Icons.Default.Done, stringResource(R.string.done))
                }
            }
            HorizontalDivider()
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        stringResource(R.string.info), style = MaterialTheme.typography.titleLarge
                    )
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    "${stringResource(R.string.githash)}: ${BuildConfig.gitHash}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    "${stringResource(R.string.datafile)}: ${Einkaufszettel.dataFile.canonicalPath}",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}
