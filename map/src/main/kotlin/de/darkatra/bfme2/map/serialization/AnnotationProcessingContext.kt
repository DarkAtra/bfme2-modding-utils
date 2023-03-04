package de.darkatra.bfme2.map.serialization

import de.darkatra.bfme2.map.serialization.model.ProcessableElement
import java.util.Stack

/**
 * Contains data relevant for resolution of deserializer arguments via [ArgumentResolver][de.darkatra.bfme2.map.serialization.argumentresolution.ArgumentResolver]s.
 * Is invalidated after the root deserializer for the MapFile is constructed.
 */
internal class AnnotationProcessingContext(
    internal val debugMode: Boolean
) {

    private var isInvalid: Boolean = false
    private val processingStack = Stack<ProcessableElement>()

    internal fun getCurrentElement(): ProcessableElement {
        ensureContextIsValid()
        return processingStack.peek()
    }

    internal fun beginProcessing(processableElement: ProcessableElement): ProcessableElement {
        ensureContextIsValid()
        return processingStack.push(processableElement)
    }

    internal fun endProcessingCurrentElement() {
        ensureContextIsValid()
        processingStack.pop()
    }

    internal fun invalidate() {
        isInvalid = true
        if (processingStack.isNotEmpty()) {
            error("Can not reset ${AnnotationProcessingContext::class.simpleName} if processing is not completed yet.")
        }
        processingStack.clear()
    }

    private fun ensureContextIsValid() {
        if (isInvalid) {
            error("${AnnotationProcessingContext::class.simpleName} is marked as invalid. This error only occurs if any ${Deserializer::class.simpleName} attempts to access data from this context which is prohibited.")
        }
    }
}
