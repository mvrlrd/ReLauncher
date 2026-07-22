package ru.mvrlrd.relauncher.domain

/** Converts a human label into a shell-friendly slug: lowercase, non-alphanumeric → `_`. */
fun slugify(label: String): String {
    val sb = StringBuilder()
    for (ch in label.lowercase()) {
        sb.append(if (ch.isLetterOrDigit()) ch else '_')
    }
    return sb.toString().trim('_').replace(Regex("_+"), "_")
}
