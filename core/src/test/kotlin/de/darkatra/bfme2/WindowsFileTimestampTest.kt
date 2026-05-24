package de.darkatra.bfme2

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.Instant

class WindowsFileTimestampTest {

    @Test
    fun `windows epoch converts to zero file timestamp`() {

        val instant = Instant.parse("1601-01-01T00:00:00Z")

        val timestamp = instant.toWindowsFileTimestamp()

        assertThat(timestamp.lowDateTime).isEqualTo(0u)
        assertThat(timestamp.highDateTime).isEqualTo(0u)
    }

    @Test
    fun `zero file timestamp converts to windows epoch`() {

        val timestamp = WindowsFileTimestamp(
            lowDateTime = 0u,
            highDateTime = 0u,
        )

        assertThat(timestamp.toInstant()).isEqualTo("1601-01-01T00:00:00Z")
    }

    @Test
    fun `unix epoch converts to expected windows file timestamp`() {

        val timestamp = Instant.EPOCH.toWindowsFileTimestamp()

        assertThat(timestamp.lowDateTime).isEqualTo(0xd53e8000u)
        assertThat(timestamp.highDateTime).isEqualTo(0x019db1deu)
    }

    @Test
    fun `unix epoch windows file timestamp converts to unix epoch`() {

        val timestamp = WindowsFileTimestamp(
            lowDateTime = 0xd53e8000u,
            highDateTime = 0x019db1deu,
        )

        assertThat(timestamp.toInstant()).isEqualTo(Instant.EPOCH)
    }

    @Test
    fun `known date converts to expected windows file timestamp`() {

        val instant = Instant.parse("2024-01-01T00:00:00Z")

        val timestamp = instant.toWindowsFileTimestamp()

        assertThat(timestamp.lowDateTime).isEqualTo(0x7689c000u)
        assertThat(timestamp.highDateTime).isEqualTo(0x01da3c45u)
    }

    @Test
    fun `known windows file timestamp converts to expected instant`() {

        val timestamp = WindowsFileTimestamp(
            lowDateTime = 0x7689c000u,
            highDateTime = 0x01da3c45u,
        )

        assertThat(timestamp.toInstant()).isEqualTo("2024-01-01T00:00:00Z")
    }

    @Test
    fun `conversion preserves 100 nanosecond precision`() {

        val instant = Instant.parse("2024-01-01T00:00:00.123456700Z")

        val converted = instant.toWindowsFileTimestamp().toInstant()

        assertThat(converted).isEqualTo(instant)
    }

    @Test
    fun `conversion truncates sub 100 nanosecond precision`() {

        val instant = Instant.parse("2024-01-01T00:00:00.123456789Z")

        val converted = instant.toWindowsFileTimestamp().toInstant()

        assertThat(converted).isEqualTo("2024-01-01T00:00:00.123456700Z")
    }

    @Test
    fun `round trip preserves representative instants`() {

        val instants = listOf(
            Instant.parse("1601-01-01T00:00:00Z"),
            Instant.parse("1970-01-01T00:00:00Z"),
            Instant.parse("2000-02-29T12:34:56Z"),
            Instant.parse("2024-01-01T00:00:00.123456700Z"),
            Instant.parse("2038-01-19T03:14:07Z"),
            Instant.parse("9999-12-31T23:59:59.999999900Z"),
        )

        assertThat(instants.map { it.toWindowsFileTimestamp().toInstant() }).containsExactlyElementsOf(instants)
    }
}
