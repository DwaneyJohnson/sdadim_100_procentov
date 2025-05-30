package com.example.codeblockinterpreter.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.codeblockinterpreter.model.BlockType
import com.example.codeblockinterpreter.model.CodeBlock
import com.example.codeblockinterpreter.ui.components.*

@Composable
fun CodeBlockInterpreterScreen(
    blocks: List<CodeBlock>,
    onBlocksChanged: (List<CodeBlock>) -> Unit,
    onRunProgram: () -> Unit,
    onClearVariables: () -> Unit,
    output: List<String>,
    modifier: Modifier = Modifier
) {
    var isSidePanelOpen by remember { mutableStateOf(false) }
    var editingBlock by remember { mutableStateOf<CodeBlock?>(null) }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { isSidePanelOpen = true },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                ) {
                    Icon(Icons.Default.Menu, contentDescription = "Open blocks panel")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Blocks Library")
                }

                Button(
                    onClick = {
                        if (blocks.isNotEmpty()) {
                            onClearVariables()
                            onRunProgram()
                        }
                    },
                    modifier = Modifier.weight(1f),
                    enabled = blocks.isNotEmpty()
                ) {
                    Text("Run Program")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            BlockList(
                blocks = blocks,
                onEditBlock = { block -> editingBlock = block },
                onDeleteBlock = { block ->
                    if (block.type == BlockType.VARIABLE_DECLARATION) {
                        onClearVariables()
                    }
                    onBlocksChanged(blocks.filterNot { it == block })
                },
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Output Console",
                style = MaterialTheme.typography.labelLarge
            )
            Spacer(modifier = Modifier.height(4.dp))
            OutputDisplay(
                output = output,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.4f)
            )
        }

        SidePanel(
            isExpanded = isSidePanelOpen,
            onClose = { isSidePanelOpen = false },
            onBlockSelected = { blockType ->
                val newBlock = createDefaultBlock(blockType)
                onBlocksChanged(blocks + newBlock)
                isSidePanelOpen = false
            },
            modifier = Modifier.align(Alignment.TopStart)
        )

        editingBlock?.let { block ->
            when (block.type) {
                BlockType.VARIABLE_DECLARATION -> VariableDialog(
                    initialNames = block.parameters["names"] ?: "",
                    onConfirm = { names ->
                        onBlocksChanged(blocks.map {
                            if (it == block) it.copy(
                                parameters = mapOf("names" to names),
                                lastModified = System.currentTimeMillis()
                            ) else it
                        })
                        editingBlock = null
                    },
                    onCancel = { editingBlock = null }
                )

                BlockType.ASSIGNMENT -> AssignmentDialog(
                    initialVarName = block.parameters["varName"] ?: "",
                    initialExpr = block.parameters["expression"] ?: "",
                    onConfirm = { varName, expr ->
                        onBlocksChanged(blocks.map {
                            if (it == block) it.copy(
                                parameters = mapOf(
                                    "varName" to varName,
                                    "expression" to expr
                                )
                            ) else it
                        })
                        editingBlock = null
                    },
                    onCancel = { editingBlock = null }
                )

                BlockType.ARITHMETIC_OPERATION -> ArithmeticDialog(
                    initialExpr = block.parameters["expression"] ?: "",
                    onConfirm = { expr ->
                        onBlocksChanged(blocks.map {
                            if (it == block) it.copy(parameters = mapOf("expression" to expr))
                            else it
                        })
                        editingBlock = null
                    },
                    onCancel = { editingBlock = null }
                )

                BlockType.IF_STATEMENT,
                BlockType.IF_ELSE_STATEMENT,
                BlockType.WHILE_LOOP -> {
                    var condition by remember {
                        mutableStateOf(
                            block.parameters["condition"] ?: ""
                        )
                    }

                    AlertDialog(
                        onDismissRequest = { editingBlock = null },
                        title = {
                            Text(
                                when (block.type) {
                                    BlockType.IF_STATEMENT -> "Edit If Condition"
                                    BlockType.IF_ELSE_STATEMENT -> "Edit If-Else Condition"
                                    else -> "Edit While Condition"
                                }
                            )
                        },
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
                                onClick = {
                                    onBlocksChanged(blocks.map {
                                        if (it == block) it.copy(parameters = mapOf("condition" to condition))
                                        else it
                                    })
                                    editingBlock = null
                                }
                            ) {
                                Text("Save")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { editingBlock = null }) {
                                Text("Cancel")
                            }
                        }
                    )
                }

                else -> {}
            }
        }
    }
}

private fun createDefaultBlock(type: BlockType): CodeBlock {
    return when (type) {
        BlockType.VARIABLE_DECLARATION ->
            CodeBlock(type, mapOf("names" to "var1, var2"))
        BlockType.ASSIGNMENT ->
            CodeBlock(type, mapOf("varName" to "x", "expression" to "0"))
        BlockType.ARITHMETIC_OPERATION ->
            CodeBlock(type, mapOf("expression" to "a + b"))
        BlockType.IF_STATEMENT ->
            CodeBlock(type, mapOf("condition" to "x > 0"))
        BlockType.IF_ELSE_STATEMENT ->
            CodeBlock(type, mapOf("condition" to "x > 0"))
        BlockType.WHILE_LOOP ->
            CodeBlock(type, mapOf("condition" to "x < 10"))
        else -> CodeBlock(type)
    }
}