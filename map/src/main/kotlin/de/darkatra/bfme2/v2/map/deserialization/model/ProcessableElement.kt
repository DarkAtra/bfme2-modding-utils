package de.darkatra.bfme2.v2.map.deserialization.model

import kotlin.reflect.KType

internal interface ProcessableElement {

    fun getName(): String
    fun getType(): KType
}
