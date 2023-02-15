package de.darkatra.bfme2.v2.map.deserialization

import de.darkatra.bfme2.InvalidDataException
import de.darkatra.bfme2.v2.map.deserialization.postprocessing.PostProcessor
import org.apache.commons.io.input.CountingInputStream

@UseDeserializerProperties(AssetListDeserializer.Properties::class)
internal class AssetListDeserializer<T>(
    annotationProcessingContext: AnnotationProcessingContext,
    private val deserializationContext: DeserializationContext,
    private val entryDeserializer: Deserializer<T>,
    private val postProcessor: PostProcessor<List<T>>,

    private val expectedAssetName: String
) : Deserializer<List<T>> {

    private val currentElementName = annotationProcessingContext.getCurrentElement().getName()

    @MustBeDocumented
    @Retention(AnnotationRetention.RUNTIME)
    @Target(AnnotationTarget.TYPE)
    @DeserializerProperties
    @Suppress("unused") // properties are used via AnnotationParameterArgumentResolver
    internal annotation class Properties(
        val expectedAssetName: String = ""
    )

    override fun deserialize(inputStream: CountingInputStream): List<T> {

        val list = mutableListOf<T>()

        MapFileReader.readAssets(inputStream, deserializationContext) { assetName ->

            if (assetName != expectedAssetName) {
                throw InvalidDataException("Unexpected assetName '$assetName' reading $currentElementName.")
            }

            list.add(entryDeserializer.deserialize(inputStream))
        }

        return list.also {
            postProcessor.postProcess(list, deserializationContext)
        }
    }
}
