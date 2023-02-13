package de.darkatra.bfme2.v2.map.deserialization.model

import kotlin.reflect.KType

internal class Generic(
    private val parent: ProcessableElement,
    private val genericIndex: Int,
    private val type: KType
) : ProcessableElement {

    override fun getName(): String {
        return "${parent.getName()}.<${genericIndex}>"
    }

    override fun getType(): KType {
        return type
    }
}
