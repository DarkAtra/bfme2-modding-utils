package de.darkatra.bfme2.v2.map.deserialization

import de.darkatra.bfme2.ConversionException
import org.apache.commons.io.input.CountingInputStream
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty1
import kotlin.reflect.KVisibility
import kotlin.reflect.full.memberProperties

internal class EnumDeserializer<T : Enum<*>>(
    private val classOfT: KClass<T>,
    private val deserializer: Deserializer<Any>
) : Deserializer<T> {

    private val enumValues: Array<T>
    private val enumDeserializationValueGetter: KProperty1.Getter<T, *>

    init {
        val firstConstructorParameter = classOfT.memberProperties.firstOrNull()
            ?: error("Expected '${classOfT.simpleName}' to have a primary constructor with exactly one parameter.")

        enumValues = classOfT.java.enumConstants
        enumDeserializationValueGetter = firstConstructorParameter.getter

        if (enumDeserializationValueGetter.visibility != KVisibility.INTERNAL && enumDeserializationValueGetter.visibility != KVisibility.PUBLIC) {
            error("Expected '${classOfT.simpleName}#${enumDeserializationValueGetter.name}' to be accessible.")
        }
    }

    override fun deserialize(inputStream: CountingInputStream): T {
        return getEnumValue(deserializer.deserialize(inputStream))
    }

    private fun getEnumValue(value: Any): T {
        return enumValues.firstOrNull { enumValue ->
            val deserializationEnumValue = enumDeserializationValueGetter.call(enumValue)
            if (value is String && deserializationEnumValue is String) {
                return@firstOrNull value.equals(deserializationEnumValue, true)
            }
            return@firstOrNull value == deserializationEnumValue
        } ?: throw ConversionException("Could not deserialize ${classOfT.simpleName} from '$value' (${value::class.simpleName})")
    }

    private fun hasCorrectSignature(function: KFunction<T>): Boolean {
        val valueParameters = function.parameters.filter { it.kind == KParameter.Kind.VALUE }
        return valueParameters.size == 1
    }
}
