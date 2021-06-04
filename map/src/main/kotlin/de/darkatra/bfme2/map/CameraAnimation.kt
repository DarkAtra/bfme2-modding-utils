package de.darkatra.bfme2.map

data class CameraAnimation(
	val animationType: CameraAnimationType,
	val name: String,
	val numberOfFrames: UInt,
	val startOffset: UInt,
	val cameraFrames: List<CameraAnimationFrame>,
	val lookAtFrames: List<CameraAnimationFrame>?
)
