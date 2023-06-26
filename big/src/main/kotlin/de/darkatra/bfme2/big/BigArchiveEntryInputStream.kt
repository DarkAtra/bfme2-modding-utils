package de.darkatra.bfme2.big

import com.google.common.io.ByteStreams
import de.darkatra.bfme2.SkippingInputStream
import java.io.InputStream
import kotlin.io.path.inputStream

internal class BigArchiveEntryInputStream(
    bigArchiveEntry: BigArchiveEntry
) : InputStream() {

    private val inputStream = ByteStreams.limit(
        SkippingInputStream(
            bigArchiveEntry.archive.path.inputStream().buffered(),
            bigArchiveEntry.offset.toLong()
        ),
        bigArchiveEntry.size.toLong()
    )

    override fun read(): Int {
        return inputStream.read()
    }
}
