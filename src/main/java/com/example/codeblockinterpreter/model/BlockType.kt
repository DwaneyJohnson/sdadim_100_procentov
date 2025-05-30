package com.example.codeblockinterpreter.model

enum class BlockType {
    VARIABLE_DECLARATION,
    ASSIGNMENT,
    ARITHMETIC_OPERATION,
    IF_STATEMENT,
    IF_ELSE_STATEMENT,
    WHILE_LOOP,
    ARRAY_DECLARATION,
    ARRAY_ACCESS;

    fun displayName(): String {
        return when (this) {
            VARIABLE_DECLARATION -> "Variable Declaration"
            ASSIGNMENT -> "Assignment"
            ARITHMETIC_OPERATION -> "Arithmetic Operation"
            IF_STATEMENT -> "If Statement"
            IF_ELSE_STATEMENT -> "If-Else Statement"
            WHILE_LOOP -> "While Loop"
            ARRAY_DECLARATION -> "Array Declaration"
            ARRAY_ACCESS -> "Array Access"
        }
    }
}