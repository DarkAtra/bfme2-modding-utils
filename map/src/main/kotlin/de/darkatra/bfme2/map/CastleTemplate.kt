package de.darkatra.bfme2.map

import de.darkatra.bfme2.Vector3

data class CastleTemplate(
    val name: String,
    val templateName: String,
    val offset: Vector3,
    val angle: Float,
    val priority: UInt?,
    val phase: UInt?
)
