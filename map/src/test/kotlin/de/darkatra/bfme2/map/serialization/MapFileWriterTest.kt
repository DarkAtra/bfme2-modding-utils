package de.darkatra.bfme2.map.serialization

import com.google.common.io.ByteStreams
import de.darkatra.bfme2.map.MapFile
import de.darkatra.bfme2.map.MapFileCompression
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
    fun `should have the same byte size when writing zlib compressed maps`() {

        val mapFilePath = "/maps/bfme2-rotwk/Legendary War.zlib"
        val parsedMapFile = TestUtils.getInputStream(mapFilePath).use(MapFileReader()::read)

        val writtenBytes = ByteArrayOutputStream().use {
            MapFileWriter().write(it, parsedMapFile, MapFileCompression.ZLIB)
            it.toByteArray()
        }

        val writtenMapFile = writtenBytes.inputStream().use(MapFileReader()::read)

        assertMapsAreEqual(writtenMapFile, parsedMapFile)
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

        assertMapsAreEqual(writtenMapFile, parsedMapFile)
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

        assertMapsAreEqual(writtenMapFile, parsedMapFile)
    }

    private fun assertMapsAreEqual(actual: MapFile, expected: MapFile) {

        assertThat(actual.blendTileData).isEqualTo(expected.blendTileData)
        assertThat(actual.buildLists).isEqualTo(expected.buildLists)
        assertThat(actual.cameraAnimations).isEqualTo(expected.cameraAnimations)
        assertThat(actual.cameras).isEqualTo(expected.cameras)
        assertThat(actual.environmentData).isEqualTo(expected.environmentData)
        assertThat(actual.globalLighting).isEqualTo(expected.globalLighting)
        assertThat(actual.heightMap).isEqualTo(expected.heightMap)
        assertThat(actual.libraryMapsList).isEqualTo(expected.libraryMapsList)
        assertThat(actual.multiplayerPositions).isEqualTo(expected.multiplayerPositions)
        assertThat(actual.objects).isEqualTo(expected.objects)
        assertThat(actual.playerScriptsList).isEqualTo(expected.playerScriptsList)
        assertThat(actual.postEffects).isEqualTo(expected.postEffects)
        assertThat(actual.riverAreas).isEqualTo(expected.riverAreas)
        assertThat(actual.sides).isEqualTo(expected.sides)
        assertThat(actual.standingWaterAreas).isEqualTo(expected.standingWaterAreas)
        assertThat(actual.teams).isEqualTo(expected.teams)
        assertThat(actual.triggerAreas).isEqualTo(expected.triggerAreas)
        assertThat(actual.worldInfo).isEqualTo(expected.worldInfo)
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
