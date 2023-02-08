package de.darkatra.bfme2.map

import de.darkatra.bfme2.Vector3

data class ScriptArgument(
    val argumentType: ScriptArgumentType,

    // either
    val intValue: Int? = null, // sometimes interpreted as Boolean
    val floatValue: Float? = null,
    val stringValue: String? = null,

    // or
    val position: Vector3? = null
)
