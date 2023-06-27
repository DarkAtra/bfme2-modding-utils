package de.darkatra.bfme2.map.serialization

import com.google.common.io.ByteStreams
import de.darkatra.bfme2.map.MapFile
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class MapFileSerdeTest {

    @Test
    fun `should calculate the correct map file size`() {

        val expectedMapFileSize = ByteStreams.exhaust(TestUtils.getInputStream(TestUtils.UNCOMPRESSED_MAP_PATH))

        val map = MapFileReader().read(TestUtils.getInputStream(TestUtils.UNCOMPRESSED_MAP_PATH))

        val serializationContext = SerializationContext(true)
        val annotationProcessingContext = AnnotationProcessingContext(false)
        val serdeFactory = SerdeFactory(annotationProcessingContext, serializationContext)
        val mapFileSerde = serdeFactory.getSerde(MapFile::class)

        val actualMapFileDataSection = mapFileSerde.collectDataSections(map)

        // TODO: write the MapFile via MapFileWriter, then compare the file sizes (this makes it include the assetNames)
        // 1382 is the byte count of the assetNames for this particular map
        assertThat(actualMapFileDataSection.size + 1382).isEqualTo(expectedMapFileSize)
    }
}
