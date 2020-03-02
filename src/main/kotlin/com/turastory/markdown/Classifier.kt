@file:Suppress("FunctionName")

package com.turastory.markdown

interface Classifier {
    operator fun invoke(line: String): Type
}

fun Classifier(): Classifier = object : Classifier {
    override fun invoke(line: String): Type = when {
        line.firstWord()?.containsOnly('#') == true -> Type.Header
        line.firstWord() == ">" -> Type.Quote
        line.startsWith("\t") or line.startsWith("    ") -> Type.Question
        line.startsWith("->") -> Type.Answer
        line.startsWith("```") -> Type.Code
        else -> Type.None
    }

    private fun String.firstWord(): String? =
        split(" ").getOrNull(0)?.takeIf { it.isNotEmpty() }

    private fun String.containsOnly(char: Char): Boolean =
        fold(true) { acc, ch ->
            acc && ch == char
        }
}
