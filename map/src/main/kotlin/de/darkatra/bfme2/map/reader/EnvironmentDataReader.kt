package de.darkatra.bfme2.map.reader

import de.darkatra.bfme2.map.AssetName
import de.darkatra.bfme2.map.EnvironmentData
import de.darkatra.bfme2.map.MapFile
import de.darkatra.bfme2.readBoolean
import de.darkatra.bfme2.readFloat
import de.darkatra.bfme2.readUShortPrefixedString
import org.apache.commons.io.input.CountingInputStream

class EnvironmentDataReader : AssetReader {

    override fun read(reader: CountingInputStream, context: MapFileParseContext, builder: MapFile.Builder) {

        MapFileReader.readAsset(reader, context, AssetName.ENVIRONMENT_DATA.assetName) { version ->

            val waterMaxAlphaDepth = when {
                version >= 3u -> reader.readFloat()
                else -> null
            }
            val deepWaterAlpha = when {
                version >= 3u -> reader.readFloat()
                else -> null
            }

            val isMacroTextureStretched = when {
                version < 5u -> reader.readBoolean()
                else -> false
            }

            val macroTexture = reader.readUShortPrefixedString()
            val cloudTexture = reader.readUShortPrefixedString()

            val unknownTexture = when {
                version >= 4u -> reader.readUShortPrefixedString()
                else -> null
            }

            // TODO: both RA3 Uprising and C&C4 used v6 for this chunk, but RA3 Uprising had an extra texture here
            // e.g. check if the end position for this asset was reached, if not read the texture
            val unknownTexture2 = when {
                version >= 6u -> reader.readUShortPrefixedString()
                else -> null
            }

            builder.environmentData(
                EnvironmentData(
                    waterMaxAlphaDepth = waterMaxAlphaDepth,
                    deepWaterAlpha = deepWaterAlpha,
                    isMacroTextureStretched = isMacroTextureStretched,
                    macroTexture = macroTexture,
                    cloudTexture = cloudTexture,
                    unknownTexture = unknownTexture,
                    unknownTexture2 = unknownTexture2
                )
            )
        }
    }
}
