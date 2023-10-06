package de.darkatra.bfme2.map.serialization

import com.google.common.io.ByteStreams
import de.darkatra.bfme2.map.MapFileCompression
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream

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

        assertThat(writtenMapFile.blendTileData).isEqualTo(parsedMapFile.blendTileData)
        assertThat(writtenMapFile.buildLists).isEqualTo(parsedMapFile.buildLists)
        assertThat(writtenMapFile.cameraAnimations).isEqualTo(parsedMapFile.cameraAnimations)
        assertThat(writtenMapFile.cameras).isEqualTo(parsedMapFile.cameras)
        assertThat(writtenMapFile.environmentData).isEqualTo(parsedMapFile.environmentData)
        assertThat(writtenMapFile.globalLighting).isEqualTo(parsedMapFile.globalLighting)
        assertThat(writtenMapFile.heightMap).isEqualTo(parsedMapFile.heightMap)
        assertThat(writtenMapFile.libraryMapsList).isEqualTo(parsedMapFile.libraryMapsList)
        assertThat(writtenMapFile.multiplayerPositions).isEqualTo(parsedMapFile.multiplayerPositions)
        assertThat(writtenMapFile.objects).isEqualTo(parsedMapFile.objects)
        assertThat(writtenMapFile.playerScriptsList).isEqualTo(parsedMapFile.playerScriptsList)
        assertThat(writtenMapFile.postEffects).isEqualTo(parsedMapFile.postEffects)
        assertThat(writtenMapFile.riverAreas).isEqualTo(parsedMapFile.riverAreas)
        assertThat(writtenMapFile.sides).isEqualTo(parsedMapFile.sides)
        assertThat(writtenMapFile.standingWaterAreas).isEqualTo(parsedMapFile.standingWaterAreas)
        assertThat(writtenMapFile.teams).isEqualTo(parsedMapFile.teams)
        assertThat(writtenMapFile.triggerAreas).isEqualTo(parsedMapFile.triggerAreas)
        assertThat(writtenMapFile.worldInfo).isEqualTo(parsedMapFile.worldInfo)
    }

    @Test
    fun `should produce identical map file when writing a parsed map`() {

        val parsedMapFile = TestUtils.getInputStream(TestUtils.UNCOMPRESSED_MAP_PATH).use(MapFileReader()::read)

        val writtenBytes = ByteArrayOutputStream().use {
            MapFileWriter().write(it, parsedMapFile, MapFileCompression.UNCOMPRESSED)
            it.toByteArray()
        }

        val writtenMapFile = writtenBytes.inputStream().use(MapFileReader()::read)

        assertThat(writtenMapFile.blendTileData).isEqualTo(parsedMapFile.blendTileData)
        assertThat(writtenMapFile.buildLists).isEqualTo(parsedMapFile.buildLists)
        assertThat(writtenMapFile.cameraAnimations).isEqualTo(parsedMapFile.cameraAnimations)
        assertThat(writtenMapFile.cameras).isEqualTo(parsedMapFile.cameras)
        assertThat(writtenMapFile.environmentData).isEqualTo(parsedMapFile.environmentData)
        assertThat(writtenMapFile.globalLighting).isEqualTo(parsedMapFile.globalLighting)
        assertThat(writtenMapFile.heightMap).isEqualTo(parsedMapFile.heightMap)
        assertThat(writtenMapFile.libraryMapsList).isEqualTo(parsedMapFile.libraryMapsList)
        assertThat(writtenMapFile.multiplayerPositions).isEqualTo(parsedMapFile.multiplayerPositions)
        assertThat(writtenMapFile.objects).isEqualTo(parsedMapFile.objects)
        assertThat(writtenMapFile.playerScriptsList).isEqualTo(parsedMapFile.playerScriptsList)
        assertThat(writtenMapFile.postEffects).isEqualTo(parsedMapFile.postEffects)
        assertThat(writtenMapFile.riverAreas).isEqualTo(parsedMapFile.riverAreas)
        assertThat(writtenMapFile.sides).isEqualTo(parsedMapFile.sides)
        assertThat(writtenMapFile.standingWaterAreas).isEqualTo(parsedMapFile.standingWaterAreas)
        assertThat(writtenMapFile.teams).isEqualTo(parsedMapFile.teams)
        assertThat(writtenMapFile.triggerAreas).isEqualTo(parsedMapFile.triggerAreas)
        assertThat(writtenMapFile.worldInfo).isEqualTo(parsedMapFile.worldInfo)
    }
}
