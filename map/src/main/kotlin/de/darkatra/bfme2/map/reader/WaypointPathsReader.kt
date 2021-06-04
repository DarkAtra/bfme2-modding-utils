package de.darkatra.bfme2.map.reader

import de.darkatra.bfme2.map.MapFile
import de.darkatra.bfme2.map.WaypointPath
import de.darkatra.bfme2.readUInt
import org.apache.commons.io.input.CountingInputStream

class WaypointPathsReader : AssetReader {

	companion object {
		const val ASSET_NAME = "WaypointsList"
	}

	override fun read(reader: CountingInputStream, context: MapFileParseContext, builder: MapFile.Builder) {

		MapFileReader.readAsset(reader, context, PolygonTriggersReader.ASSET_NAME) {

			val numberOfWaypointPaths = reader.readUInt()

			val waypointPaths = mutableListOf<WaypointPath>()
			for (i in 0u until numberOfWaypointPaths step 1) {
				waypointPaths.add(WaypointPath(
					startWaypointID = reader.readUInt(),
					endWaypointID = reader.readUInt()
				))
			}

			builder.waypointPaths(waypointPaths)
		}
	}
}
