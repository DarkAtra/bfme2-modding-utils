package de.darkatra.bfme2

import java.time.Duration
import java.time.Instant

private val WINDOWS_EPOCH by lazy { Instant.parse("1601-01-01T00:00:00Z") }
private val SECONDS_BETWEEN_WINDOWS_AND_UNIX_EPOCH by lazy { Duration.between(WINDOWS_EPOCH, Instant.EPOCH).seconds }
private const val TICKS_PER_SECOND = 10_000_000L

data class WindowsFileTimestamp(
    val lowDateTime: UInt,
    val highDateTime: UInt,
) {

    @PublicApi
    fun toInstant(): Instant {
        val ticks = (highDateTime.toULong() shl 32) or lowDateTime.toULong()
        val unixTicks = ticks.toLong() - SECONDS_BETWEEN_WINDOWS_AND_UNIX_EPOCH * TICKS_PER_SECOND
        val seconds = unixTicks / TICKS_PER_SECOND
        val nanos = (unixTicks % TICKS_PER_SECOND) * 100L
        return Instant.ofEpochSecond(seconds, nanos)
    }
}

@PublicApi
fun Instant.toWindowsFileTimestamp(): WindowsFileTimestamp {
    val duration = Duration.between(WINDOWS_EPOCH, this)
    val ticksSinceWindowsEpoch = duration.seconds * TICKS_PER_SECOND + duration.nano / 100
    return WindowsFileTimestamp(
        lowDateTime = (ticksSinceWindowsEpoch and 0xffffffff).toUInt(),
        highDateTime = ((ticksSinceWindowsEpoch ushr 32) and 0xffffffff).toUInt(),
    )
}
