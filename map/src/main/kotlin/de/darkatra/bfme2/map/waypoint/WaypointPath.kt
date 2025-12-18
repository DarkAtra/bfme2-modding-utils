package de.darkatra.bfme2.map.waypoint

import io.goodforgod.graalvm.hint.annotation.ReflectionHint

@ReflectionHint(ReflectionHint.AccessType.ALL_DECLARED_CONSTRUCTORS, ReflectionHint.AccessType.ALL_DECLARED_METHODS)
data class WaypointPath(
    val startWaypointID: UInt,
    val endWaypointID: UInt
)
