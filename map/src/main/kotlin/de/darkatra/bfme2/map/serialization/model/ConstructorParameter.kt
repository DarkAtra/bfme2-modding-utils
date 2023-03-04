package de.darkatra.bfme2.map.serialization.model

import kotlin.reflect.KParameter
import kotlin.reflect.KType

internal class ConstructorParameter(
    private val parameter: KParameter
) : ProcessableElement {

    init {
        check(parameter.kind === KParameter.Kind.VALUE) {
            "Can not construct ${ConstructorParameter::class.simpleName} from parameter kind: ${parameter.kind}"
        }
    }

    override fun getName(): String {
        return parameter.name!!
    }

    override fun getType(): KType {
        return parameter.type
    }
}
