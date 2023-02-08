package de.darkatra.bfme2.map.reader

import de.darkatra.bfme2.Color
import de.darkatra.bfme2.InvalidDataException
import de.darkatra.bfme2.map.AssetName
import de.darkatra.bfme2.map.MapFile
import de.darkatra.bfme2.map.RiverArea
import de.darkatra.bfme2.readBoolean
import de.darkatra.bfme2.readByte
import de.darkatra.bfme2.readFloat
import de.darkatra.bfme2.readUInt
import de.darkatra.bfme2.readUShortPrefixedString
import org.apache.commons.io.input.CountingInputStream
import java.awt.geom.Line2D

class RiverAreasReader : AssetReader {

    companion object {
        const val MIN_VERSION_WITH_RIVER_TYPE = 3u
    }

    override fun read(reader: CountingInputStream, context: MapFileParseContext, builder: MapFile.Builder) {

        MapFileReader.readAsset(reader, context, AssetName.RIVER_AREAS.assetName) { version ->

            val numberOfRiverAreas = reader.readUInt()

            val riverAreas = mutableListOf<RiverArea>()
            for (i in 0u until numberOfRiverAreas step 1) {
                riverAreas.add(
                    readRiverArea(reader, context, version)
                )
            }

            builder.riverAreas(riverAreas)
        }
    }

    @Suppress("DuplicatedCode")
    private fun readRiverArea(reader: CountingInputStream, context: MapFileParseContext, version: UShort): RiverArea {

        val id = reader.readUInt()
        val name = reader.readUShortPrefixedString()
        val layerName = reader.readUShortPrefixedString()
        val uvScrollSpeed = reader.readFloat()
        val useAdditiveBlending = reader.readBoolean()
        val riverTexture = reader.readUShortPrefixedString()
        val noiseTexture = reader.readUShortPrefixedString()
        val alphaEdgeTexture = reader.readUShortPrefixedString()
        val sparkleTexture = reader.readUShortPrefixedString()
        val color = Color(
            reader.readByte().toInt(),
            reader.readByte().toInt(),
            reader.readByte().toInt()
        )
        val unusedColorA = reader.readByte()
        if (unusedColorA != 0.toByte()) {
            throw InvalidDataException("Expected unused byte after color.")
        }

        val alpha = reader.readFloat()
        val waterHeight = reader.readUInt()

        val riverType = when (version >= MIN_VERSION_WITH_RIVER_TYPE) {
            true -> reader.readUShortPrefixedString()
            false -> null
        }

        val minimumWaterLod = reader.readUShortPrefixedString()

        val numberOfLines = reader.readUInt()

        val lines = mutableListOf<Line2D>()
        for (i in 0u until numberOfLines step 1) {
            lines.add(
                Line2D.Float(
                    reader.readFloat(),
                    reader.readFloat(),
                    reader.readFloat(),
                    reader.readFloat()
                )
            )
        }

        return RiverArea(
            id = id,
            name = name,
            layerName = layerName,
            uvScrollSpeed = uvScrollSpeed,
            useAdditiveBlending = useAdditiveBlending,
            riverTexture = riverTexture,
            noiseTexture = noiseTexture,
            alphaEdgeTexture = alphaEdgeTexture,
            sparkleTexture = sparkleTexture,
            color = color,
            alpha = alpha,
            waterHeight = waterHeight,
            riverType = riverType,
            minimumWaterLod = minimumWaterLod,
            lines = lines
        )
    }
}
