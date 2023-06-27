package de.darkatra.bfme2.map.serialization

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class MapFileWriterTest {

    @Test
    fun `should write map`() {

        val inputMapFile = TestUtils.getInputStream(TestUtils.UNCOMPRESSED_MAP_PATH)
        val parsedMapFile = MapFileReader().read(inputMapFile)
        val writtenMapFile = ByteArrayOutputStream()

        MapFileWriter().write(writtenMapFile, parsedMapFile, MapFileCompression.UNCOMPRESSED)

        ByteArrayInputStream(writtenMapFile.toByteArray()).use {
            // TODO: checking for same size is probably sufficient, this allows us to reorder assets if we wanted to
            assertThat(it).hasSameContentAs(inputMapFile)
        }
    }
}
