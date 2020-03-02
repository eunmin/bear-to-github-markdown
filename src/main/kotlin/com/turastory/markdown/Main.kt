package com.turastory.markdown

import java.io.File

fun main() {
    val indent = "&nbsp;".times(8)
    val lineEndSpace = "  "

    val rules: Map<Type, LineConversionRule> = mapOf(
        Type.Quote to { line: String ->
            listOf("$line$lineEndSpace")
        },
        Type.Question to { line: String ->
            listOf("${indent}Q. ${line.trimStart()}$lineEndSpace")
        },
        Type.Answer to { line: String ->
            listOf("- ${line.split(" ").drop(1).joinToString(separator = " ")}$lineEndSpace")
        },
        Type.None to { line: String ->
            listOf("$line$lineEndSpace")
        }
    )

    val classifier = Classifier()
    val converter = LineConverter(classifier, rules)

    File("input")
        .requireListFiles()
        .map { file ->
            file.name to file.readLines()
                .let(converter::invoke)
                .joinToString(separator = "\n")
                .insertBreaks()
        }
        .forEach { (name, text) ->
            with(File("output/$name")) {
                createNewFile()
                writeText(text)
            }
        }
}

private fun String.times(n: Int): String {
    val stringBuilder = StringBuilder()
    repeat(n) {
        stringBuilder.append(this)
    }
    return stringBuilder.toString()
}

private fun File.requireListFiles(): List<File> = listFiles()!!.toList()

private fun String.insertBreaks(): String = replace("\n\n\n", "\n\n<br>\n\n")

