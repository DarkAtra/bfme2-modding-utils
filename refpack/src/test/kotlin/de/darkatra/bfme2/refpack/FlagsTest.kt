package de.darkatra.bfme2.refpack

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class FlagsTest {

    @Test
    fun `should have no flags set`() {

        val byte = 0b00000000

        assertThat(Flags.DEFAULT.isPresent(byte.toUByte())).isFalse()
        assertThat(Flags.STORES_COMPRESSED_SIZE.isPresent(byte.toUByte())).isFalse()
        assertThat(Flags.UNKNOWN.isPresent(byte.toUByte())).isFalse()
        assertThat(Flags.USE_32BIT_SIZE_HEADER.isPresent(byte.toUByte())).isFalse()
    }

    @Test
    fun `should have default flag set`() {

        val byte = 0b00010000

        assertThat(Flags.DEFAULT.isPresent(byte.toUByte())).isTrue()
    }

    @Test
    fun `should have compressed size flag set`() {

        val byte = 0b00000001

        assertThat(Flags.STORES_COMPRESSED_SIZE.isPresent(byte.toUByte())).isTrue()
    }

    @Test
    fun `should have unknown spore flag set`() {

        val byte = 0b01000000

        assertThat(Flags.UNKNOWN.isPresent(byte.toUByte())).isTrue()
    }

    @Test
    fun `should have large file flag set`() {

        val byte = 0b10000000

        assertThat(Flags.USE_32BIT_SIZE_HEADER.isPresent(byte.toUByte())).isTrue()
    }

    @Test
    fun `should have all flags set`() {

        val byte = 0b11010001

        assertThat(Flags.DEFAULT.isPresent(byte.toUByte())).isTrue()
        assertThat(Flags.STORES_COMPRESSED_SIZE.isPresent(byte.toUByte())).isTrue()
        assertThat(Flags.UNKNOWN.isPresent(byte.toUByte())).isTrue()
        assertThat(Flags.USE_32BIT_SIZE_HEADER.isPresent(byte.toUByte())).isTrue()
    }
}
