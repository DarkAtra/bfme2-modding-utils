package de.darkatra.bfme2.map.reader

import de.darkatra.bfme2.map.Player
import org.apache.commons.io.input.CountingInputStream

class PlayerReader(
    private val buildListReader: BuildListReader,
    private val propertiesReader: PropertiesReader
) {

    fun read(reader: CountingInputStream, context: MapFileParseContext): Player {

        val properties = propertiesReader.read(reader, context)
        val buildListItems = buildListReader.readBuildListItems(reader, context)

        return Player(
            buildListItems = buildListItems,
            properties = properties
        )
    }
}
