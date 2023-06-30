package de.darkatra.bfme2.map.serialization

/**
 * Determines the order in which a property should be serialized. Only impacts serialization, not deserialization.
 */
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.VALUE_PARAMETER)
internal annotation class SerializationOrder(
    val ordered: Int = DEFAULT_ORDER
) {

    companion object {
        const val DEFAULT_ORDER = 0
        const val HIGHEST_PRECEDENCE = Int.MIN_VALUE
        const val LOWEST_PRECEDENCE = Int.MAX_VALUE
    }
}
