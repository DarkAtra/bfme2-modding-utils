package de.darkatra.bfme2.map.scripting

enum class SequentialScriptTarget(
    internal val byte: Byte
) {
    TEAM(0),
    UNIT(1)
}
