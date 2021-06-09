package de.darkatra.bfme2.map.writer

import de.darkatra.bfme2.map.AssetListItem
import de.darkatra.bfme2.map.MapFile
import de.darkatra.bfme2.map.reader.MapFileReader
import de.darkatra.bfme2.map.reader.MapFileReaderTest
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import kotlin.test.assertEquals

internal class MapFileWriterTest {

	private val uncompressedMapPath = MapFileReaderTest::class.java.getResourceAsStream("/maps/bfme2-rotwk/Legendary War.txt")!!

	@Test
	internal fun shouldWriteMap() {

		val map = MapFile.Builder(MapFileReader().read(uncompressedMapPath))
			.assetList(mutableListOf(
				AssetListItem(10u, 20u),
				AssetListItem(30u, 40u),
				AssetListItem(50u, 60u),
				AssetListItem(70u, 80u)
			))
			.build()

		val memory = ByteArrayOutputStream()

		MapFileWriter().write(map, memory)

		val map2 = MapFileReader().readPartial(memory.toByteArray().inputStream().buffered())

		assertEquals(map.assetList, map2.assetList)
		assertEquals(map.globalVersion, map2.globalVersion)
		assertEquals(map.heightMap, map2.heightMap)
	}
}
