package de.darkatra.bfme2.map.waypoint

import de.darkatra.bfme2.map.Asset
import io.goodforgod.graalvm.hint.annotation.ReflectionHint

@ReflectionHint(ReflectionHint.AccessType.ALL_DECLARED_CONSTRUCTORS, ReflectionHint.AccessType.ALL_DECLARED_METHODS)
@Asset(name = "WaypointsList", version = 1u)
data class WaypointList(
    val waypointPaths: List<WaypointPath>
)
