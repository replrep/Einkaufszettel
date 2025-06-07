package de.cbrunzema.einkaufszettel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import de.cbrunzema.einkaufszettel.ui.theme.EinkaufszettelTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val snackbarCoroutineScope = rememberCoroutineScope()
            val snackbarHostState = remember { SnackbarHostState() }

            EinkaufszettelTheme {
                Scaffold(modifier = Modifier.fillMaxSize(), snackbarHost = {
                    SnackbarHost(hostState = snackbarHostState)
                }) { innerPadding ->
                    mainViewModel = viewModel()
                    mainViewModel.load()
                    MainView(
                        mainViewModel = mainViewModel,
                        modifier = Modifier.padding(innerPadding),
                        snackbarLauncher = { msg ->
                            snackbarCoroutineScope.launch {
                                snackbarHostState.showSnackbar(
                                    message = msg, duration = SnackbarDuration.Short
                                )
                            }
                        })
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        mainViewModel.save()
    }
}
