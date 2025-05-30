package com.example.codeblockinterpreter.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.codeblockinterpreter.model.BlockType
import com.example.codeblockinterpreter.model.CodeBlock

@Composable
fun BlockItem(
    block: CodeBlock,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val blockColor = when (block.type) {
        BlockType.VARIABLE_DECLARATION -> Color(0xFF4CAF50)
        BlockType.ASSIGNMENT -> Color(0xFF2196F3)
        BlockType.ARITHMETIC_OPERATION -> Color(0xFF9C27B0)
        BlockType.IF_STATEMENT -> Color(0xFFFF9800)
        BlockType.IF_ELSE_STATEMENT -> Color(0xFFFF5722) // Цвет для if-else
        BlockType.WHILE_LOOP -> Color(0xFFF44336)
        BlockType.ARRAY_DECLARATION -> Color(0xFF607D8B)
        BlockType.ARRAY_ACCESS -> Color(0xFF795548)
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = blockColor.copy(alpha = 0.2f)
        )
    ) {
        Column(Modifier.padding(8.dp)) {
            Text(block.type.displayName(), style = MaterialTheme.typography.titleSmall)

            block.parameters.forEach { (key, value) ->
                Text("$key: $value", style = MaterialTheme.typography.bodySmall)
            }

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, "Edit")
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, "Delete")
                }
            }
        }
    }
}