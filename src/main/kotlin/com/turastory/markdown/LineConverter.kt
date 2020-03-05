package com.turastory.markdown

typealias LineConversionRule = (String) -> List<String>

/**
 * LineConverter reads lines of texts and generates another texts,
 * depending on the rules provided.
 */
interface LineConverter {
    operator fun invoke(lines: List<String>): List<String>
}

fun LineConverter(classifier: Classifier, rules: Map<Type, LineConversionRule>): LineConverter = object : LineConverter {
    override fun invoke(lines: List<String>): List<String> {
        val convertWithType: LineConversionRule = { line ->
            val type = classifier(line)
            rules[type]?.invoke(line) ?: listOf(line)
        }

        return lines.fold(Pair(false, listOf<String>())) { (skipConversion, result), line ->
            val type = classifier(line)
            if (skipConversion) {
                if (type == Type.Code) {
                    Pair(false, result + convertWithType(line))
                } else {
                    Pair(skipConversion, result + listOf(line))
                }
            } else {
                if (type == Type.Code) {
                    Pair(true, result + convertWithType(line))
                } else {
                    Pair(skipConversion, result + convertWithType(line))
                }
            }
        }.second
    }
}
