package com.example.codeblockinterpreter.interpreter

import com.example.codeblockinterpreter.model.BlockType
import com.example.codeblockinterpreter.model.CodeBlock

class Interpreter {
    private val variables = mutableMapOf<String, Int>()
    private val arrays = mutableMapOf<String, IntArray>()
    private val output = mutableListOf<String>()
    private var error: String? = null

    fun interpret(blocks: List<CodeBlock>): List<String> {
        clearState()
        try {
            executeBlocks(blocks)
        } catch (e: Exception) {
            error = e.message
            output.add("Error: ${e.message}")
        }
        return output
    }

    fun clearState() {
        variables.clear()
        arrays.clear()
        output.clear()
        error = null
    }

    private fun executeBlocks(blocks: List<CodeBlock>) {
        blocks.forEach { block ->
            when (block.type) {
                BlockType.VARIABLE_DECLARATION -> handleVariableDeclaration(block)
                BlockType.ASSIGNMENT -> handleAssignment(block)
                BlockType.ARITHMETIC_OPERATION -> handleArithmetic(block)
                BlockType.IF_STATEMENT -> handleIf(block)
                BlockType.IF_ELSE_STATEMENT -> handleIfElse(block)
                BlockType.WHILE_LOOP -> handleWhile(block)
                BlockType.ARRAY_DECLARATION -> handleArrayDeclaration(block)
                BlockType.ARRAY_ACCESS -> handleArrayAccess(block)
            }
        }
    }

    private fun handleIfElse(block: CodeBlock) {
        val condition = block.parameters["condition"] ?: throw Exception("Missing condition")
        val isTrue = evaluateCondition(condition)

        if (isTrue) {
            output.add("Condition '$condition' is TRUE (executing if branch)")
            executeBlocks(block.children.filter { it.parameters["branch"] != "else" })
        } else {
            output.add("Condition '$condition' is FALSE (executing else branch)")
            executeBlocks(block.children.filter { it.parameters["branch"] == "else" })
        }
    }

    private fun handleVariableDeclaration(block: CodeBlock) {
        val names = block.parameters["names"]?.split(",")?.map { it.trim() }
            ?: throw Exception("Missing variable names")

        names.forEach { name ->
            if (name.isBlank()) throw Exception("Empty variable name")
            if (name in variables) throw Exception("Variable '$name' already exists")
            variables[name] = 0
            output.add("Declared variable: $name = 0")
        }
    }

    private fun handleAssignment(block: CodeBlock) {
        val varName = block.parameters["varName"] ?: throw Exception("Missing variable name")
        val expr = block.parameters["expression"] ?: throw Exception("Missing expression")

        if (varName !in variables) throw Exception("Unknown variable '$varName'")

        val value = evaluateExpression(expr)
        variables[varName] = value
        output.add("$varName = $value")
    }

    private fun handleArithmetic(block: CodeBlock) {
        val expr = block.parameters["expression"] ?: throw Exception("Missing expression")
        val result = evaluateExpression(expr)
        output.add("Result: $expr = $result")
    }

    private fun handleIf(block: CodeBlock) {
        val condition = block.parameters["condition"] ?: throw Exception("Missing condition")
        val isTrue = evaluateCondition(condition)

        if (isTrue) {
            output.add("Condition '$condition' is TRUE")
            executeBlocks(block.children)
        } else {
            output.add("Condition '$condition' is FALSE")
        }
    }

    private fun handleWhile(block: CodeBlock) {
        val condition = block.parameters["condition"] ?: throw Exception("Missing condition")

        while (evaluateCondition(condition)) {
            output.add("While condition '$condition' is TRUE")
            executeBlocks(block.children)
        }
        output.add("While condition '$condition' is FALSE")
    }

    private fun handleArrayDeclaration(block: CodeBlock) {
        val name = block.parameters["name"] ?: throw Exception("Missing array name")
        val size = block.parameters["size"]?.toIntOrNull()
            ?: throw Exception("Invalid array size")

        if (name in arrays) throw Exception("Array '$name' already exists")
        if (size <= 0) throw Exception("Array size must be positive")

        arrays[name] = IntArray(size) { 0 }
        output.add("Declared array: $name[$size]")
    }

    private fun handleArrayAccess(block: CodeBlock) {
        val name = block.parameters["arrayName"] ?: throw Exception("Missing array name")
        val index = block.parameters["index"]?.toIntOrNull()
            ?: throw Exception("Invalid array index")
        val operation = block.parameters["operation"] ?: throw Exception("Missing operation")

        val array = arrays[name] ?: throw Exception("Array '$name' not found")
        if (index !in array.indices) throw Exception("Index $index out of bounds")

        when (operation) {
            "get" -> output.add("$name[$index] = ${array[index]}")
            "set" -> {
                val value = block.parameters["value"]?.toIntOrNull()
                    ?: throw Exception("Missing value")
                array[index] = value
                output.add("Set $name[$index] = $value")
            }
            else -> throw Exception("Unknown operation '$operation'")
        }
    }

    private fun evaluateExpression(expr: String): Int {
        return Parser(expr).parse()
    }

    private fun evaluateCondition(cond: String): Boolean {
        val ops = listOf("==", "!=", "<=", ">=", "<", ">")
        val (opIndex, op) = ops.mapNotNull { op ->
            cond.indexOf(op).takeIf { it != -1 }?.let { it to op }
        }.minByOrNull { it.first } ?: throw Exception("Invalid condition")

        val left = cond.substring(0, opIndex).trim()
        val right = cond.substring(opIndex + op.length).trim()

        val leftVal = evaluateExpression(left)
        val rightVal = evaluateExpression(right)

        return when (op) {
            "==" -> leftVal == rightVal
            "!=" -> leftVal != rightVal
            "<" -> leftVal < rightVal
            ">" -> leftVal > rightVal
            "<=" -> leftVal <= rightVal
            ">=" -> leftVal >= rightVal
            else -> throw Exception("Unknown operator '$op'")
        }
    }

    private inner class Parser(private val input: String) {
        private var pos = 0

        fun parse(): Int {
            val result = parseExpression()
            if (pos < input.length) {
                throw Exception("Unexpected character at position $pos")
            }
            return result
        }

        private fun parseExpression(): Int {
            var result = parseTerm()
            while (pos < input.length) {
                when (input[pos]) {
                    '+' -> { pos++; result += parseTerm() }
                    '-' -> { pos++; result -= parseTerm() }
                    else -> break
                }
            }
            return result
        }

        private fun parseTerm(): Int {
            var result = parseFactor()
            while (pos < input.length) {
                when (input[pos]) {
                    '*' -> { pos++; result *= parseFactor() }
                    '/' -> {
                        pos++
                        val divisor = parseFactor()
                        if (divisor == 0) throw Exception("Division by zero")
                        result /= divisor
                    }
                    '%' -> {
                        pos++
                        val divisor = parseFactor()
                        if (divisor == 0) throw Exception("Modulo by zero")
                        result %= divisor
                    }
                    else -> break
                }
            }
            return result
        }

        private fun parseFactor(): Int {
            skipWhitespace()
            if (pos >= input.length) throw Exception("Unexpected end of expression")

            return when (val c = input[pos]) {
                '(' -> {
                    pos++
                    val result = parseExpression()
                    skipWhitespace()
                    if (pos >= input.length || input[pos++] != ')') {
                        throw Exception("Missing closing parenthesis")
                    }
                    result
                }
                in '0'..'9' -> parseNumber()
                in 'a'..'z', in 'A'..'Z', '_' -> parseVariable()
                else -> throw Exception("Unexpected character '$c' at position $pos")
            }
        }

        private fun parseNumber(): Int {
            var num = 0
            while (pos < input.length && input[pos].isDigit()) {
                num = num * 10 + (input[pos++] - '0')
            }
            return num
        }

        private fun parseVariable(): Int {
            val start = pos
            while (pos < input.length && (input[pos].isLetterOrDigit() || input[pos] == '_')) {
                pos++
            }
            val name = input.substring(start, pos)
            return variables[name] ?: throw Exception("Unknown variable '$name'")
        }

        private fun skipWhitespace() {
            while (pos < input.length && input[pos].isWhitespace()) {
                pos++
            }
        }
    }
}