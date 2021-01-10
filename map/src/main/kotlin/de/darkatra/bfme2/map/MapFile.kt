package de.darkatra.bfme2.map

/**
 * Represents one .map file, but is not tightly coupled to the serialization format for a .map file.
 * This makes it easier to work with the data, as it does not depend on the [version][globalVersion] of the serialization format.
 *
 * Can be serialized and deserialized via [MapFileWriter][de.darkatra.bfme2.map.reader.MapFileWriter] and [MapFileReader][de.darkatra.bfme2.map.reader.MapFileReader]
 */
data class MapFile(
	val assetList: List<AssetListItem>,
	val buildLists: List<BuildList>,
	val globalVersion: Short,
	val multiplayerPositions: List<MultiplayerPosition>,
	val players: List<Player>,
	val playerScripts: List<PlayerScript>,
	val teams: List<Team>,
	val unknown: Boolean,
	val worldSettings: List<Property>
) {

	class Builder {
		private var assetList: List<AssetListItem>? = null
		private var buildLists: List<BuildList>? = null
		private var globalVersion: Short? = null
		private var multiplayerPositions: List<MultiplayerPosition>? = null
		private var players: List<Player>? = null
		private var playerScripts: List<PlayerScript>? = null
		private var teams: List<Team>? = null
		private var unknown: Boolean? = null
		private var worldSettings: List<Property>? = null

		fun assetList(assetList: List<AssetListItem>) = apply { this.assetList = assetList }
		fun buildLists(buildLists: List<BuildList>) = apply { this.buildLists = buildLists }
		fun globalVersion(globalVersion: Short) = apply { this.globalVersion = globalVersion }
		fun multiplayerPositions(multiplayerPositions: List<MultiplayerPosition>) = apply { this.multiplayerPositions = multiplayerPositions }
		fun players(players: List<Player>) = apply { this.players = players }
		fun playerScripts(playerScripts: List<PlayerScript>) = apply { this.playerScripts = playerScripts }
		fun teams(teams: List<Team>) = apply { this.teams = teams }
		fun unknown(unknown: Boolean) = apply { this.unknown = unknown }
		fun worldSettings(worldSettings: List<Property>) = apply { this.worldSettings = worldSettings }

		fun build() = MapFile(
			assetList = assetList ?: throwIllegalStateExceptionForField("assetList"),
			buildLists = buildLists ?: throwIllegalStateExceptionForField("buildLists"),
			globalVersion = globalVersion ?: throwIllegalStateExceptionForField("globalVersion"),
			multiplayerPositions = multiplayerPositions ?: throwIllegalStateExceptionForField("multiplayerPositions"),
			players = players ?: throwIllegalStateExceptionForField("players"),
			playerScripts = playerScripts ?: throwIllegalStateExceptionForField("playerScripts"),
			teams = teams ?: throwIllegalStateExceptionForField("teams"),
			unknown = unknown ?: throwIllegalStateExceptionForField("unknown"),
			worldSettings = worldSettings ?: throwIllegalStateExceptionForField("worldSettings")
		)

		private fun throwIllegalStateExceptionForField(fieldName: String): Nothing {
			throw IllegalStateException("Field '$fieldName' is null.")
		}
	}
}
