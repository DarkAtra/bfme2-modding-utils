package de.darkatra.bfme2.map.serialization.argumentresolution

import de.darkatra.bfme2.map.serialization.Serde
import de.darkatra.bfme2.map.serialization.SerdeProperties
import de.darkatra.bfme2.map.serialization.UseSerdeProperties
import de.darkatra.bfme2.map.serialization.model.ProcessableElement
import de.darkatra.bfme2.map.toKClass
import io.goodforgod.graalvm.hint.annotation.ReflectionHint
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty
import kotlin.reflect.KType
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.isSupertypeOf
import kotlin.reflect.jvm.javaGetter
import kotlin.reflect.typeOf

@ReflectionHint(ReflectionHint.AccessType.ALL_DECLARED_CONSTRUCTORS, ReflectionHint.AccessType.ALL_DECLARED_METHODS)
internal class SerdePropertiesArgumentResolver(
    private val serdeClass: KClass<Serde<*>>,
    private val serdeParameter: KParameter
) : ArgumentResolver<Any> {

    override fun resolve(currentElement: ProcessableElement): Any {

        val serdePropertiesAnnotations: MutableList<Annotation> = currentElement.getType().annotations
            .filter { isMetaAnnotatedWithSerdeProperties(it) }
            .toMutableList()

        // fallback to class based annotations if type was not explicitly annotated
        if (serdePropertiesAnnotations.isEmpty()) {
            serdePropertiesAnnotations += currentElement.getType().toKClass().annotations
                .filter { isMetaAnnotatedWithSerdeProperties(it) }
        }

        if (serdePropertiesAnnotations.size > 1) {
            error("At most one ${SerdeProperties::class.simpleName} annotation is supported per type. Found for '${currentElement.getName()}': $serdePropertiesAnnotations")
        }

        if (serdePropertiesAnnotations.isEmpty()) {
            return getDefaultValue(currentElement)
        }

        val serdeProperties = serdePropertiesAnnotations.first()

        val annotationProperties = serdeProperties.annotationClass.members
            .filterIsInstance<KProperty<*>>()
            .filter { property -> isCompatible(serdeParameter.type, property.returnType) }

        if (annotationProperties.size != 1) {
            error("Expected exactly one ${SerdeProperties::class.simpleName} field to be of type '${serdeParameter.type}'. Found for '${currentElement.getName()}': $annotationProperties")
        }

        return try {
            convertIfNecessary(annotationProperties.first().getter.call(serdeProperties)!!)
        } catch (e: Exception) {
            getDefaultValue(currentElement)
        }
    }

    private fun isMetaAnnotatedWithSerdeProperties(annotation: Annotation, alreadyVisited: Set<Annotation> = emptySet()): Boolean {

        if (annotation.annotationClass.hasAnnotation<SerdeProperties>()) {
            return true
        }

        return annotation.annotationClass.annotations
            .filter { !alreadyVisited.contains(it) }
            .any { isMetaAnnotatedWithSerdeProperties(it, alreadyVisited + annotation) }
    }

    private fun getDefaultValue(currentElement: ProcessableElement): Any {

        val useSerdeProperties = serdeClass.findAnnotation<UseSerdeProperties>()
            ?: error("Could not find ${UseSerdeProperties::class.simpleName} on class '${serdeClass.simpleName}'.")

        if (!useSerdeProperties.propertiesClass.hasAnnotation<SerdeProperties>()) {
            error("Argument of ${UseSerdeProperties::class.simpleName} is required to be meta annotated with ${SerdeProperties::class.simpleName}.")
        }

        val annotationMethods = useSerdeProperties.propertiesClass.members
            .filterIsInstance<KProperty<*>>()
            .filter { annotationProperty ->
                annotationProperty.returnType == serdeParameter.type
                    || innerGenericIsCompatible(annotationProperty.returnType, serdeParameter.type)
            }

        if (annotationMethods.size != 1) {
            error("Expected exactly one ${SerdeProperties::class.simpleName} field to be of type '${serdeParameter.type}'. Found for '${currentElement.getName()}': ${annotationMethods.map { it.name }}")
        }

        // we have to use java reflection here as kotlin reflect does not expose default values for annotation
        val defaultValue = annotationMethods.first().javaGetter!!.defaultValue
            ?: error("Could not resolve default value for parameter '${serdeParameter.name}' on class '${serdeClass.simpleName}'.")

        return convertIfNecessary(
            when (serdeParameter.type) {
                // default value specific conversion
                typeOf<UInt>() -> (defaultValue as Int).toUInt()
                else -> defaultValue
            }
        )
    }

    private fun innerGenericIsCompatible(expectedType: KType, actualType: KType): Boolean {

        if (expectedType.arguments.isEmpty() || actualType.arguments.isEmpty()) {
            return false
        }

        return expectedType.arguments.zip(actualType.arguments)
            .all { (expectedType, serdeType) ->
                expectedType.type == null
                    || expectedType.type!!.isSupertypeOf(serdeType.type!!)
                    || innerGenericIsCompatible(expectedType.type!!, serdeType.type!!)
            }
    }

    private fun isCompatible(annotationType: KType, serdeParameterType: KType): Boolean {
        if (annotationType != serdeParameterType) {
            return innerGenericIsCompatible(typeOf<Array<*>>(), annotationType) && innerGenericIsCompatible(typeOf<List<*>>(), serdeParameterType)
        }
        return true
    }

    private fun convertIfNecessary(value: Any): Any {
        return when (value) {
            is Array<*> -> value.toList().map { convertIfNecessary(it!!) }
            is Class<*> -> value.kotlin
            else -> value
        }
    }
}
