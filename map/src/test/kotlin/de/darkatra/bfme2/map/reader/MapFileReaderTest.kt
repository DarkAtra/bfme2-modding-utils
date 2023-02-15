package de.darkatra.bfme2.map.reader

import de.darkatra.bfme2.Color
import de.darkatra.bfme2.Vector3
import de.darkatra.bfme2.map.RoadType
import de.darkatra.bfme2.map.ScriptConditionType
import de.darkatra.bfme2.map.TimeOfDay
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.InputStream

internal class MapFileReaderTest {

    private val uncompressedMapPath = "/maps/bfme2-rotwk/Legendary War.txt"
    private val refpackCompressedMapPath = "/maps/bfme2-rotwk/Legendary War.refpack"
    private val zlibCompressedMapPath = "/maps/bfme2-rotwk/Legendary War.zlib"

    @Test
    internal fun shouldYieldSameResultsForOldAndNewReaderImplementation() {

        // old reader
        val mapFile = MapFileReader().read(getMapInputStream(uncompressedMapPath))

        // new reader
        val newMapFile = de.darkatra.bfme2.v2.map.deserialization.MapFileReader().read(getMapInputStream(uncompressedMapPath))

        assertThat(newMapFile.assetNameRegistry!!.assetNames).isEqualTo(mapFile.assetNames)
        assertThat(newMapFile.heightMapV5!!.width).isEqualTo(mapFile.heightMap.width)
        assertThat(newMapFile.heightMapV5!!.height).isEqualTo(mapFile.heightMap.height)
        assertThat(newMapFile.worldInfo!!.properties.size).isEqualTo(mapFile.worldSettings.size)
    }

    @Test
    internal fun shouldReadBfme1MapWithSkyboxSettings() {

        val map = MapFileReader().read(getMapInputStream("/maps/bfme1/skybox.map"))

        assertThat(map.skybox).isNotNull
        map.skybox!!
        assertThat(map.skybox.position.x).isEqualTo(500.5f)
        assertThat(map.skybox.position.y).isEqualTo(393.5f)
        assertThat(map.skybox.position.z).isEqualTo(97.5f)
        assertThat(map.skybox.scale).isEqualTo(2.5f)
        assertThat(map.skybox.rotation).isEqualTo(10.5f)
        assertThat(map.skybox.textureScheme).isEqualTo("MountainSnow")
    }

    @Test
    internal fun shouldReadBfme2MapWithStrangeRoadType() {

        val map = MapFileReader().read(getMapInputStream("/maps/bfme2-rotwk/map mp harlond.zlib"))

        assertThat(map).isNotNull
        assertThat(map.objects.any { it.roadType == RoadType.UNKNOWN_5 }).isTrue
    }

    @Test
    internal fun shouldReadBfme2MapWithStandingWaveAreas() {

        val map = MapFileReader().read(getMapInputStream("/maps/bfme2-rotwk/map mp midgewater.zlib"))

        assertThat(map).isNotNull
        assertThat(map.standingWaveAreas).hasSize(2)
    }

    @Test
    internal fun shouldReadBfme2MapWithScripts() {

        val map = MapFileReader().read(getMapInputStream("/maps/bfme2-rotwk/script.map"))

        assertThat(map.playerScripts).isNotEmpty
        map.playerScripts!!
        assertThat(map.playerScripts[0].scriptFolders).isNotEmpty
        assertThat(map.playerScripts[0].scriptFolders[0].name).isEqualTo("TestFolder")
        assertThat(map.playerScripts[0].scriptFolders[0].scripts).isNotEmpty
        assertThat(map.playerScripts[0].scriptFolders[0].scripts[0].name).isEqualTo("TestScript1")
        assertThat(map.playerScripts[0].scriptFolders[0].scripts[0].activeInEasy).isTrue
        assertThat(map.playerScripts[0].scriptFolders[0].scripts[0].activeInMedium).isTrue
        assertThat(map.playerScripts[0].scriptFolders[0].scripts[0].activeInHard).isTrue
        assertThat(map.playerScripts[0].scriptFolders[0].scripts[0].orConditions).isNotEmpty
        assertThat(map.playerScripts[0].scriptFolders[0].scripts[0].orConditions[0].conditions).isNotEmpty
        assertThat(map.playerScripts[0].scriptFolders[0].scripts[0].orConditions[0].conditions[0].type).isEqualTo(ScriptConditionType.TRUE)
        assertThat(map.playerScripts[0].scriptFolders[0].scripts[0].orConditions[0].conditions[0].inverted).isFalse
        assertThat(map.playerScripts[0].scriptFolders[0].scripts[0].actionsIfFalse).isEmpty()
        assertThat(map.playerScripts[0].scriptFolders[0].scripts[0].actionsIfTrue).isNotEmpty
        assertThat(map.playerScripts[0].scriptFolders[0].scripts[0].actionsIfTrue[0].enabled).isTrue
        assertThat(map.playerScripts[0].scriptFolders[0].scripts[1].name).isEqualTo("TestScript2")
        assertThat(map.playerScripts[0].scriptFolders[0].scripts[1].activeInEasy).isTrue
        assertThat(map.playerScripts[0].scriptFolders[0].scripts[1].activeInMedium).isFalse
        assertThat(map.playerScripts[0].scriptFolders[0].scripts[1].activeInHard).isFalse
        assertThat(map.playerScripts[0].scriptFolders[0].scripts[1].orConditions).isNotEmpty
        assertThat(map.playerScripts[0].scriptFolders[0].scripts[1].orConditions[0].conditions).isNotEmpty
        assertThat(map.playerScripts[0].scriptFolders[0].scripts[1].orConditions[0].conditions[0].type).isEqualTo(ScriptConditionType.TRUE)
        assertThat(map.playerScripts[0].scriptFolders[0].scripts[1].orConditions[0].conditions[0].inverted).isTrue
        assertThat(map.playerScripts[0].scriptFolders[0].scripts[1].actionsIfTrue).isEmpty()
        assertThat(map.playerScripts[0].scriptFolders[0].scripts[1].actionsIfFalse).isNotEmpty
        assertThat(map.playerScripts[0].scriptFolders[0].scripts[1].actionsIfFalse[0].enabled).isTrue
    }

    @Test
    internal fun shouldReadBfme2MapWithScriptThatChecksForActiveGameModes() {

        val map = MapFileReader().read(getMapInputStream("/maps/bfme2-rotwk/map mp westmarch.zlib"))

        assertThat(map.playerScripts).isNotEmpty
        map.playerScripts!!
        assertThat(map.playerScripts[2].scriptFolders).isNotEmpty
        assertThat(map.playerScripts[2].scriptFolders[0].name).isEqualTo("SkirmishGollum_Spawn")
        assertThat(map.playerScripts[2].scriptFolders[0].scripts).isNotEmpty
        assertThat(map.playerScripts[2].scriptFolders[0].scripts[0].name).isEqualTo("SkirmishGollum_PickSpawnPoint")
        assertThat(map.playerScripts[2].scriptFolders[0].scripts[0].orConditions).isNotEmpty
        assertThat(map.playerScripts[2].scriptFolders[0].scripts[0].orConditions[0].conditions).isNotEmpty
        assertThat(map.playerScripts[2].scriptFolders[0].scripts[0].orConditions[0].conditions[0].type).isEqualTo(ScriptConditionType.IS_GAME_MODE_ACTIVE)
    }

    @Test
    internal fun shouldReadMap() {

        val map = MapFileReader().read(getMapInputStream(uncompressedMapPath))

        assertThat(map.assetList).isNull()
        assertThat(map.buildLists).hasSize(17)
        assertThat(map.cameraAnimations).hasSize(0)
        assertThat(map.cameras).hasSize(0)
        assertThat(map.castleData).isNull()
        assertThat(map.environmentData).isNotNull
        map.environmentData!!
        assertThat(map.environmentData.waterMaxAlphaDepth).isEqualTo(3f)
        assertThat(map.environmentData.deepWaterAlpha).isEqualTo(1f)
        assertThat(map.environmentData.isMacroTextureStretched).isEqualTo(false)
        assertThat(map.environmentData.macroTexture).isEqualTo("TSNoiseUrb.tga")
        assertThat(map.environmentData.cloudTexture).isEqualTo("TSCloudMed.tga")
        assertThat(map.environmentData.unknownTexture).isNull()
        assertThat(map.environmentData.unknownTexture2).isNull()
        assertThat(map.fogSettings).isNull()
        assertThat(map.globalLighting.time).isEqualTo(TimeOfDay.AFTERNOON)
        assertThat(map.globalLighting.lightingConfigurations).hasSize(4)
        assertThat(map.globalLighting.shadowColor).isEqualTo(Color(0, 0, 0, 64))
        assertThat(map.globalLighting.unknown).hasSize(44)
        assertThat(map.globalLighting.unknown2).isNull()
        assertThat(map.globalLighting.unknown3).isNull()
        assertThat(map.globalLighting.noCloudFactor).isEqualTo(Vector3(1f, 1f, 1f))
        assertThat(map.globalVersion).isNull()
        assertThat(map.globalWaterSettings).isNull()
        assertThat(map.heightMap.width).isEqualTo(640u)
        assertThat(map.heightMap.height).isEqualTo(640u)
        assertThat(map.heightMap.borderWidth).isEqualTo(20u)
        assertThat(map.heightMap.borders).hasSize(1)
        assertThat(map.heightMap.elevations).hasSize(640)
        assertThat(map.heightMap.area).isEqualTo(640u * 640u)
        assertThat(map.libraryMaps).hasSize(17)
        assertThat(map.missionHotSpots).isNull()
        assertThat(map.missionObjectives).isNull()
        assertThat(map.multiplayerPositions).hasSize(8)
        assertThat(map.objects).hasSize(666)
        assertThat(map.players).hasSize(17)
        assertThat(map.playerScripts).hasSize(17)
        assertThat(map.polygonTriggers).isNull()
        assertThat(map.postEffects).hasSize(0)
        assertThat(map.riverAreas).hasSize(0)
        assertThat(map.skybox).isNull()
        assertThat(map.standingWaterAreas).hasSize(0)
        assertThat(map.standingWaveAreas).hasSize(0)
        assertThat(map.teams).hasSize(17)
        assertThat(map.triggerAreas).hasSize(0)
        assertThat(map.unknown).isTrue
        assertThat(map.waypointPaths).hasSize(0)
        assertThat(map.worldSettings).hasSize(12)
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
