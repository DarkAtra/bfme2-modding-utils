package de.darkatra.bfme2.map.reader

import de.darkatra.bfme2.map.MapFile
import org.apache.commons.io.input.CountingInputStream

class PostEffectReader : AssetReader {

	companion object {
		const val ASSET_NAME = "PostEffectsChunk"
	}

	override fun read(reader: CountingInputStream, context: MapFileParseContext, builder: MapFile.Builder) {


	}
}
