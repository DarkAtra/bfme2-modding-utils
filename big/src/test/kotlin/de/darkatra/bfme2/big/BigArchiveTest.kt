package de.darkatra.bfme2.big

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Path
import kotlin.io.path.inputStream
import kotlin.io.path.toPath

internal class BigArchiveTest {

    private val testFile: Path = javaClass.getResource("/test/hello.txt")!!.toURI().toPath()
    private val testArchive: Path = javaClass.getResource("/test/hello.big")!!.toURI().toPath()

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
        testFile.inputStream().use { input ->
            entry.outputStream().use { output ->
                input.transferTo(output)
            }
        }
        bigArchive.writeToDisk()

        assertThatExpectedEntryForTestFileExists(bigArchive)

        // write to the entry again (should override and produce the same result)
        testFile.inputStream().use { input ->
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

        assertThat(bigArchive.entries).hasSize(1)
        assertThat(bigArchive.entries[0]).isNotNull
        assertThat(bigArchive.entries[0].name).isEqualTo("/test/hello.txt")
        assertThat(bigArchive.entries[0].archive).isEqualTo(bigArchive)
        assertThat(bigArchive.entries[0].offset).isEqualTo(48u)
        assertThat(bigArchive.entries[0].size).isEqualTo(592u)
        assertThat(bigArchive.entries[0].hasPendingChanges).isFalse
        assertThat(bigArchive.entries[0].pendingOutputStream).isNotNull

        val entryBytes = bigArchive.entries[0].inputStream().use { it.readAllBytes() }
        assertThat(entryBytes).isEqualTo(testFile.inputStream().use { it.readAllBytes() })
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
