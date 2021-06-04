package de.darkatra.bfme2.map

data class MultiplayerPosition(
	val isHuman: Boolean,
	val isComputer: Boolean,
	val loadAIScript: Boolean?,
	val team: UInt,
	val sideRestrictions: List<String>
)
