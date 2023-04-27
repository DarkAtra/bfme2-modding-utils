package de.darkatra.bfme2.map.scripting

import de.darkatra.bfme2.map.serialization.ScriptListEntrySerde
import de.darkatra.bfme2.map.serialization.Serialize

@Serialize(using = ScriptListEntrySerde::class)
sealed interface ScriptListEntry {
    val name: String
    val active: Boolean
    val subroutine: Boolean
}
