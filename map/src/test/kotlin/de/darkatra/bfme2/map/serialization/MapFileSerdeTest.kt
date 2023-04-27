package de.darkatra.bfme2.map.serialization

import de.darkatra.bfme2.map.MapFile
import org.apache.commons.io.IOUtils
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.io.InputStream

@Disabled // WIP
class MapFileSerdeTest {

    private val uncompressedMapPath = "/maps/bfme2-rotwk/Legendary War.txt"

    @Test
    fun `should calculate the correct map file size`() {

        val expectedMapFileSize = IOUtils.consume(getMapInputStream(uncompressedMapPath))

        val map = MapFileReader().read(getMapInputStream(uncompressedMapPath))

        val serializationContext = SerializationContext(true)
        val annotationProcessingContext = AnnotationProcessingContext(false)
        val serdeFactory = SerdeFactory(annotationProcessingContext, serializationContext)
        val mapFileSerde = serdeFactory.getSerde(MapFile::class)

        val actualMapFileSize = mapFileSerde.calculateByteCount(map)

        assertThat(actualMapFileSize).isEqualTo(expectedMapFileSize)
    }

    private fun getMapInputStream(name: String): InputStream {
        return MapFileReaderTest::class.java.getResourceAsStream(name)!!
    }
}
