package de.darkatra.bfme2.big

import de.darkatra.bfme2.SkippingInputStream
import org.apache.commons.io.input.BoundedInputStream
import java.io.InputStream
import kotlin.io.path.inputStream

internal class BigArchiveEntryInputStream(
    bigArchiveEntry: BigArchiveEntry
) : InputStream() {

    private val inputStream = BoundedInputStream(
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
