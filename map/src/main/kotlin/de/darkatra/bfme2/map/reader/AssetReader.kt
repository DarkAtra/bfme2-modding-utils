package de.darkatra.bfme2.map.reader

import de.darkatra.bfme2.map.MapFile
import org.apache.commons.io.input.CountingInputStream

interface AssetReader {

	fun read(reader: CountingInputStream, context: MapFileParseContext, builder: MapFile.Builder)
}
