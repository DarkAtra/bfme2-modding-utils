package de.darkatra.bfme2.map.reader

import de.darkatra.bfme2.map.AssetName
import de.darkatra.bfme2.map.MapFile
import org.apache.commons.io.input.CountingInputStream

class WorldSettingsReader(
    private val propertiesReader: PropertiesReader
) : AssetReader {

    override fun read(reader: CountingInputStream, context: MapFileParseContext, builder: MapFile.Builder) {
        MapFileReader.readAsset(reader, context, AssetName.WORLD_INFO.assetName) {
            builder.worldSettings(
                worldSettings = propertiesReader.read(reader, context)
            )
        }
    }
}
