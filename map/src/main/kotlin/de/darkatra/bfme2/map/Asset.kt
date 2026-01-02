package de.darkatra.bfme2.map

@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
internal annotation class Asset(
    val name: String,
    val version: UShort
)
