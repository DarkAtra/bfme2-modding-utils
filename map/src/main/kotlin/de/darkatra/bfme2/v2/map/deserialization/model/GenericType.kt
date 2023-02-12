package de.darkatra.bfme2.v2.map.deserialization.model

import kotlin.reflect.KType

class GenericType(
    private val parent: ProcessableElement,
    private val genericIndex: Int,
    private val type: KType
) : ProcessableElement {

    companion object {
        private fun findRootElement(currentElement: ProcessableElement): ProcessableElement {
            return when (currentElement) {
                is GenericType -> findRootElement(currentElement.parent)
                else -> currentElement
            }
        }
    }

    override fun getName(): String {
        return "${parent.getName()}.<${genericIndex}>"
    }

    override fun getType(): KType {
        return type
    }
}
