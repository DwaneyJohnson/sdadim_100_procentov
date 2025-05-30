package com.example.codeblockinterpreter.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.codeblockinterpreter.model.BlockType
import com.example.codeblockinterpreter.model.CodeBlock

@Composable
fun BlockCreationPanel(
    onBlockCreated: (CodeBlock) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDialog by remember { mutableStateOf<BlockType?>(null) }

    Column(
        modifier = modifier
            .padding(8.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Button(
            onClick = { showDialog = BlockType.VARIABLE_DECLARATION },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Variable")
        }

        Button(
            onClick = { showDialog = BlockType.ASSIGNMENT },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Assignment")
        }

        Button(
            onClick = { showDialog = BlockType.ARITHMETIC_OPERATION },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Arithmetic Operation")
        }

        Button(
            onClick = { showDialog = BlockType.IF_STATEMENT },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add If Statement")
        }

        Button(
            onClick = { showDialog = BlockType.IF_ELSE_STATEMENT },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add If-Else Statement")
        }

        Button(
            onClick = { showDialog = BlockType.WHILE_LOOP },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add While Loop")
        }
    }

    showDialog?.let { type ->
        when (type) {
            BlockType.VARIABLE_DECLARATION -> VariableDialog(
                onConfirm = { names ->
                    onBlockCreated(CodeBlock(
                        type = BlockType.VARIABLE_DECLARATION,
                        parameters = mapOf("names" to names)
                    ))
                    showDialog = null
                },
                onCancel = { showDialog = null }
            )

            BlockType.ASSIGNMENT -> AssignmentDialog(
                onConfirm = { varName, expr ->
                    onBlockCreated(CodeBlock(
                        type = BlockType.ASSIGNMENT,
                        parameters = mapOf(
                            "varName" to varName,
                            "expression" to expr
                        )
                    ))
                    showDialog = null
                },
                onCancel = { showDialog = null }
            )

            BlockType.ARITHMETIC_OPERATION -> ArithmeticDialog(
                onConfirm = { expr ->
                    onBlockCreated(CodeBlock(
                        type = BlockType.ARITHMETIC_OPERATION,
                        parameters = mapOf("expression" to expr)
                    ))
                    showDialog = null
                },
                onCancel = { showDialog = null }
            )

            BlockType.IF_STATEMENT -> IfConditionDialog(
                title = "Create If Statement",
                onConfirm = { condition ->
                    onBlockCreated(CodeBlock(
                        type = BlockType.IF_STATEMENT,
                        parameters = mapOf("condition" to condition)
                    ))
                    showDialog = null
                },
                onCancel = { showDialog = null }
            )

            BlockType.IF_ELSE_STATEMENT -> IfConditionDialog(
                title = "Create If-Else Statement",
                onConfirm = { condition ->
                    onBlockCreated(CodeBlock(
                        type = BlockType.IF_ELSE_STATEMENT,
                        parameters = mapOf("condition" to condition)
                    ))
                    showDialog = null
                },
                onCancel = { showDialog = null }
            )

            BlockType.WHILE_LOOP -> IfConditionDialog(
                title = "Create While Loop",
                onConfirm = { condition ->
                    onBlockCreated(CodeBlock(
                        type = BlockType.WHILE_LOOP,
                        parameters = mapOf("condition" to condition)
                    ))
                    showDialog = null
                },
                onCancel = { showDialog = null }
            )

            else -> {}
        }
    }
}

@Composable
fun IfConditionDialog(
    title: String,
    onConfirm: (String) -> Unit,
    onCancel: () -> Unit
) {
    var condition by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onCancel,
        title = { Text(title) },
        text = {
            Column {
                Text("Supported comparisons: == != < > <= >=")
                Text("Example: a > b + 2")
                TextField(
                    value = condition,
                    onValueChange = { condition = it },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { if (condition.isNotBlank()) onConfirm(condition) }
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onCancel) {
                Text("Cancel")
            }
        }
    )
}