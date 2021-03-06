package de.darkatra.bfme2.map

import de.darkatra.bfme2.Vector3

data class BuildListItem(
	val buildingName: String,
	val name: String,
	val position: Vector3,
	val angle: Float,
	val isAlreadyBuilt: Boolean,
	val unknown1: Boolean?,
	val rebuilds: UInt,
	val script: String,
	val startingHealth: UInt,
	val unknown2: Boolean,
	val unknown3: Boolean,
	val unknown4: Boolean,
)
