package de.darkatra.bfme2.map.serialization

import com.google.common.io.ByteStreams
import de.darkatra.bfme2.map.MapFileCompression
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream

class MapFileWriterTest {

    @Test
    fun `should write map`() {

        val expectedMapFileSize = TestUtils.getInputStream(TestUtils.UNCOMPRESSED_MAP_PATH).use(ByteStreams::exhaust)
        val parsedMapFile = TestUtils.getInputStream(TestUtils.UNCOMPRESSED_MAP_PATH).use(MapFileReader()::read)

        val mapFileOutputStream = ByteArrayOutputStream().use {
            MapFileWriter().write(it, parsedMapFile, MapFileCompression.UNCOMPRESSED)
            it
        }

        assertThat(mapFileOutputStream.size()).isEqualTo(expectedMapFileSize)
    }

    @Test
    fun `should produce identical map file when writing a parsed map`() {

        val parsedMapFile = MapFileReader().read(TestUtils.getInputStream(TestUtils.UNCOMPRESSED_MAP_PATH))

        val mapFileOutputStream = ByteArrayOutputStream()
        MapFileWriter().write(mapFileOutputStream, parsedMapFile, MapFileCompression.UNCOMPRESSED)

        val writtenMapFile = MapFileReader().read(mapFileOutputStream.toByteArray().inputStream())

        assertThat(parsedMapFile.worldInfo).isEqualTo(writtenMapFile.worldInfo)
    }
}
