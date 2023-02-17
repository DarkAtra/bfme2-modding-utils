package de.darkatra.bfme2.map.reader

import de.darkatra.bfme2.InvalidDataException
import de.darkatra.bfme2.Vector2
import de.darkatra.bfme2.map.AssetName
import de.darkatra.bfme2.map.MapFile
import de.darkatra.bfme2.map.StandingWaveArea
import de.darkatra.bfme2.readBoolean
import de.darkatra.bfme2.readFloat
import de.darkatra.bfme2.readUInt
import de.darkatra.bfme2.readUIntAsBoolean
import de.darkatra.bfme2.readUShortPrefixedString
import org.apache.commons.io.input.CountingInputStream

class StandingWaveAreasReader : AssetReader {

    override fun read(reader: CountingInputStream, context: MapFileParseContext, builder: MapFile.Builder) {

        MapFileReader.readAsset(reader, context, AssetName.STANDING_WAVE_AREAS.assetName) { version ->

            val numberOfStandingWaveAreasReader = reader.readUInt()

            val standingWaveAreas = mutableListOf<StandingWaveArea>()
            for (i in 0u until numberOfStandingWaveAreasReader step 1) {
                standingWaveAreas.add(
                    readStandingWaveArea(reader, version)
                )
            }

            builder.standingWaveAreas(standingWaveAreas)
        }
    }

    @Suppress("DuplicatedCode")
    private fun readStandingWaveArea(reader: CountingInputStream, version: UShort): StandingWaveArea {

        val standingWaveArea = StandingWaveArea.Builder()

        standingWaveArea.id(reader.readUInt())
        standingWaveArea.name(reader.readUShortPrefixedString())
        standingWaveArea.layerName(reader.readUShortPrefixedString())
        standingWaveArea.uvScrollSpeed(reader.readFloat())
        standingWaveArea.useAdditiveBlending(reader.readBoolean())

        val numberOfPoints = reader.readUInt()

        val points = mutableListOf<Vector2>()
        for (i in 0u until numberOfPoints step 1) {
            points.add(
                Vector2(
                    x = reader.readFloat(),
                    y = reader.readFloat()
                )
            )
        }
        standingWaveArea.points(points)

        val unknown = reader.readUInt()
        if (unknown != 0u) {
            throw InvalidDataException("Expected unknown to equal '0' but was '$unknown'.")
        }
        standingWaveArea.unknown(unknown)

        if (version < 3u) {
            standingWaveArea.finalWidth(reader.readUInt())
            standingWaveArea.finalHeight(reader.readUInt())
            standingWaveArea.initialWidthFraction(reader.readUInt())
            standingWaveArea.initialHeightFraction(reader.readUInt())
            standingWaveArea.initialVelocity(reader.readUInt())
            standingWaveArea.timeToFade(reader.readUInt())
            standingWaveArea.timeToCompress(reader.readUInt())
            standingWaveArea.timeOffset2ndWave(reader.readUInt())
            standingWaveArea.distanceFromShore(reader.readUInt())
            standingWaveArea.texture(reader.readUShortPrefixedString())

            if (version == 2.toUShort()) {
                standingWaveArea.enablePcaWave(reader.readUIntAsBoolean())
            }
        }

        if (version >= 4u) {
            standingWaveArea.waveParticleFXName(reader.readUShortPrefixedString())
        }

        return standingWaveArea.build()
    }
}
