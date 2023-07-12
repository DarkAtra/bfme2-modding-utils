package de.darkatra.bfme2.map.serialization

import de.darkatra.bfme2.Color
import de.darkatra.bfme2.Vector3
import de.darkatra.bfme2.map.camera.LookAtCameraAnimation
import de.darkatra.bfme2.map.globallighting.TimeOfDay
import de.darkatra.bfme2.map.`object`.RoadType
import de.darkatra.bfme2.map.scripting.ScriptConditionType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class MapFileReaderTest {

    @Test
    internal fun `should read map`() {

        val map = TestUtils.getInputStream(TestUtils.UNCOMPRESSED_MAP_PATH).use(MapFileReader()::read)

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
        assertThat(map.globalLighting.shadowColor).isEqualTo(Color(0u, 0u, 0u, 64u))
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
    internal fun `should read bfme2 map with strange road type`() {

        val map = TestUtils.getInputStream("/maps/bfme2-rotwk/map mp harlond.zlib").use(MapFileReader()::read)

        assertThat(map).isNotNull
        assertThat(map.objects.objects.any { it.roadType == RoadType.UNKNOWN_5 }).isTrue
    }

    @Test
    internal fun `should read bfme2 map with standing wave areas`() {

        val map = TestUtils.getInputStream("/maps/bfme2-rotwk/map mp midgewater.zlib").use(MapFileReader()::read)

        assertThat(map).isNotNull
        assertThat(map.standingWaveAreas.areas).hasSize(2)
    }

    @Test
    internal fun `should read bfme2 map with scripts`() {

        val map = TestUtils.getInputStream("/maps/bfme2-rotwk/script.map").use(MapFileReader()::read)

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
    internal fun `should read bfme2 map with script that checks for active game modes`() {

        val map = TestUtils.getInputStream("/maps/bfme2-rotwk/map mp westmarch.zlib").use(MapFileReader()::read)

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
    internal fun `should read the same map information for all compressions`() {

        val plain = TestUtils.getInputStream(TestUtils.UNCOMPRESSED_MAP_PATH).use(MapFileReader()::read)
        val refpack = TestUtils.getInputStream(TestUtils.REFPACK_COMPRESSED_MAP_PATH).use(MapFileReader()::read)
        val zlib = TestUtils.getInputStream(TestUtils.ZLIB_COMPRESSED_MAP_PATH).use(MapFileReader()::read)

        assertThat(plain).isEqualTo(refpack)
        assertThat(plain).isEqualTo(zlib)
    }

    @Test
    internal fun `should read map with look-at camera animation frames`() {

        val map = TestUtils.getInputStream("/maps/bfme2-rotwk/cin fornost - witchking fire.map").use(MapFileReader()::read)

        assertThat(map.cameraAnimations.animations).isNotEmpty
        assertThat(map.cameraAnimations.animations[0]).isInstanceOf(LookAtCameraAnimation::class.java)
        assertThat(map.cameraAnimations.animations[0].name).isEqualTo("Look-at Animation")
        assertThat(map.cameraAnimations.animations[0].numberOfFrames).isEqualTo(1000u)
        assertThat(map.cameraAnimations.animations[0].startOffset).isEqualTo(0u)
        assertThat((map.cameraAnimations.animations[0] as LookAtCameraAnimation).lookAtCameraFrames).hasSize(4)
        assertThat((map.cameraAnimations.animations[0] as LookAtCameraAnimation).lookAtTargetFrames).hasSize(1)
    }
}
