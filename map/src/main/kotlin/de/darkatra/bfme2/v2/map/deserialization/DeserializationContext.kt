package de.darkatra.bfme2.v2.map.deserialization

import de.darkatra.bfme2.v2.map.AssetNameRegistry
import de.darkatra.bfme2.v2.map.deserialization.model.ProcessableElement
import java.util.Stack
import kotlin.reflect.KClass
import kotlin.reflect.KParameter

// TODO: make internal
data class DeserializationContext(
    internal val mapFileSize: Long,
    internal val deserializerFactory: DeserializerFactory = DeserializerFactory(),
    internal val sharedData: MutableMap<String, Any> = mutableMapOf()
) {

    private val processingStack = Stack<ProcessableElement>()
    internal lateinit var assetNameRegistry: AssetNameRegistry

    private var currentParameter: KParameter? = null
    private var currentType: KClass<*>? = null

    internal fun getCurrentElement() = processingStack.peek()
    internal fun getCurrentParameter() = currentParameter!!
    internal fun getCurrentType() = currentType!!

    internal fun beginProcessing(processableElement: ProcessableElement) {
        processingStack.push(processableElement)
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

    internal fun getAssetName(assetIndex: UInt): String {
        return assetNameRegistry.assetNames[assetIndex]
            ?: throw IllegalArgumentException("Could not find assetName for assetIndex '$assetIndex'.")
    }
}
