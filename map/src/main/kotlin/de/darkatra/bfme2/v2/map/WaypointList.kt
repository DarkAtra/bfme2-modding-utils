package de.darkatra.bfme2.v2.map

@Asset(name = "WaypointsList", version = 1u)
data class WaypointList(
    val waypointPaths: List<WaypointPath>
) {
    data class WaypointPath(
        val startWaypointID: UInt,
        val endWaypointID: UInt
    )
}
