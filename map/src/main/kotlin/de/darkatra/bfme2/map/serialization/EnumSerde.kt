package de.darkatra.bfme2.map.serialization

import com.google.common.io.CountingInputStream
import de.darkatra.bfme2.ConversionException
import de.darkatra.bfme2.map.serialization.postprocessing.PostProcessor
import de.darkatra.bfme2.map.serialization.preprocessing.PreProcessor
import java.io.OutputStream
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.KVisibility
import kotlin.reflect.full.memberProperties

internal class EnumSerde<T : Enum<*>>(
    private val classOfT: KClass<T>,
    private val context: SerializationContext,
    private val serde: Serde<Any>,
    private val preProcessor: PreProcessor<T>,
    private val postProcessor: PostProcessor<T>
) : Serde<T> {

    private val enumValues: Array<T>
    private val enumValueGetter: KProperty1.Getter<T, *>

    init {
        val firstConstructorParameter = classOfT.memberProperties.firstOrNull()
            ?: error("Expected '${classOfT.simpleName}' to have a primary constructor with exactly one parameter.")

        enumValues = classOfT.java.enumConstants
        enumValueGetter = firstConstructorParameter.getter

        if (enumValueGetter.visibility != KVisibility.INTERNAL && enumValueGetter.visibility != KVisibility.PUBLIC) {
            error("Expected '${classOfT.simpleName}#${enumValueGetter.name}' to be accessible.")
        }
    }

    override fun calculateByteCount(data: T): Long {
        return serde.calculateByteCount(enumValueGetter.call(data)!!)
    }

    override fun serialize(outputStream: OutputStream, data: T) {
        serde.serialize(
            outputStream,
            enumValueGetter.call(preProcessor.preProcess(data, context))!!
        )
    }

    override fun deserialize(inputStream: CountingInputStream): T {
        return getEnumValue(serde.deserialize(inputStream)).also {
            postProcessor.postProcess(it, context)
        }
    }

    private fun getEnumValue(value: Any): T {
        return enumValues.firstOrNull { enumValue ->
            val deserializationEnumValue = enumValueGetter.call(enumValue)
            if (value is String && deserializationEnumValue is String) {
                return@firstOrNull value.equals(deserializationEnumValue, true)
            }
            return@firstOrNull value == deserializationEnumValue
        } ?: throw ConversionException("Could not deserialize ${classOfT.simpleName} from '$value' (${value::class.simpleName})")
    }
}
