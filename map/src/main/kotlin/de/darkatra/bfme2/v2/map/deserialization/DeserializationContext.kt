package de.darkatra.bfme2.v2.map.deserialization

import de.darkatra.bfme2.v2.map.AssetNameRegistry
import de.darkatra.bfme2.v2.map.deserialization.model.ProcessableElement
import java.util.Stack
import kotlin.reflect.KClass
import kotlin.reflect.KParameter

// TODO: separate fields that are only required for the initial discovery of deserializer from fields for the actual deserialization step
internal class DeserializationContext private constructor(
    internal val mapFileSize: Long
) {

    internal val deserializerFactory: DeserializerFactory = DeserializerFactory()

    internal val sharedData: MutableMap<String, Any> = mutableMapOf()
    private var assetNameRegistry: AssetNameRegistry? = null

    private val processingStack = Stack<ProcessableElement>()
    private var currentParameter: KParameter? = null
    private var currentType: KClass<*>? = null

    internal fun getCurrentElement() = processingStack.peek()
    internal fun getCurrentParameter() = currentParameter!!
    internal fun getCurrentType() = currentType!!

    internal fun beginProcessing(processableElement: ProcessableElement): ProcessableElement {
        return processingStack.push(processableElement)
    }

    internal fun endProcessingCurrentElement() {
        processingStack.pop()
    }

    internal fun setCurrentParameter(parameter: KParameter) {
        currentParameter = parameter
    }

    internal fun setCurrentType(type: KClass<*>) {
        currentType = type
    }

    internal fun reset() {
        if (processingStack.isNotEmpty()) {
            error("Can not reset ${DeserializationContext::class.simpleName} if processing is not completed yet.")
        }

        processingStack.clear()
        currentParameter = null
        currentType = null
    }

    internal fun setAssetNameRegistry(assetNameRegistry: AssetNameRegistry) {
        this.assetNameRegistry = assetNameRegistry
    }

    internal fun getAssetName(assetIndex: UInt): String {
        return assetNameRegistry!!.assetNames[assetIndex]
            ?: throw IllegalArgumentException("Could not find assetName for assetIndex '$assetIndex'.")
    }

    internal class Builder {
        internal fun build(mapFileSize: Long): DeserializationContext {
            return DeserializationContext(mapFileSize).also {
                it.deserializerFactory.context = it
            }
        }
    }
}
