package de.darkatra.bfme2.v2.map

import de.darkatra.bfme2.v2.map.deserialization.DeserializationContext
import de.darkatra.bfme2.v2.map.deserialization.Deserialize
import de.darkatra.bfme2.v2.map.deserialization.MapDeserializer
import de.darkatra.bfme2.v2.map.deserialization.MapDeserializer.DeserializationOrder
import de.darkatra.bfme2.v2.map.deserialization.SevenBitIntPrefixedStringDeserializer
import de.darkatra.bfme2.v2.map.deserialization.postprocessing.PostProcess
import de.darkatra.bfme2.v2.map.deserialization.postprocessing.PostProcessor

class AssetNameRegistry(
    // TODO: consider making it possible to place annotations on the parameter
    val assetNames:
    @PostProcess(using = AssetNamesValidatorValidator::class)
    @MapDeserializer.Properties(deserializationOrder = DeserializationOrder.VALUE_FIRST)
    Map<UInt, @Deserialize(using = SevenBitIntPrefixedStringDeserializer::class) String>
) {

    internal class AssetNamesValidatorValidator : PostProcessor<Map<UInt, String>> {

        override fun postProcess(data: Map<UInt, String>, context: DeserializationContext) {
            val totalAssetNames = data.size
            data.entries.forEachIndexed { index, (assetIndex, assetName) ->
                if ((totalAssetNames - index).toUInt() != assetIndex) {
                    throw IllegalStateException("Illegal assetIndex for '$assetName'.")
                }
            }
        }
    }
}
