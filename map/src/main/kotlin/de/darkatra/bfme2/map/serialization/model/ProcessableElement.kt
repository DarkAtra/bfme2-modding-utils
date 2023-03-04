package de.darkatra.bfme2.map.serialization.model

import kotlin.reflect.KType

internal interface ProcessableElement {

    fun getName(): String
    fun getType(): KType
}
