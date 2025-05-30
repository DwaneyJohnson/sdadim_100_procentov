package com.example.codeblockinterpreter.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.codeblockinterpreter.model.CodeBlock

@Composable
fun BlockList(
    blocks: List<CodeBlock>,
    onEditBlock: (CodeBlock) -> Unit,
    onDeleteBlock: (CodeBlock) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .verticalScroll(scrollState)
            .fillMaxWidth()
    ) {
        blocks.forEach { block ->
            BlockItem(
                block = block,
                onEdit = { onEditBlock(block) },
                onDelete = { onDeleteBlock(block) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            )
        }
    }
}