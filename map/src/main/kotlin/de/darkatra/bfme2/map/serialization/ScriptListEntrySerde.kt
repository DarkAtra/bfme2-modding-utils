package de.darkatra.bfme2.map.serialization

import com.google.common.io.CountingInputStream
import de.darkatra.bfme2.InvalidDataException
import de.darkatra.bfme2.map.Asset
import de.darkatra.bfme2.map.scripting.Script
import de.darkatra.bfme2.map.scripting.ScriptFolder
import de.darkatra.bfme2.map.scripting.ScriptListEntry
import de.darkatra.bfme2.map.serialization.postprocessing.NoopPostProcessor
import de.darkatra.bfme2.map.serialization.postprocessing.PostProcessor
import de.darkatra.bfme2.map.serialization.preprocessing.NoopPreProcessor
import de.darkatra.bfme2.map.serialization.preprocessing.PreProcessor
import de.darkatra.bfme2.readBoolean
import de.darkatra.bfme2.readUShortPrefixedString
import de.darkatra.bfme2.writeBoolean
import de.darkatra.bfme2.writeUShortPrefixedString
import java.io.OutputStream
import kotlin.reflect.full.findAnnotation

internal class ScriptListEntrySerde(
    serdeFactory: SerdeFactory,
    annotationProcessingContext: AnnotationProcessingContext,
    private val serializationContext: SerializationContext,
    private val preProcessor: PreProcessor<ScriptListEntry>,
    private val postProcessor: PostProcessor<ScriptListEntry>
) : Serde<ScriptListEntry> {

    private val currentElementName = annotationProcessingContext.getCurrentElement().getName()
    private val scriptAssetName = Script::class.findAnnotation<Asset>()!!.name
    private val scriptFolderAssetAnnotation = ScriptFolder::class.findAnnotation<Asset>()!!
    private val assetListSerde = AssetListSerde(serializationContext, this, NoopPreProcessor(), NoopPostProcessor())
    private val scriptSerde = serdeFactory.getSerde(Script::class)

    override fun calculateByteCount(data: ScriptListEntry): Long {
        return when (data) {
            is Script -> scriptSerde.calculateByteCount(data)
            is ScriptFolder -> 2L + data.name.length + 1 + 1 + assetListSerde.calculateByteCount(data.scriptListEntries)
        }
    }

    override fun serialize(outputStream: OutputStream, data: ScriptListEntry) {
        preProcessor.preProcess(data, serializationContext).let { scriptListEntry ->
            when (scriptListEntry) {
                is Script -> scriptSerde.serialize(outputStream, scriptListEntry)
                is ScriptFolder -> serializeAssetFolder(outputStream, scriptListEntry)
            }
        }
    }

    override fun deserialize(inputStream: CountingInputStream): ScriptListEntry {
        return when (val assetName = serializationContext.peek().assetName) {
            scriptAssetName -> scriptSerde.deserialize(inputStream)
            scriptFolderAssetAnnotation.name -> deserializeAssetFolder(inputStream)
            else -> failWithException(assetName)
        }.also {
            postProcessor.postProcess(it, serializationContext)
        }
    }

    private fun serializeAssetFolder(outputStream: OutputStream, scriptFolder: ScriptFolder) {

        outputStream.writeUShortPrefixedString(scriptFolder.name)
        outputStream.writeBoolean(scriptFolder.active)
        outputStream.writeBoolean(scriptFolder.subroutine)
        assetListSerde.serialize(outputStream, scriptFolder.scriptListEntries)
    }

    private fun deserializeAssetFolder(inputStream: CountingInputStream): ScriptListEntry {

        val currentAsset = serializationContext.peek()
        if (scriptFolderAssetAnnotation.version != currentAsset.assetVersion) {
            throw InvalidDataException("Unexpected assetVersion '${currentAsset.assetVersion}' for assetName '${currentAsset.assetName}' reading $currentElementName. Expected: '${scriptFolderAssetAnnotation.version}'")
        }

        val name = inputStream.readUShortPrefixedString()
        val active = inputStream.readBoolean()
        val subroutine = inputStream.readBoolean()
        val scripts = assetListSerde.deserialize(inputStream)

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
