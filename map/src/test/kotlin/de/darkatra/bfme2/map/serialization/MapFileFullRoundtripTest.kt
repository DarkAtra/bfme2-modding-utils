package de.darkatra.bfme2.map.serialization

import de.darkatra.bfme2.map.MapFileCompression
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Named
import org.junit.jupiter.api.condition.EnabledIfSystemProperty
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.nio.file.Path
import java.util.stream.Stream
import kotlin.io.path.absolutePathString
import kotlin.io.path.exists

@EnabledIfSystemProperty(named = "fullVerification", matches = "true")
class MapFileFullRoundtripTest {

    @ParameterizedTest
    @MethodSource("originalGameMaps")
    fun `should roundtrip map`(mapInputStream: InputStream) {

        val parsedMapFile = mapInputStream.use(MapFileReader()::read)

        val writtenBytes = ByteArrayOutputStream().use {
            MapFileWriter().write(it, parsedMapFile, MapFileCompression.UNCOMPRESSED)
            it.toByteArray()
        }

        val writtenMapFile = writtenBytes.inputStream().use(MapFileReader()::read)

        assertThat(writtenMapFile).isEqualTo(parsedMapFile)
    }

    companion object {

        private const val EXPANSION_REGISTRY_KEY = "SOFTWARE\\Wow6432Node\\Electronic Arts\\Electronic Arts\\The Lord of the Rings, The Rise of the Witch-king"

        @JvmStatic
        fun originalGameMaps(): Stream<Arguments> {

            // hacky: lazily loads classes for JNA and BigArchive to avoid errors when compiling without the "fullVerification" profile
            val advapi32UtilClass = Class.forName("com.sun.jna.platform.win32.Advapi32Util")
            val winRegClass = Class.forName("com.sun.jna.platform.win32.WinReg")

            val registryGetStringValueMethod = advapi32UtilClass.methods.first { it.name == "registryGetStringValue" && it.parameterCount == 3 }
            val hkeyLocalMachineValue = winRegClass.getField("HKEY_LOCAL_MACHINE").get(null)

            val rotwkHomeDir = Path.of(
                registryGetStringValueMethod.invoke(null, hkeyLocalMachineValue, EXPANSION_REGISTRY_KEY, "InstallPath") as String
            ).normalize()

            if (!rotwkHomeDir.exists()) {
                throw IllegalStateException("Profile 'fullVerification' is enabled but the directory '${rotwkHomeDir.absolutePathString()}' does not exist.")
            }

            val mapsBig = rotwkHomeDir.resolve("Maps.big")
            if (!mapsBig.exists()) {
                throw IllegalStateException("Profile 'fullVerification' is enabled but 'Maps.big' does not exist in directory '${rotwkHomeDir.absolutePathString()}'.")
            }

            val bigArchiveClass = Class.forName("de.darkatra.bfme2.big.BigArchive")
            val bigArchiveCompanion = bigArchiveClass.getDeclaredField("Companion").get(null)
            val bigArchiveCompanionClass = Class.forName($$"de.darkatra.bfme2.big.BigArchive$Companion")
            val fromMethod = bigArchiveCompanionClass.methods.first { it.name == "from" && it.parameterCount == 1 }
            val getEntriesMethod = bigArchiveClass.methods.first { it.name == "getEntries" }

            val bigArchiveEntryClass = Class.forName("de.darkatra.bfme2.big.BigArchiveEntry")
            val getNameMethod = bigArchiveEntryClass.methods.first { it.name == "getName" }
            val inputStreamMethod = bigArchiveEntryClass.methods.first { it.name == "inputStream" }

            val maps = fromMethod.invoke(bigArchiveCompanion, mapsBig)
            val entries = getEntriesMethod.invoke(maps) as List<*>

            return entries
                .filter { map -> (getNameMethod.invoke(map) as String).endsWith(".map") }
                .map { map ->
                    Arguments.of(
                        Named.of(
                            getNameMethod.invoke(map) as String,
                            inputStreamMethod.invoke(map) as InputStream
                        )
                    )
                }
                .stream()
        }
    }
}
