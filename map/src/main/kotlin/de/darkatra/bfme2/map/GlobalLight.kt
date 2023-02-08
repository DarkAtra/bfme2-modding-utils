package de.darkatra.bfme2.map

import de.darkatra.bfme2.Vector3

data class GlobalLight(
    val ambient: Vector3,
    val color: Vector3,
    val direction: Vector3
)
