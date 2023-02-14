package de.darkatra.bfme2.v2.map.deserialization

import de.darkatra.bfme2.readByte
import org.apache.commons.io.input.CountingInputStream
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.companionObject
import kotlin.reflect.full.companionObjectInstance
import kotlin.reflect.full.starProjectedType

internal class EnumDeserializer<T : Enum<*>>(
    private val classOfT: KClass<T>,
) : Deserializer<T> {

    private val targetType = Byte::class
    private val deserializationFunction: KFunction<T>

    init {
        deserializationFunction = classOfT.companionObject!!.members
            .filterIsInstance<KFunction<T>>()
            .filter { function -> function.name == "of${targetType.simpleName}" }
            .firstOrNull(this::hasCorrectSignature)
            ?: error("Expected ${classOfT.simpleName} to have a companion object with at least one function that starts with 'of' and has exactly one parameter of type '${targetType.simpleName}'")
    }

    override fun deserialize(inputStream: CountingInputStream): T {
        return deserializationFunction.call(classOfT.companionObjectInstance, inputStream.readByte()) // TODO: add support for different data types
    }

    private fun hasCorrectSignature(function: KFunction<T>): Boolean {
        val valueParameters = function.parameters.filter { it.kind == KParameter.Kind.VALUE }
        return valueParameters.size == 1 && valueParameters.first().type == targetType.starProjectedType
    }
}
