package de.darkatra.bfme2.big

import com.google.common.io.ByteStreams
import de.darkatra.bfme2.SkippingInputStream
import java.io.FilterInputStream
import kotlin.io.path.inputStream

internal class BigArchiveEntryInputStream(
    bigArchiveEntry: BigArchiveEntry
) : FilterInputStream(
    ByteStreams.limit(
        SkippingInputStream(
            bigArchiveEntry.archive.path.inputStream().buffered(),
            bigArchiveEntry.offset.toLong()
        ),
        bigArchiveEntry.size.toLong()
    )
)
