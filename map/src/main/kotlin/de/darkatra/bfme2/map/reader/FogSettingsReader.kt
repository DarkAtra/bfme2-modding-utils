package de.darkatra.bfme2.map.reader

import de.darkatra.bfme2.map.AssetName
import de.darkatra.bfme2.map.FogSettings
import de.darkatra.bfme2.map.MapFile
import org.apache.commons.io.input.CountingInputStream

class FogSettingsReader : AssetReader {

    override fun read(reader: CountingInputStream, context: MapFileParseContext, builder: MapFile.Builder) {
        MapFileReader.readAsset(reader, context, AssetName.FOG_SETTINGS.assetName) {
            builder.fogSettings(
                FogSettings(
                    unknown = reader.readNBytes(24).toList()
                )
            )
        }
    }
}
