package de.darkatra.bfme2.map.camera

import de.darkatra.bfme2.map.Asset

@Asset(name = "CameraAnimationList", version = 3u)
data class CameraAnimations(
    val animations: List<CameraAnimation>
)
