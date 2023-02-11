package de.darkatra.bfme2.map.reader

import de.darkatra.bfme2.InvalidDataException
import de.darkatra.bfme2.Vector2
import de.darkatra.bfme2.map.AssetName
import de.darkatra.bfme2.map.MapFile
import de.darkatra.bfme2.map.TriggerArea
import de.darkatra.bfme2.readFloat
import de.darkatra.bfme2.readUInt
import de.darkatra.bfme2.readUShortPrefixedString
import org.apache.commons.io.input.CountingInputStream

class TriggerAreasReader : AssetReader {

    override fun read(reader: CountingInputStream, context: MapFileParseContext, builder: MapFile.Builder) {

        MapFileReader.readAsset(reader, context, AssetName.TRIGGER_AREAS.assetName) {

            val numberOfTriggerAreas = reader.readUInt()

            val triggerAreas = mutableListOf<TriggerArea>()
            for (i in 0u until numberOfTriggerAreas step 1) {
                triggerAreas.add(
                    readTriggerArea(reader)
                )
            }

            builder.triggerAreas(triggerAreas)
        }
    }

    @Suppress("DuplicatedCode")
    private fun readTriggerArea(reader: CountingInputStream): TriggerArea {

        val name = reader.readUShortPrefixedString()
        val layerName = reader.readUShortPrefixedString()

        val id = reader.readUInt()

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

        val unknown = reader.readUInt()
        if (unknown != 0u) {
            throw InvalidDataException("Expected unknown to equal '0' but was '$unknown'.")
        }

        return TriggerArea(
            name = name,
            layerName = layerName,
            id = id,
            points = points,
            unknown = unknown
        )
    }
}
