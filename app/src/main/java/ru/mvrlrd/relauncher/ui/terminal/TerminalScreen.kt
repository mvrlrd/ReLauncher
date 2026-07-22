package ru.mvrlrd.relauncher.ui.terminal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

private val TerminalBackground = Color(0xFF0C0C0C)
private val PromptColor = Color(0xFF9CDCFE)
private val OutputColor = Color(0xFFD4D4D4)
private val ErrorColor = Color(0xFFF44747)

private val terminalTextStyle = TextStyle(
    fontFamily = FontFamily.Monospace,
    fontSize = 14.sp,
    color = OutputColor,
)

@Composable
fun TerminalScreen(
    viewModel: TerminalViewModel,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(state.lines.size) {
        if (state.lines.isNotEmpty()) {
            listState.animateScrollToItem(state.lines.lastIndex)
        }
    }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TerminalBackground)
            .padding(8.dp),
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        ) {
            items(state.lines) { line ->
                androidx.compose.material3.Text(
                    text = line.text,
                    style = terminalTextStyle.copy(color = line.color()),
                )
            }
        }

        Row(modifier = Modifier.fillMaxWidth()) {
            androidx.compose.material3.Text(
                text = state.prompt,
                style = terminalTextStyle.copy(color = PromptColor),
            )
            BasicTextField(
                value = state.input,
                onValueChange = viewModel::onInputChange,
                textStyle = terminalTextStyle,
                cursorBrush = SolidColor(OutputColor),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { viewModel.onSubmit() }),
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
                    .onPreviewKeyEvent { event ->
                        if (event.type != KeyEventType.KeyDown) return@onPreviewKeyEvent false
                        when (event.key) {
                            Key.DirectionUp -> {
                                viewModel.historyPrev(); true
                            }
                            Key.DirectionDown -> {
                                viewModel.historyNext(); true
                            }
                            Key.Enter -> {
                                viewModel.onSubmit(); true
                            }
                            else -> false
                        }
                    },
            )
        }
    }
}

private fun TerminalLine.color(): Color = when (type) {
    LineType.PROMPT -> PromptColor
    LineType.OUTPUT -> OutputColor
    LineType.ERROR -> ErrorColor
}
