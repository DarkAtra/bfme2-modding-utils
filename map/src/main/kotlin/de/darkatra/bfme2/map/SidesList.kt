package de.darkatra.bfme2.map

data class SidesList(
	val unknown: Boolean,
	val players: List<Player>,
	val teams: List<Team>,
	val playerScripts: PlayerScripts,
)
