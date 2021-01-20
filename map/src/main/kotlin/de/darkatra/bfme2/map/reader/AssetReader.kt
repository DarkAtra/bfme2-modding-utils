package de.darkatra.bfme2.map.reader

import de.darkatra.bfme2.map.MapFile
import org.apache.commons.io.input.CountingInputStream

// TODO: find a better name for this
interface AssetReader {

	fun read(reader: CountingInputStream, context: MapFileParseContext, builder: MapFile.Builder)
}
