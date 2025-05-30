package com.example.codeblockinterpreter.model

data class CodeBlock(
    val type: BlockType,
    val parameters: Map<String, String> = emptyMap(),
    val children: List<CodeBlock> = emptyList(),
    val lastModified: Long = System.currentTimeMillis()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CodeBlock) return false

        return this.type == other.type &&
                this.parameters == other.parameters &&
                this.children == other.children
    }

    override fun hashCode(): Int {
        return type.hashCode() + parameters.hashCode() + children.hashCode()
    }
}