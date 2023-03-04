package de.darkatra.bfme2.map.camera

import de.darkatra.bfme2.map.Asset

@Asset(name = "NamedCameras", version = 2u)
data class Cameras(
    val cameras: List<Camera>
)
