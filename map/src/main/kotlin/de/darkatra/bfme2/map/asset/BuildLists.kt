package de.darkatra.bfme2.map.asset

import de.darkatra.bfme2.map.MapFileParseContext
import de.darkatra.bfme2.toLittleEndianInt
import org.apache.commons.io.input.CountingInputStream

class BuildLists(
	val buildLists: List<BuildList>
) : Asset {

	companion object : AssetReader<BuildLists> {
		const val ASSET_NAME = "BuildLists"

		override fun read(reader: CountingInputStream, context: MapFileParseContext): BuildLists {

			return AssetReader.readAsset(reader, context) {

				val numberOfBuildLists = reader.readNBytes(4).toLittleEndianInt()

				val buildLists = mutableListOf<BuildList>()
				for (i in 0 until numberOfBuildLists step 1) {
					buildLists.add(
						BuildList.read(reader, context)
					)
				}

				BuildLists(
					buildLists = buildLists
				)
			}
		}
	}
}
