package de.darkatra.bfme2.map.reader

import de.darkatra.bfme2.Vector3
import de.darkatra.bfme2.map.AssetName
import de.darkatra.bfme2.map.CastleData
import de.darkatra.bfme2.map.CastleTemplate
import de.darkatra.bfme2.map.MapFile
import de.darkatra.bfme2.readFloat
import de.darkatra.bfme2.readUInt
import de.darkatra.bfme2.readUIntAsBoolean
import de.darkatra.bfme2.readUShortPrefixedString
import org.apache.commons.io.input.CountingInputStream

class CastleDataReader(
    private val propertyKeyReader: PropertyKeyReader
) : AssetReader {

    override fun read(reader: CountingInputStream, context: MapFileParseContext, builder: MapFile.Builder) {

        MapFileReader.readAsset(reader, context, AssetName.POLYGON_TRIGGERS.assetName) { version ->

            val propertyKey = propertyKeyReader.read(reader, context)

            val numberOfCastleTemplates = reader.readUInt()

            val castleTemplates = mutableListOf<CastleTemplate>()
            for (i in 0u until numberOfCastleTemplates step 1) {
                castleTemplates.add(
                    readCastleTemplate(reader, version)
                )
            }

            val castlePerimeterPoints = mutableListOf<Vector3>()

            val hasCastlePerimeter = reader.readUIntAsBoolean()
            if (hasCastlePerimeter) {
                val numberOfCastlePerimeterPoints = reader.readUInt()
                for (i in 0u until numberOfCastlePerimeterPoints step 1) {
                    castlePerimeterPoints.add(
                        readCastlePerimeterPoint(reader, version)
                    )
                }
            }

            builder.castleData(
                CastleData(
                    propertyKey,
                    castleTemplates,
                    castlePerimeterPoints
                )
            )
        }
    }

    private fun readCastleTemplate(reader: CountingInputStream, version: UShort): CastleTemplate {

        val name = reader.readUShortPrefixedString()
        val templateName = reader.readUShortPrefixedString()
        val offset = Vector3(
            x = reader.readFloat(),
            y = reader.readFloat(),
            z = reader.readFloat()
        )
        val angle = reader.readFloat()

        val priority = when {
            version >= 4u -> reader.readUInt()
            else -> null
        }
        val phase = when {
            version >= 4u -> reader.readUInt()
            else -> null
        }

        return CastleTemplate(
            name = name,
            templateName = templateName,
            offset = offset,
            angle = angle,
            priority = priority,
            phase = phase
        )
    }

    private fun readCastlePerimeterPoint(reader: CountingInputStream, version: UShort): Vector3 {
        return if (version >= 3u) {
            Vector3(
                x = reader.readFloat(),
                y = reader.readFloat(),
                z = 0f
            )
        } else {
            Vector3(
                x = reader.readFloat(),
                y = reader.readFloat(),
                z = reader.readFloat()
            )
        }
    }
}
