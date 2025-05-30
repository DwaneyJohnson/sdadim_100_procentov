package com.example.codeblockinterpreter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.codeblockinterpreter.interpreter.Interpreter
import com.example.codeblockinterpreter.model.CodeBlock
import com.example.codeblockinterpreter.ui.screens.CodeBlockInterpreterScreen
import com.example.codeblockinterpreter.ui.theme.CodeBlockInterpreterTheme
import androidx.compose.material3.MaterialTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CodeBlockInterpreterTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var blocks by remember { mutableStateOf(emptyList<CodeBlock>()) }
                    var output by remember { mutableStateOf(emptyList<String>()) }
                    val interpreter = remember { Interpreter() }

                    CodeBlockInterpreterScreen(
                        blocks = blocks,
                        onBlocksChanged = { updatedBlocks ->
                            blocks = updatedBlocks
                            output = emptyList() // Очищаем вывод при изменении блоков
                        },
                        onRunProgram = {
                            output = emptyList() // Очистка перед выполнением
                            output = interpreter.interpret(blocks)
                        },
                        onClearVariables = {
                            interpreter.clearState() // Используем существующий метод clearState()
                            output = emptyList() // Очищаем вывод
                        },
                        output = output
                    )
                }
            }
        }
    }
}