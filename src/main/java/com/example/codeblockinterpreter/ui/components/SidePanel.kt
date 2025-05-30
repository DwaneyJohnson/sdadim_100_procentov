package com.example.codeblockinterpreter.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.codeblockinterpreter.model.BlockType
import androidx.compose.ui.Alignment


@Composable
fun SidePanel(
    isExpanded: Boolean,
    onClose: () -> Unit,
    onBlockSelected: (BlockType) -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = isExpanded,
        enter = slideInHorizontally { -it } + fadeIn(),
        exit = slideOutHorizontally { -it } + fadeOut(),
        modifier = modifier.fillMaxHeight()
    ) {
        Surface(
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .width(240.dp)
                .fillMaxHeight()
        ) {
            Column(
                modifier = Modifier
                    .padding(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Blocks", style = MaterialTheme.typography.titleLarge)
                    IconButton(onClick = onClose) {
                        Icon(Icons.Default.Close, contentDescription = "Close panel")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                BlockCategorySection(
                    title = "Variables",
                    blockTypes = listOf(
                        BlockType.VARIABLE_DECLARATION,
                        BlockType.ASSIGNMENT
                    ),
                    onBlockSelected = onBlockSelected
                )

                BlockCategorySection(
                    title = "Logic",
                    blockTypes = listOf(
                        BlockType.IF_STATEMENT,
                        BlockType.WHILE_LOOP
                    ),
                    onBlockSelected = onBlockSelected
                )

                BlockCategorySection(
                    title = "Operations",
                    blockTypes = listOf(
                        BlockType.ARITHMETIC_OPERATION
                    ),
                    onBlockSelected = onBlockSelected
                )
            }
        }
    }
}


@Composable
private fun BlockCategorySection(
    title: String,
    blockTypes: List<BlockType>,
    onBlockSelected: (BlockType) -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp)
        ) {
            blockTypes.forEach { blockType ->
                OutlinedButton(
                    onClick = { onBlockSelected(blockType) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Text(
                        text = blockType.displayName(),
                        modifier = Modifier.padding(4.dp)
                    )
                }
            }
        }
    }
}