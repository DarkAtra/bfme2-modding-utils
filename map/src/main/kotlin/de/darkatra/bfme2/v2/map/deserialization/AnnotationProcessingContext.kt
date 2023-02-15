package de.darkatra.bfme2.v2.map.deserialization

import de.darkatra.bfme2.v2.map.deserialization.model.ProcessableElement
import java.util.Stack

internal class AnnotationProcessingContext {

    private lateinit var deserializerFactory: DeserializerFactory
    private val processingStack = Stack<ProcessableElement>()

    internal fun setDeserializerFactory(deserializerFactory: DeserializerFactory) {
        this.deserializerFactory = deserializerFactory
    }

    internal fun getCurrentElement() = processingStack.peek()

    internal fun beginProcessing(processableElement: ProcessableElement): ProcessableElement {
        return processingStack.push(processableElement)
    }

    internal fun endProcessingCurrentElement() {
        processingStack.pop()
    }

    // TODO: use reset after all annotations were processed
    internal fun reset() {
        if (processingStack.isNotEmpty()) {
            error("Can not reset ${AnnotationProcessingContext::class.simpleName} if processing is not completed yet.")
        }
        processingStack.clear()
    }
}
