package de.darkatra.bfme2.map.reader

import de.darkatra.bfme2.map.AssetName
import de.darkatra.bfme2.map.GlobalWaterSettings
import de.darkatra.bfme2.map.MapFile
import de.darkatra.bfme2.readFloat
import de.darkatra.bfme2.readUIntAsBoolean
import org.apache.commons.io.input.CountingInputStream

class GlobalWaterSettingsReader : AssetReader {

    override fun read(reader: CountingInputStream, context: MapFileParseContext, builder: MapFile.Builder) {

        MapFileReader.readAsset(reader, context, AssetName.GLOBAL_WATER_SETTINGS.assetName) {
            builder.globalWaterSettings(
                GlobalWaterSettings(
                    reflectionEnabled = reader.readUIntAsBoolean(),
                    reflectionPlaneZ = reader.readFloat()
                )
            )
        }
    }
}
