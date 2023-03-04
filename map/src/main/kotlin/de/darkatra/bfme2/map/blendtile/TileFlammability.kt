package de.darkatra.bfme2.map.blendtile

enum class TileFlammability(
    internal val byte: Byte
) {
    FIRE_RESISTANT(0),
    GRASS(1),
    HIGHLY_FLAMMABLE(2),
    UNDEFINED(3)
}
