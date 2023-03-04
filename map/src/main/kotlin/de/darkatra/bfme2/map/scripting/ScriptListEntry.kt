package de.darkatra.bfme2.map.scripting

import de.darkatra.bfme2.map.serialization.Deserialize
import de.darkatra.bfme2.map.serialization.ScriptListEntryDeserializer

@Deserialize(using = ScriptListEntryDeserializer::class)
sealed interface ScriptListEntry {
    val name: String
    val active: Boolean
    val subroutine: Boolean
}
