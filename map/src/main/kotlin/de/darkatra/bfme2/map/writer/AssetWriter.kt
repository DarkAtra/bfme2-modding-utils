package de.darkatra.bfme2.map.writer

import de.darkatra.bfme2.map.MapFile
import org.apache.commons.io.output.CountingOutputStream

// TODO: make this generic?
interface AssetWriter {

	// TODO: does it make sense the pass the version of the asset? maybe even as constructor parameter
	// this way the MapFile class is version independent and the user has to decide for which game a map should be serialized
	// of course, this is not trivial if the actual data has to be converted
	fun write(writer: CountingOutputStream, context: MapFileComposeContext, mapFile: MapFile)

	fun composeAssetNames(context: MapFileComposeContext, mapFile: MapFile)
}
