package de.darkatra.bfme2

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream

internal class SkippingInputStreamTest {

    @Test
    internal fun `should correctly skip bytes`() {

        val input = ByteArrayInputStream("0123456789".toByteArray())

        val remainingBytes = SkippingInputStream(input, 4).use { it.readAllBytes() }

        assertThat(remainingBytes).isEqualTo("456789".toByteArray())
    }
}
