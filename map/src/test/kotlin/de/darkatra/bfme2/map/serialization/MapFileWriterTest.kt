package de.darkatra.bfme2.map.serialization

import com.google.common.io.ByteStreams
import de.darkatra.bfme2.map.MapFileCompression
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream

class MapFileWriterTest {

    @Test
    fun `should write map`() {

        val expectedMapFileSize = ByteStreams.exhaust(TestUtils.getInputStream(TestUtils.UNCOMPRESSED_MAP_PATH))

        val inputMapFile = TestUtils.getInputStream(TestUtils.UNCOMPRESSED_MAP_PATH)
        val parsedMapFile = MapFileReader().read(inputMapFile)
        val writtenMapFile = ByteArrayOutputStream()

        MapFileWriter().write(writtenMapFile, parsedMapFile, MapFileCompression.UNCOMPRESSED)

        assertThat(writtenMapFile.size()).isEqualTo(expectedMapFileSize)
    }
}
