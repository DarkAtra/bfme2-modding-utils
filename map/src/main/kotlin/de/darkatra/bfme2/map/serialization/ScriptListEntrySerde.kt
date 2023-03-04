package de.darkatra.bfme2.map.serialization

import de.darkatra.bfme2.InvalidDataException
import de.darkatra.bfme2.map.Asset
import de.darkatra.bfme2.map.scripting.Script
import de.darkatra.bfme2.map.scripting.ScriptFolder
import de.darkatra.bfme2.map.scripting.ScriptListEntry
import de.darkatra.bfme2.readBoolean
import de.darkatra.bfme2.readUShortPrefixedString
import org.apache.commons.io.input.CountingInputStream
import java.io.OutputStream
import kotlin.reflect.full.findAnnotation

internal class ScriptListEntrySerde(
    serdeFactory: SerdeFactory,
    annotationProcessingContext: AnnotationProcessingContext,
    private val serializationContext: SerializationContext
) : Serde<ScriptListEntry> {

    private val currentElementName = annotationProcessingContext.getCurrentElement().getName()
    private val scriptAssetName = Script::class.findAnnotation<Asset>()!!.name
    private val scriptFolderAssetAnnotation = ScriptFolder::class.findAnnotation<Asset>()!!
    private val scriptSerde = serdeFactory.getSerde(Script::class)

    override fun serialize(outputStream: OutputStream, data: ScriptListEntry) {
        TODO("Not yet implemented")
    }

    override fun deserialize(inputStream: CountingInputStream): ScriptListEntry {
        return when (val assetName = serializationContext.peek().assetName) {
            scriptAssetName -> scriptSerde.deserialize(inputStream)
            scriptFolderAssetAnnotation.name -> deserializeAssetFolder(inputStream)
            else -> failWithException(assetName)
        }
    }

    private fun deserializeAssetFolder(inputStream: CountingInputStream): ScriptListEntry {

        val currentAsset = serializationContext.peek()
        if (scriptFolderAssetAnnotation.version != currentAsset.assetVersion) {
            throw InvalidDataException("Unexpected assetVersion '${currentAsset.assetVersion}' for assetName '${currentAsset.assetName}' reading $currentElementName. Expected: '${scriptFolderAssetAnnotation.version}'")
        }

        val name = inputStream.readUShortPrefixedString()
        val active = inputStream.readBoolean()
        val subroutine = inputStream.readBoolean()

        val scripts = mutableListOf<ScriptListEntry>()
        MapFileReader.readAssets(inputStream, serializationContext) {
            scripts.add(deserialize(inputStream))
        }

        return ScriptFolder(
            name = name,
            active = active,
            subroutine = subroutine,
            scriptListEntries = scripts
        )
    }

    private fun failWithException(assetName: String): Nothing {
        throw InvalidDataException("Unexpected assetName '$assetName' reading $currentElementName. Expected either '$scriptAssetName' or '${scriptFolderAssetAnnotation.name}'.")
    }
}
