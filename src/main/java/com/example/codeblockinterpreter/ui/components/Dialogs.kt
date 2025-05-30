package com.example.codeblockinterpreter.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
internal fun VariableDialog(
    initialNames: String = "",
    onConfirm: (String) -> Unit,
    onCancel: () -> Unit
) {
    var names by remember { mutableStateOf(initialNames) }

    AlertDialog(
        onDismissRequest = onCancel,
        title = { Text("Declare Variables") },
        text = {
            Column {
                Text("Enter variable names (comma separated):")
                TextField(
                    value = names,
                    onValueChange = { names = it },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { if (names.isNotBlank()) onConfirm(names) }
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            Button(
                onClick = onCancel,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                )
            ) {
                Text("Cancel")
            }
        }
    )
}

@Composable
internal fun AssignmentDialog(
    initialVarName: String = "",
    initialExpr: String = "",
    onConfirm: (String, String) -> Unit,
    onCancel: () -> Unit
) {
    var varName by remember { mutableStateOf(initialVarName) }
    var expr by remember { mutableStateOf(initialExpr) }

    AlertDialog(
        onDismissRequest = onCancel,
        title = { Text("Create Assignment") },
        text = {
            Column {
                Text("Variable name:")
                TextField(
                    value = varName,
                    onValueChange = { varName = it },
                    modifier = Modifier.fillMaxWidth()
                )
                Text("Expression:")
                TextField(
                    value = expr,
                    onValueChange = { expr = it },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (varName.isNotBlank() && expr.isNotBlank()) {
                        onConfirm(varName, expr)
                    }
                }
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            Button(
                onClick = onCancel,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                )
            ) {
                Text("Cancel")
            }
        }
    )
}

@Composable
internal fun ArithmeticDialog(
    initialExpr: String = "",
    onConfirm: (String) -> Unit,
    onCancel: () -> Unit
) {
    var expr by remember { mutableStateOf(initialExpr) }

    AlertDialog(
        onDismissRequest = onCancel,
        title = { Text("Create Arithmetic Operation") },
        text = {
            Column {
                Text("Supported operations: + - * / %")
                Text("Example: (a + b) * 2 - c % 3")
                TextField(
                    value = expr,
                    onValueChange = { expr = it },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { if (expr.isNotBlank()) onConfirm(expr) }
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            Button(
                onClick = onCancel,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                )
            ) {
                Text("Cancel")
            }
        }
    )
}

@Composable
internal fun IfDialog(
    initialCondition: String = "",
    onConfirm: (String) -> Unit,
    onCancel: () -> Unit
) {
    var condition by remember { mutableStateOf(initialCondition) }

    AlertDialog(
        onDismissRequest = onCancel,
        title = { Text("Create If Statement") },
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
            Button(
                onClick = onCancel,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                )
            ) {
                Text("Cancel")
            }
        }
    )
}