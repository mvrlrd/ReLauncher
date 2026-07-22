package ru.mvrlrd.relauncher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.mvrlrd.relauncher.ui.terminal.TerminalScreen
import ru.mvrlrd.relauncher.ui.terminal.TerminalViewModel
import ru.mvrlrd.relauncher.ui.terminal.TerminalViewModelFactory
import ru.mvrlrd.relauncher.ui.theme.ReLauncherTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ReLauncherTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val component = (application as ReLauncherApp).component
                    val terminalViewModel: TerminalViewModel = viewModel(
                        factory = TerminalViewModelFactory(component.executor()),
                    )
                    TerminalScreen(
                        viewModel = terminalViewModel,
                        modifier = Modifier.padding(innerPadding),
                    )
                }
            }
        }
    }
}
