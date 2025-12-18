package de.darkatra.bfme2.map.scripting

import io.goodforgod.graalvm.hint.annotation.ReflectionHint

@ReflectionHint(ReflectionHint.AccessType.ALL_DECLARED_METHODS)
enum class SequentialScriptTarget(
    internal val byte: Byte
) {
    TEAM(0),
    UNIT(1)
}
