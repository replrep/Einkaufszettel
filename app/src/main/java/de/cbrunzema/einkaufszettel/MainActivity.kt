package de.cbrunzema.einkaufszettel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.cbrunzema.einkaufszettel.ui.theme.EinkaufszettelTheme

val fontSize = 30.sp
val lineHeight = 54.sp
val padding = 10.dp

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
fun LeftPanel() {
    for (i in 0..82) {
        Text("left $i", fontSize = fontSize, lineHeight = lineHeight)
    }
}

@Composable
fun RightPanel() {
    for (i in 0..82) {
        Text("right $i", fontSize = fontSize, lineHeight = lineHeight)
    }
}

@Composable
fun MainView(modifier: Modifier = Modifier) {
    val leftScrollState = rememberScrollState()
    val rightScrollState = rememberScrollState()
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
            LeftPanel()
        }
        Column(
            modifier = Modifier
                .weight(1.0f)
                .verticalScroll(rightScrollState)
        ) {
            RightPanel()
        }
    }
}
