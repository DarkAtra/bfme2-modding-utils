package de.darkatra.bfme2.map.waypoint

import de.darkatra.bfme2.map.Asset

@Asset(name = "WaypointsList", version = 1u)
data class WaypointList(
    val waypointPaths: List<WaypointPath>
)
