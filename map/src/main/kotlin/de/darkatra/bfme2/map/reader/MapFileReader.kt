package de.darkatra.bfme2.map.reader

import de.darkatra.bfme2.InvalidDataException
import de.darkatra.bfme2.SkippingInputStream
import de.darkatra.bfme2.map.AssetName
import de.darkatra.bfme2.map.MapFile
import de.darkatra.bfme2.read7BitString
import de.darkatra.bfme2.readUInt
import de.darkatra.bfme2.readUShort
import de.darkatra.bfme2.refpack.MemorizingInputStream
import de.darkatra.bfme2.refpack.RefPackInputStream
import org.apache.commons.io.input.CountingInputStream
import java.io.BufferedInputStream
import java.io.FileNotFoundException
import java.io.InputStream
import java.io.PushbackInputStream
import java.io.UnsupportedEncodingException
import java.nio.charset.StandardCharsets
import java.nio.file.Path
import java.util.zip.InflaterInputStream
import kotlin.system.measureTimeMillis

class MapFileReader {

	private val propertyKeyReader = PropertyKeyReader()
	private val propertyReader = PropertyReader(propertyKeyReader)
	private val propertiesReader = PropertiesReader(propertyReader)

	private val assetListReader = AssetListReader()
	private val blendTileDataReader = BlendTileDataReader()
	private val buildListReader = BuildListReader(propertyKeyReader)
	private val buildListsReader = BuildListsReader(buildListReader)
	private val camerasAnimationsReader = CamerasAnimationsReader()
	private val camerasReader = CamerasReader()
	private val castleDataReader = CastleDataReader(propertyKeyReader)
	private val environmentDataReader = EnvironmentDataReader()
	private val fogSettingsReader = FogSettingsReader()
	private val globalLightingReader = GlobalLightingReader()
	private val globalVersionReader = GlobalVersionReader()
	private val globalWaterSettingsReader = GlobalWaterSettingsReader()
	private val heightMapReader = HeightMapReader()
	private val libraryMapsListReader = LibraryMapsListReader()
	private val missionHotSpotsReader = MissionHotSpotsReader()
	private val missionObjectivesReader = MissionObjectivesReader()
	private val multiplayerPositionsReader = MultiplayerPositionsReader()
	private val objectsReader = ObjectsReader(propertiesReader)
	private val playerReader = PlayerReader(buildListReader, propertiesReader)
	private val playerScriptsReader = PlayerScriptsReader()
	private val polygonTriggersReader = PolygonTriggersReader()
	private val postEffectReader = PostEffectReader()
	private val riverAreasReader = RiverAreasReader()
	private val skyboxReader = SkyboxReader()
	private val standingWaterAreasReader = StandingWaterAreasReader()
	private val standingWaveAreasReader = StandingWaveAreasReader()
	private val teamsReader = TeamsReader(propertiesReader)
	private val triggerAreaReader = TriggerAreasReader()
	private val sidesReader = SidesReader(playerReader, playerScriptsReader, teamsReader)
	private val waypointPathsReader = WaypointPathsReader()
	private val worldSettingsReader = WorldSettingsReader(propertiesReader)

	companion object {
		const val UNCOMPRESSED_FOUR_CC = "CkMp"
		const val REFPACK_FOUR_CC = "EAR\u0000"
		const val ZLIB_FOUR_CC = "ZL5\u0000"

		// TODO: find a better name for this (maybe readList)
		fun readAssets(reader: CountingInputStream, context: MapFileParseContext, callback: (assetName: String) -> Unit) {

			while (reader.byteCount < context.currentEndPosition) {
				val assetIndex = reader.readUInt()
				val assetName = context.getAssetName(assetIndex)

				callback(assetName)
			}
		}

		// TODO: find a better name for this (maybe readObject)
		fun readAsset(reader: CountingInputStream, context: MapFileParseContext, assetName: String, callback: (assetVersion: UShort) -> Unit) {

			val assetVersion = reader.readUShort()

			val dataSize = reader.readUInt()
			val startPosition = reader.byteCount
			val endPosition = dataSize.toLong() + startPosition

			context.push(assetName, endPosition)

			callback(assetVersion)

			context.pop()

			if (reader.byteCount != endPosition) {
				throw InvalidDataException("Error reading '$assetName'. Expected reader to be at position $endPosition, but was at ${reader.byteCount}.")
			}
		}
	}

	fun read(file: Path): MapFile {

		val inputFile = file.toFile()

		if (!inputFile.exists()) {
			throw FileNotFoundException("File '${inputFile.absolutePath}' does not exist.")
		}

		return read(inputFile.inputStream())
	}

	fun read(inputStream: InputStream): MapFile {
		return read(inputStream.buffered())
	}

	fun read(bufferedInputStream: BufferedInputStream): MapFile {

		if (!bufferedInputStream.markSupported()) {
			throw IllegalArgumentException("Can only parse InputStreams that support marks.")
		}

		// TODO: find a better way to determine the size of the actual data
		bufferedInputStream.mark(Int.MAX_VALUE)
		val inputStreamSize = decodeIfNecessary(bufferedInputStream).readBytes().size.toLong()
		bufferedInputStream.reset()

		val countingInputStream = CountingInputStream(decodeIfNecessary(bufferedInputStream))

		val mapBuilder = MapFile.Builder()

		countingInputStream.use {
			readAndValidateFourCC(countingInputStream)

			val assetNames = readAssetNames(countingInputStream)

			val context = MapFileParseContext(assetNames)
			context.push(AssetName.MAP.assetName, inputStreamSize)

			readAssets(countingInputStream, context) { assetName ->
				when (assetName) {
					// TODO: find a better name for ASSET_NAME
					AssetName.ASSET_LIST.assetName -> assetListReader
					AssetName.BLEND_TILE_DATA.assetName -> blendTileDataReader
					AssetName.BUILD_LISTS.assetName -> buildListsReader
					AssetName.CAMERA_ANIMATION_LIST.assetName -> camerasAnimationsReader
					AssetName.NAMED_CAMERAS.assetName -> camerasReader
					AssetName.CASTLE_TEMPLATES.assetName -> castleDataReader
					AssetName.ENVIRONMENT_DATA.assetName -> environmentDataReader
					AssetName.FOG_SETTINGS.assetName -> fogSettingsReader
					AssetName.GLOBAL_LIGHTING.assetName -> globalLightingReader
					AssetName.GLOBAL_VERSION.assetName -> globalVersionReader
					AssetName.GLOBAL_WATER_SETTINGS.assetName -> globalWaterSettingsReader
					AssetName.HEIGHT_MAP_DATA.assetName -> heightMapReader
					AssetName.LIBRARY_MAP_LISTS.assetName -> libraryMapsListReader
					AssetName.MISSION_HOT_SPOTS.assetName -> missionHotSpotsReader
					AssetName.MISSION_OBJECTIVES.assetName -> missionObjectivesReader
					AssetName.MP_POSITION_LIST.assetName -> multiplayerPositionsReader
					AssetName.OBJECTS_LIST.assetName -> objectsReader
					AssetName.PLAYER_SCRIPTS_LIST.assetName -> playerScriptsReader
					AssetName.POLYGON_TRIGGERS.assetName -> polygonTriggersReader
					AssetName.POST_EFFECTS_CHUNK.assetName -> postEffectReader
					AssetName.RIVER_AREAS.assetName -> riverAreasReader
					AssetName.SKYBOX_SETTINGS.assetName -> skyboxReader
					AssetName.SIDES_LIST.assetName -> sidesReader
					AssetName.STANDING_WATER_AREAS.assetName -> standingWaterAreasReader
					AssetName.STANDING_WAVE_AREAS.assetName -> standingWaveAreasReader
					AssetName.TEAMS.assetName -> teamsReader
					AssetName.TRIGGER_AREAS.assetName -> triggerAreaReader
					AssetName.WAYPOINTS_LIST.assetName -> waypointPathsReader
					AssetName.WORLD_INFO.assetName -> worldSettingsReader
					// TODO: implement the remaining readers... (see OpenFeign)
					else -> throw InvalidDataException("Unknown asset name '$assetName'.")
				}.also {
					val timeElapsedToRead = measureTimeMillis {
						it.read(countingInputStream, context, mapBuilder)
					}
					println("Reading $assetName took $timeElapsedToRead millis.")
				}
			}
		}

		return mapBuilder.build()
	}

	// TODO: find a better name for this (maybe readObjectNames)
	private fun readAssetNames(reader: CountingInputStream): Map<UInt, String> {

		val numberOfAssetStrings = reader.readUInt()

		val assetNames = mutableMapOf<UInt, String>()
		for (i in numberOfAssetStrings downTo 1u step 1) {
			val assetName = reader.read7BitString()
			val assetIndex = reader.readUInt()
			if (assetIndex != i) {
				throw IllegalStateException("Illegal assetIndex for '$assetName'.")
			}
			assetNames[assetIndex] = assetName
		}
		return assetNames
	}

	private fun decodeIfNecessary(inputStream: InputStream): InputStream {

		val pushbackInputStream = PushbackInputStream(inputStream, 4)
		val fourCCBytes = pushbackInputStream.readNBytes(4)

		return when (fourCCBytes.toString(StandardCharsets.UTF_8)) {
			// unread 4 bytes to make it possible to read them again when actually parsing the map data
			UNCOMPRESSED_FOUR_CC -> pushbackInputStream.also { it.unread(fourCCBytes) }
			// skip 4 size bytes, we don't need that information
			REFPACK_FOUR_CC -> MemorizingInputStream(RefPackInputStream(SkippingInputStream(pushbackInputStream, 4)), 50)
			// skip 4 size bytes, we don't need that information
			ZLIB_FOUR_CC -> InflaterInputStream(SkippingInputStream(pushbackInputStream, 4))
			else -> throw UnsupportedEncodingException("Encoding is not supported.")
		}
	}

	private fun readAndValidateFourCC(inputStream: InputStream) {
		val fourCC = inputStream.readNBytes(4).toString(StandardCharsets.UTF_8)
		if (fourCC != UNCOMPRESSED_FOUR_CC) {
			throw InvalidDataException("Invalid four character code. Expected '$UNCOMPRESSED_FOUR_CC' but found '$fourCC'.")
		}
	}
}

