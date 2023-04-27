package de.darkatra.bfme2.map.scripting

import de.darkatra.bfme2.Vector3

data class ScriptArgument(
    val argumentType: ScriptArgumentType,
    val intValue: Int? = null, // sometimes interpreted as Boolean
    val floatValue: Float? = null,
    val stringValue: String? = null,
    val position: Vector3? = null
)
