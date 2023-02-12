package de.darkatra.bfme2.v2.map.deserialization.model

import kotlin.reflect.KType

interface ProcessableElement {

    fun getName(): String
    fun getType(): KType
}
