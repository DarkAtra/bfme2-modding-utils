package de.darkatra.bfme2

// TODO: consider changing r, g, b, a to one single UInt field and providing getters and setters for each color component
data class Color(
    val r: Int,
    val g: Int,
    val b: Int,
    val a: Int = 255
)
