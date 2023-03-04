package de.darkatra.bfme2.map.reader

import de.darkatra.bfme2.Color
import de.darkatra.bfme2.Vector3
import de.darkatra.bfme2.map.globallighting.TimeOfDay
import de.darkatra.bfme2.map.`object`.RoadType
import de.darkatra.bfme2.map.scripting.ScriptConditionType
import de.darkatra.bfme2.map.serialization.MapFileReader
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.InputStream

internal class MapFileReaderTest {

    private val uncompressedMapPath = "/maps/bfme2-rotwk/Legendary War.txt"
    private val refpackCompressedMapPath = "/maps/bfme2-rotwk/Legendary War.refpack"
    private val zlibCompressedMapPath = "/maps/bfme2-rotwk/Legendary War.zlib"

    @Test
    internal fun shouldReadMap() {

        val map = MapFileReader().read(getMapInputStream(uncompressedMapPath))

        assertThat(map.blendTileData.numberOfTiles).isEqualTo(640u * 640u)
        assertThat(map.blendTileData.textures).hasSize(4)
        assertThat(map.blendTileData.textures.map { it.name }).containsExactly("LothlorienGrass05", "CliffLothlorien01", "AsphaltType1", "GrassIsengard19")
        assertThat(map.buildLists.buildLists).hasSize(17)
        assertThat(map.cameraAnimations.animations).hasSize(0)
        assertThat(map.cameras.cameras).hasSize(0)
        assertThat(map.environmentData).isNotNull
        assertThat(map.environmentData.waterMaxAlphaDepth).isEqualTo(3f)
        assertThat(map.environmentData.deepWaterAlpha).isEqualTo(1f)
        assertThat(map.environmentData.isMacroTextureStretched).isEqualTo(false)
        assertThat(map.environmentData.macroTexture).isEqualTo("TSNoiseUrb.tga")
        assertThat(map.environmentData.cloudTexture).isEqualTo("TSCloudMed.tga")
        assertThat(map.globalLighting.time).isEqualTo(TimeOfDay.AFTERNOON)
        assertThat(map.globalLighting.lightingConfigurations).hasSize(4)
        assertThat(map.globalLighting.shadowColor).isEqualTo(Color(0, 0, 0, 64))
        assertThat(map.globalLighting.unknown).hasSize(44)
        assertThat(map.globalLighting.noCloudFactor).isEqualTo(Vector3(1f, 1f, 1f))
        assertThat(map.heightMap.width).isEqualTo(640u)
        assertThat(map.heightMap.height).isEqualTo(640u)
        assertThat(map.heightMap.borderWidth).isEqualTo(20u)
        assertThat(map.heightMap.borders).hasSize(1)
        assertThat(map.heightMap.borders.first().x).isEqualTo(600u)
        assertThat(map.heightMap.borders.first().y).isEqualTo(600u)
        assertThat(map.heightMap.elevations.size()).isEqualTo(640 * 640)
        assertThat(map.heightMap.area).isEqualTo(640u * 640u)
        assertThat(map.libraryMapsList.libraryMaps).hasSize(17)
        assertThat(map.multiplayerPositions.positions).hasSize(8)
        assertThat(map.objects.objects).hasSize(666)
        assertThat(map.playerScriptsList.scriptLists).hasSize(17)
        assertThat(map.postEffects.postEffects).hasSize(0)
        assertThat(map.riverAreas.areas).hasSize(0)
        assertThat(map.sides.unknown).isEqualTo(true)
        assertThat(map.sides.players).hasSize(17)
        assertThat(map.standingWaterAreas.areas).hasSize(0)
        assertThat(map.standingWaveAreas.areas).hasSize(0)
        assertThat(map.teams.teams).hasSize(17)
        assertThat(map.triggerAreas.areas).hasSize(0)
        assertThat(map.worldInfo.properties).hasSize(12)
        assertThat(map.worldInfo["cameraMaxHeight"]!!.value).isEqualTo(800f)
    }

    @Test
    internal fun shouldReadBfme2MapWithStrangeRoadType() {

        val map = MapFileReader().read(getMapInputStream("/maps/bfme2-rotwk/map mp harlond.zlib"))

        assertThat(map).isNotNull
        assertThat(map.objects.objects.any { it.roadType == RoadType.UNKNOWN_5 }).isTrue
    }

    @Test
    internal fun shouldReadBfme2MapWithStandingWaveAreas() {

        val map = MapFileReader().read(getMapInputStream("/maps/bfme2-rotwk/map mp midgewater.zlib"))

        assertThat(map).isNotNull
        assertThat(map.standingWaveAreas.areas).hasSize(2)
    }

    @Test
    internal fun shouldReadBfme2MapWithScripts() {

        val map = MapFileReader().read(getMapInputStream("/maps/bfme2-rotwk/script.map"))

        assertThat(map.playerScriptsList.scriptLists).isNotEmpty
        assertThat(map.playerScriptsList.scriptLists[0].scriptListEntries).isNotEmpty
        assertThat(map.playerScriptsList.scriptLists[0].scriptListEntries[0].name).isEqualTo("TestFolder")
        assertThat(map.playerScriptsList.scriptLists[0].scriptFolders).isNotEmpty
        assertThat(map.playerScriptsList.scriptLists[0].scriptFolders[0].scripts).isNotEmpty
        assertThat(map.playerScriptsList.scriptLists[0].scriptFolders[0].scripts[0].name).isEqualTo("TestScript1")
        assertThat(map.playerScriptsList.scriptLists[0].scriptFolders[0].scripts[0].activeInEasy).isTrue
        assertThat(map.playerScriptsList.scriptLists[0].scriptFolders[0].scripts[0].activeInMedium).isTrue
        assertThat(map.playerScriptsList.scriptLists[0].scriptFolders[0].scripts[0].activeInHard).isTrue
        assertThat(map.playerScriptsList.scriptLists[0].scriptFolders[0].scripts[0].orConditions).isNotEmpty
        assertThat(map.playerScriptsList.scriptLists[0].scriptFolders[0].scripts[0].orConditions[0].conditions).isNotEmpty
        assertThat(map.playerScriptsList.scriptLists[0].scriptFolders[0].scripts[0].orConditions[0].conditions[0].type).isEqualTo(ScriptConditionType.TRUE)
        assertThat(map.playerScriptsList.scriptLists[0].scriptFolders[0].scripts[0].orConditions[0].conditions[0].inverted).isFalse
        assertThat(map.playerScriptsList.scriptLists[0].scriptFolders[0].scripts[0].falseActions).isEmpty()
        assertThat(map.playerScriptsList.scriptLists[0].scriptFolders[0].scripts[0].actions).isNotEmpty
        assertThat(map.playerScriptsList.scriptLists[0].scriptFolders[0].scripts[0].actions[0].enabled).isTrue
        assertThat(map.playerScriptsList.scriptLists[0].scriptFolders[0].scripts[1].name).isEqualTo("TestScript2")
        assertThat(map.playerScriptsList.scriptLists[0].scriptFolders[0].scripts[1].activeInEasy).isTrue
        assertThat(map.playerScriptsList.scriptLists[0].scriptFolders[0].scripts[1].activeInMedium).isFalse
        assertThat(map.playerScriptsList.scriptLists[0].scriptFolders[0].scripts[1].activeInHard).isFalse
        assertThat(map.playerScriptsList.scriptLists[0].scriptFolders[0].scripts[1].orConditions).isNotEmpty
        assertThat(map.playerScriptsList.scriptLists[0].scriptFolders[0].scripts[1].orConditions[0].conditions).isNotEmpty
        assertThat(map.playerScriptsList.scriptLists[0].scriptFolders[0].scripts[1].orConditions[0].conditions[0].type).isEqualTo(ScriptConditionType.TRUE)
        assertThat(map.playerScriptsList.scriptLists[0].scriptFolders[0].scripts[1].orConditions[0].conditions[0].inverted).isTrue
        assertThat(map.playerScriptsList.scriptLists[0].scriptFolders[0].scripts[1].actions).isEmpty()
        assertThat(map.playerScriptsList.scriptLists[0].scriptFolders[0].scripts[1].falseActions).isNotEmpty
        assertThat(map.playerScriptsList.scriptLists[0].scriptFolders[0].scripts[1].falseActions[0].enabled).isTrue
    }

    @Test
    internal fun shouldReadBfme2MapWithScriptThatChecksForActiveGameModes() {

        val map = MapFileReader().read(getMapInputStream("/maps/bfme2-rotwk/map mp westmarch.zlib"))

        assertThat(map.playerScriptsList.scriptLists).isNotEmpty
        assertThat(map.playerScriptsList.scriptLists[2].scriptFolders).isNotEmpty
        assertThat(map.playerScriptsList.scriptLists[2].scriptFolders[0].name).isEqualTo("SkirmishGollum_Spawn")
        assertThat(map.playerScriptsList.scriptLists[2].scriptFolders[0].scripts).isNotEmpty
        assertThat(map.playerScriptsList.scriptLists[2].scriptFolders[0].scripts[0].name).isEqualTo("SkirmishGollum_PickSpawnPoint")
        assertThat(map.playerScriptsList.scriptLists[2].scriptFolders[0].scripts[0].orConditions).isNotEmpty
        assertThat(map.playerScriptsList.scriptLists[2].scriptFolders[0].scripts[0].orConditions[0].conditions).isNotEmpty
        assertThat(map.playerScriptsList.scriptLists[2].scriptFolders[0].scripts[0].orConditions[0].conditions[0].type).isEqualTo(ScriptConditionType.IS_GAME_MODE_ACTIVE)
    }

    @Test
    internal fun shouldReadTheSameMapInformationForAllCompressions() {

        val plain = MapFileReader().read(getMapInputStream(uncompressedMapPath))
        val refpack = MapFileReader().read(getMapInputStream(refpackCompressedMapPath))
        val zlib = MapFileReader().read(getMapInputStream(zlibCompressedMapPath))

        assertThat(plain).isEqualTo(refpack)
        assertThat(plain).isEqualTo(zlib)
    }

    private fun getMapInputStream(name: String): InputStream {
        return MapFileReaderTest::class.java.getResourceAsStream(name)!!
    }
}
