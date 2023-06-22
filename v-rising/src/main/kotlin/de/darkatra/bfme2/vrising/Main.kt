package de.darkatra.bfme2.vrising

import com.google.common.primitives.Bytes
import de.darkatra.bfme2.toHexString
import de.darkatra.bfme2.toLittleEndianBytes
import java.nio.file.Path
import kotlin.io.path.absolutePathString
import kotlin.io.path.outputStream
import kotlin.io.path.readBytes

fun main(args: Array<String>) {

    if (args.size != 2 && args.size != 3) {
        println("Usage: java -jar v-rising.jar <path to save file> <old steam id> <optional: new steam id>")
        return
    }

    val isReadOnly = args.size == 2
    val pathToSaveFile = args[0]
    val currentSteamIdToLookFor = args[1].toULong()

    println("Looking for $currentSteamIdToLookFor in $pathToSaveFile")

    val saveFile = Path.of(pathToSaveFile)
    val bytes = saveFile.readBytes()

    val index = Bytes.indexOf(bytes, currentSteamIdToLookFor.toLittleEndianBytes())
    val steamId = bytes.copyOfRange(index, index + 8)

    println("Found Steam ID at $index: ${steamId.toHexString()}")

    if (isReadOnly) {
        println("Not writing new save file because the you ran this tool in read-only mode.")
        return
    }

    val outFile = Path.of("new.save")
    val newSteamIdToSet = args[2].toULong()

    println("Changing Steam ID to $newSteamIdToSet and writing new save file to '${outFile.absolutePathString()}'")

    // set byte range in bytes to newSteamIdToLookFor at index
    val newBytes = bytes.copyOfRange(0, index) + newSteamIdToSet.toLittleEndianBytes() + bytes.copyOfRange(index + 8, bytes.size)

    outFile.outputStream().buffered().use { outputStream ->
        outputStream.write(newBytes)
        outputStream.flush()
    }
}
