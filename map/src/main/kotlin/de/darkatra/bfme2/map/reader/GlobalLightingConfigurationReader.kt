package de.darkatra.bfme2.map.reader

import de.darkatra.bfme2.map.GlobalLightingConfiguration
import org.apache.commons.io.input.CountingInputStream

class GlobalLightingConfigurationReader {

    companion object {
        const val MIN_VERSION_WITH_INFANTRY_SPECIFIC_LIGHT = 7u
        const val MAX_VERSION_WITH_INFANTRY_SPECIFIC_LIGHT = 9u
        const val MAX_VERSION_WITH_OBJECT_SPECIFIC_LIGHT = 9u
    }

    private val globalLightReader = GlobalLightReader()

    fun read(reader: CountingInputStream, version: UShort): GlobalLightingConfiguration {

        val globalLightingConfigurationBuilder = GlobalLightingConfiguration.Builder()

        globalLightingConfigurationBuilder.terrainSun(globalLightReader.read(reader))
        if (version <= MAX_VERSION_WITH_OBJECT_SPECIFIC_LIGHT) {
            globalLightingConfigurationBuilder.objectSun(globalLightReader.read(reader))
        }
        if (version >= MIN_VERSION_WITH_INFANTRY_SPECIFIC_LIGHT && version <= MAX_VERSION_WITH_INFANTRY_SPECIFIC_LIGHT) {
            globalLightingConfigurationBuilder.infantrySun(globalLightReader.read(reader))
        }

        globalLightingConfigurationBuilder.terrainAccent1(globalLightReader.read(reader))
        if (version <= MAX_VERSION_WITH_OBJECT_SPECIFIC_LIGHT) {
            globalLightingConfigurationBuilder.objectAccent1(globalLightReader.read(reader))
        }
        if (version >= MIN_VERSION_WITH_INFANTRY_SPECIFIC_LIGHT && version <= MAX_VERSION_WITH_INFANTRY_SPECIFIC_LIGHT) {
            globalLightingConfigurationBuilder.infantryAccent1(globalLightReader.read(reader))
        }

        globalLightingConfigurationBuilder.terrainAccent2(globalLightReader.read(reader))
        if (version <= MAX_VERSION_WITH_OBJECT_SPECIFIC_LIGHT) {
            globalLightingConfigurationBuilder.objectAccent2(globalLightReader.read(reader))
        }
        if (version >= MIN_VERSION_WITH_INFANTRY_SPECIFIC_LIGHT && version <= MAX_VERSION_WITH_INFANTRY_SPECIFIC_LIGHT) {
            globalLightingConfigurationBuilder.infantryAccent2(globalLightReader.read(reader))
        }

        return globalLightingConfigurationBuilder.build()
    }
}
