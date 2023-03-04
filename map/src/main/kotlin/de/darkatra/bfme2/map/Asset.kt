package de.darkatra.bfme2.map

@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class Asset(
    val name: String,
    val version: UShort
)
