package de.darkatra.bfme2.v2.map.deserialization.argumentresolution

import de.darkatra.bfme2.v2.map.deserialization.Deserializer
import de.darkatra.bfme2.v2.map.deserialization.DeserializerProperties
import de.darkatra.bfme2.v2.map.deserialization.UseDeserializerProperties
import de.darkatra.bfme2.v2.map.deserialization.model.ProcessableElement
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation

internal class AnnotationParameterArgumentResolver(
    private val deserializerClass: KClass<Deserializer<*>>,
    private val deserializerParameter: KParameter
) : ArgumentResolver<Any> {

    override fun resolve(currentElement: ProcessableElement): Any {

        val deserializerPropertiesAnnotations = currentElement.getType().annotations
            .filter { isMetaAnnotatedWithDeserializerProperties(it) }

        if (deserializerPropertiesAnnotations.size > 1) {
            error("At most one ${DeserializerProperties::class.simpleName} annotation is supported per type. Found for '${currentElement.getName()}': $deserializerPropertiesAnnotations")
        }

        if (deserializerPropertiesAnnotations.isEmpty()) {
            return getDefaultValue(currentElement)
        }

        val deserializerProperties = deserializerPropertiesAnnotations.first()

        val annotationProperties = deserializerProperties.annotationClass.members
            .filterIsInstance<KProperty<*>>()
            .filter { property -> property.returnType == deserializerParameter.type }

        if (annotationProperties.size != 1) {
            error("Expected exactly one ${DeserializerProperties::class.simpleName} field to be of type '${deserializerParameter.type}'. Found for '${currentElement.getName()}': $annotationProperties")
        }

        return annotationProperties.first().getter.call(deserializerProperties)!!
    }

    private fun isMetaAnnotatedWithDeserializerProperties(annotation: Annotation, alreadyVisited: Set<Annotation> = emptySet()): Boolean {

        if (annotation.annotationClass.hasAnnotation<DeserializerProperties>()) {
            return true
        }

        return annotation.annotationClass.annotations
            .filter { !alreadyVisited.contains(it) }
            .any { isMetaAnnotatedWithDeserializerProperties(it, alreadyVisited + annotation) }
    }

    private fun getDefaultValue(currentElement: ProcessableElement): Any {

        val useDeserializerProperties = deserializerClass.findAnnotation<UseDeserializerProperties>()
            ?: error("Could not resolve default value for parameter '${deserializerParameter.name}' on class '${deserializerClass.simpleName}'.")

        if (!useDeserializerProperties.annotation.hasAnnotation<DeserializerProperties>()) {
            error("Argument of ${UseDeserializerProperties::class.simpleName} is required to be meta annotated with ${DeserializerProperties::class.simpleName}.")
        }

        // we have to use java reflection here as kotlin reflect does not expose default values for annotation
        val annotationMethods = useDeserializerProperties.annotation.java.declaredMethods
            .filter { method -> method.returnType == (deserializerParameter.type.classifier as KClass<*>).java }

        if (annotationMethods.size != 1) {
            error("Expected exactly one ${DeserializerProperties::class.simpleName} field to be of type '${deserializerParameter.type}'. Found for '${currentElement.getName()}': ${useDeserializerProperties.annotation}")
        }

        return annotationMethods.first().defaultValue
    }
}
