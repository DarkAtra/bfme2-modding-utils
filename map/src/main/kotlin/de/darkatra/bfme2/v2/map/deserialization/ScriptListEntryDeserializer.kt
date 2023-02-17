package de.darkatra.bfme2.v2.map.deserialization

import de.darkatra.bfme2.InvalidDataException
import de.darkatra.bfme2.readBoolean
import de.darkatra.bfme2.readUShortPrefixedString
import de.darkatra.bfme2.v2.map.Asset
import de.darkatra.bfme2.v2.map.PlayerScriptsList
import org.apache.commons.io.input.CountingInputStream
import kotlin.reflect.full.findAnnotation

internal class ScriptListEntryDeserializer(
    deserializerFactory: DeserializerFactory,
    annotationProcessingContext: AnnotationProcessingContext,
    private val deserializationContext: DeserializationContext
) : Deserializer<PlayerScriptsList.ScriptListEntry> {

    private val currentElementName = annotationProcessingContext.getCurrentElement().getName()
    private val scriptAssetName = PlayerScriptsList.Script::class.findAnnotation<Asset>()!!.name
    private val scriptFolderAssetAnnotation = PlayerScriptsList.ScriptFolder::class.findAnnotation<Asset>()!!
    private val scriptDeserializer = deserializerFactory.getDeserializer(PlayerScriptsList.Script::class)

    override fun deserialize(inputStream: CountingInputStream): PlayerScriptsList.ScriptListEntry {
        return when (val assetName = deserializationContext.peek().assetName) {
            scriptAssetName -> scriptDeserializer.deserialize(inputStream)
            scriptFolderAssetAnnotation.name -> deserializeAssetFolder(inputStream)
            else -> failWithException(assetName)
        }
    }

    private fun deserializeAssetFolder(inputStream: CountingInputStream): PlayerScriptsList.ScriptListEntry {

        val currentAsset = deserializationContext.peek()
        if (scriptFolderAssetAnnotation.version != currentAsset.assetVersion) {
            throw InvalidDataException("Unexpected assetVersion '${currentAsset.assetVersion}' for assetName '${currentAsset.assetName}' reading $currentElementName. Expected: '${scriptFolderAssetAnnotation.version}'")
        }

        val name = inputStream.readUShortPrefixedString()
        val active = inputStream.readBoolean()
        val subroutine = inputStream.readBoolean()

        val scripts = mutableListOf<PlayerScriptsList.ScriptListEntry>()
        MapFileReader.readAssets(inputStream, deserializationContext) {
            scripts.add(deserialize(inputStream))
        }

        return PlayerScriptsList.ScriptFolder(
            name = name,
            active = active,
            subroutine = subroutine,
            scripts = scripts
        )
    }

    private fun failWithException(assetName: String): Nothing {
        throw InvalidDataException("Unexpected assetName '$assetName' reading $currentElementName. Expected either '$scriptAssetName' or '${scriptFolderAssetAnnotation.name}'.")
    }
}
