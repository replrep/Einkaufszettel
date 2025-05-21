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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun InfoDialog(
    onDismissRequest: () -> Unit
) {
    Dialog(
        onDismissRequest = { onDismissRequest() }) {
        val sourceRepoLabel = stringResource(R.string.source_repo)
        val homepageLink = remember {
            buildAnnotatedString {
                withLink(link = LinkAnnotation.Url(sourceRepoUrl)) {
                    append(sourceRepoLabel)
                    addStyle(
                        style = SpanStyle(
                            color = Color.Blue, textDecoration = TextDecoration.Underline
                        ), start = 0, end = sourceRepoLabel.length
                    )
                }
            }
        }
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
            Spacer(Modifier.height(8.dp))
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        stringResource(R.string.app_name),
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                Spacer(Modifier.height(8.dp))
                Column(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        "${stringResource(R.string.version)}: ${BuildConfig.VERSION_NAME}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Row {
                        Text(
                            stringResource(R.string.check_out_the) + " ",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            homepageLink, style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            HorizontalDivider()
            Spacer(Modifier.height(8.dp))
            Column(modifier = Modifier.padding(horizontal = 16.dp).padding(bottom = 16.dp)) {
                Text(
                    stringResource(R.string.copyright), style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    stringResource(R.string.license), style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}
