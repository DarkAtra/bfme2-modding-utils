package de.darkatra.bfme2.big

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Path
import kotlin.io.path.inputStream
import kotlin.io.path.toPath

internal class BigArchiveTest {

    private val helloFile: Path = javaClass.getResource("/test/hello.txt")!!.toURI().toPath()
    private val worldFile: Path = javaClass.getResource("/test/world.txt")!!.toURI().toPath()
    private val testArchive: Path = javaClass.getResource("/test/multiple-files.big")!!.toURI().toPath()

    @Test
    internal fun shouldWriteBigArchiveWithFilesToDisk(@TempDir tempDir: Path) {

        val tempFile = tempDir.resolve("out.big")
        val bigArchive = BigArchive(BigArchiveVersion.BIG_F, tempFile)

        val entry = bigArchive.createEntry("/test/hello.txt")

        assertThat(bigArchive.entries).hasSize(1)
        assertThat(bigArchive.entries[0]).isNotNull
        assertThat(bigArchive.entries[0].name).isEqualTo("/test/hello.txt")
        assertThat(bigArchive.entries[0].archive).isEqualTo(bigArchive)
        assertThat(bigArchive.entries[0].offset).isEqualTo(0u)
        assertThat(bigArchive.entries[0].size).isEqualTo(0u)
        assertThat(bigArchive.entries[0].hasPendingChanges).isTrue
        assertThat(bigArchive.entries[0].pendingOutputStream).isNotNull

        // write to the entry
        helloFile.inputStream().use { input ->
            entry.outputStream().use { output ->
                input.transferTo(output)
            }
        }
        bigArchive.writeToDisk()

        assertThatExpectedEntryForTestFileExists(bigArchive)

        // write to the entry again (should override and produce the same result)
        helloFile.inputStream().use { input ->
            entry.outputStream().use { output ->
                input.transferTo(output)
            }
        }
        bigArchive.writeToDisk()

        assertThatExpectedEntryForTestFileExists(bigArchive)
    }

    @Test
    internal fun shouldReadBigArchiveWithFiles() {

        val bigArchive = BigArchive.from(testArchive)

        assertThat(bigArchive.entries).hasSize(2)
        assertThat(bigArchive.entries[0]).isNotNull
        assertThat(bigArchive.entries[0].name).isEqualTo("/test/hello.txt")
        assertThat(bigArchive.entries[0].archive).isEqualTo(bigArchive)
        assertThat(bigArchive.entries[0].offset).isEqualTo(72u)
        assertThat(bigArchive.entries[0].size).isEqualTo(592u)
        assertThat(bigArchive.entries[0].hasPendingChanges).isFalse
        assertThat(bigArchive.entries[0].pendingOutputStream).isNotNull

        val firstFileBytes = bigArchive.entries[0].inputStream().use { it.readAllBytes() }
        assertThat(firstFileBytes).isEqualTo(helloFile.inputStream().use { it.readAllBytes() })

        assertThat(bigArchive.entries[1]).isNotNull
        assertThat(bigArchive.entries[1].name).isEqualTo("/test/world.txt")
        assertThat(bigArchive.entries[1].archive).isEqualTo(bigArchive)
        assertThat(bigArchive.entries[1].offset).isEqualTo(664u)
        assertThat(bigArchive.entries[1].size).isEqualTo(17u)
        assertThat(bigArchive.entries[1].hasPendingChanges).isFalse
        assertThat(bigArchive.entries[1].pendingOutputStream).isNotNull

        val secondFileBytes = bigArchive.entries[1].inputStream().use { it.readAllBytes() }
        assertThat(secondFileBytes).isEqualTo(worldFile.inputStream().use { it.readAllBytes() })
    }

    private fun assertThatExpectedEntryForTestFileExists(bigArchive: BigArchive) {
        assertThat(bigArchive.entries).hasSize(1)
        assertThat(bigArchive.entries[0]).isNotNull
        assertThat(bigArchive.entries[0].name).isEqualTo("/test/hello.txt")
        assertThat(bigArchive.entries[0].archive).isEqualTo(bigArchive)
        assertThat(bigArchive.entries[0].offset).isEqualTo(40u)
        assertThat(bigArchive.entries[0].size).isEqualTo(592u)
        assertThat(bigArchive.entries[0].hasPendingChanges).isFalse
        assertThat(bigArchive.entries[0].pendingOutputStream).isNotNull
    }
}
