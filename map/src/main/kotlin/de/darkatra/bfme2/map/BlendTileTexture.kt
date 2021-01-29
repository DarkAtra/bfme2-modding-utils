package de.darkatra.bfme2.map

data class BlendTileTexture(
	val name: String,
	val cellStart: UInt,
	/**
	 * Texture "cells" are 64x64 blocks within a source texture.
	 * So for example, a 128x128 texture has 4 cells.
	 */
	val cellCount: UInt,
	/**
	 * Size of this texture, in texture cell units.
	 */
	val cellSize: UInt,
	val magicValue: UInt
)
