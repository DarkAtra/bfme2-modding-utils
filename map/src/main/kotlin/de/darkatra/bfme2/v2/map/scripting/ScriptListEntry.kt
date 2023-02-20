package de.darkatra.bfme2.v2.map.scripting

import de.darkatra.bfme2.v2.map.deserialization.Deserialize
import de.darkatra.bfme2.v2.map.deserialization.ScriptListEntryDeserializer

@Deserialize(using = ScriptListEntryDeserializer::class)
sealed interface ScriptListEntry {
    val name: String
    val active: Boolean
    val subroutine: Boolean
}
