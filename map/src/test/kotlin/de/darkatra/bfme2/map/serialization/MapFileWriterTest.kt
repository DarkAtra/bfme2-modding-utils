package de.darkatra.bfme2.map.serialization

import com.google.common.io.ByteStreams
import de.darkatra.bfme2.map.MapFileCompression
import de.darkatra.bfme2.toLittleEndianInt
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.io.ByteArrayOutputStream
import java.util.stream.Stream

class MapFileWriterTest {

    @Test
    fun `should have the same byte size when writing uncompressed maps`() {

        val mapFilePath = TestUtils.UNCOMPRESSED_MAP_PATH
        val expectedMapFileSize = TestUtils.getInputStream(mapFilePath).use(ByteStreams::exhaust)
        val parsedMapFile = TestUtils.getInputStream(mapFilePath).use(MapFileReader()::read)

        val mapFileOutputStream = ByteArrayOutputStream().use {
            MapFileWriter().write(it, parsedMapFile, MapFileCompression.UNCOMPRESSED)
            it
        }

        assertThat(mapFileOutputStream.size()).isEqualTo(expectedMapFileSize)
    }

    @Test
    fun `should have the same file size after fourCC when writing zlib compressed maps`() {

        val mapFilePath = TestUtils.ZLIB_COMPRESSED_MAP_PATH

        // fileSize in file (after the initial fourCC) - original map
        val expectedFileSizeAfterFourCC = TestUtils.getInputStream(mapFilePath).readBytes().drop(4).take(4).toByteArray().toLittleEndianInt()

        // fileSize in file (after the initial fourCC) - edited map
        val actualFileSizeAfterFourCC = ByteArrayOutputStream().use {
            MapFileWriter().write(it, TestUtils.getInputStream(mapFilePath).use(MapFileReader()::read), MapFileCompression.ZLIB)
            it.toByteArray()
        }.drop(4).take(4).toByteArray().toLittleEndianInt()

        assertThat(actualFileSizeAfterFourCC).isEqualTo(expectedFileSizeAfterFourCC)
        assertThat(actualFileSizeAfterFourCC).isEqualTo(10059841)
    }

    @ParameterizedTest
    @MethodSource("de.darkatra.bfme2.map.serialization.MapFileWriterTestKt#mapsToRoundtrip")
    fun `should produce identical map file when writing a parsed map without compression`(mapPath: String) {

        val parsedMapFile = TestUtils.getInputStream(mapPath).use(MapFileReader()::read)

        val writtenBytes = ByteArrayOutputStream().use {
            MapFileWriter().write(it, parsedMapFile, MapFileCompression.UNCOMPRESSED)
            it.toByteArray()
        }

        val writtenMapFile = writtenBytes.inputStream().use(MapFileReader()::read)

        assertThat(writtenMapFile).isEqualTo(parsedMapFile)
    }

    @ParameterizedTest
    @MethodSource("de.darkatra.bfme2.map.serialization.MapFileWriterTestKt#mapsToRoundtrip")
    fun `should produce identical map file when writing a parsed map with zlib compression`(mapPath: String) {

        val parsedMapFile = TestUtils.getInputStream(mapPath).use(MapFileReader()::read)

        val writtenBytes = ByteArrayOutputStream().use {
            MapFileWriter().write(it, parsedMapFile, MapFileCompression.ZLIB)
            it.toByteArray()
        }

        val writtenMapFile = writtenBytes.inputStream().use(MapFileReader()::read)

        assertThat(writtenMapFile).isEqualTo(parsedMapFile)
    }

    @ParameterizedTest
    @MethodSource("de.darkatra.bfme2.map.serialization.MapFileWriterTestKt#mapsToRoundtrip")
    fun `should produce identical map file when writing a parsed map with refpack compression`(mapPath: String) {

        val parsedMapFile = TestUtils.getInputStream(mapPath).use(MapFileReader()::read)

        val writtenBytes = ByteArrayOutputStream().use {
            MapFileWriter().write(it, parsedMapFile, MapFileCompression.REFPACK)
            it.toByteArray()
        }

        val writtenMapFile = writtenBytes.inputStream().use(MapFileReader()::read)

        assertThat(writtenMapFile).isEqualTo(parsedMapFile)
    }
}

fun mapsToRoundtrip(): Stream<String> {
    return Stream.of(
        TestUtils.UNCOMPRESSED_MAP_PATH,
        "/maps/bfme2-rotwk/map mp harlindon.refpack",
        "/maps/bfme2-rotwk/map mp harlond.zlib",
        "/maps/bfme2-rotwk/map mp midgewater.zlib",
        "/maps/bfme2-rotwk/map mp westmarch.zlib",
        "/maps/bfme2-rotwk/map wor osgiliath.zlib"
    )
}
