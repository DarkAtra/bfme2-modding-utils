package de.darkatra.bfme2.map

class MultiplayerPosition(
	val isHuman: Boolean,
	val isComputer: Boolean,
	val loadAIScript: Boolean?,
	val team: Int,
	val sideRestrictions: List<String>
)
