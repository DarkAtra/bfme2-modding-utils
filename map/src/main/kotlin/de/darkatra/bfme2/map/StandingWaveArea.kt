package de.darkatra.bfme2.map

import de.darkatra.bfme2.Vector2

data class StandingWaveArea(
	val id: UInt,
	val name: String,
	val layerName: String,
	val uvScrollSpeed: Float,
	val useAdditiveBlending: Boolean,
	val point: List<Vector2>,
	val unknown: UInt,
	val finalWidth: UInt?,
	val finalHeight: UInt?,
	val initialWidthFraction: UInt?,
	val initialHeightFraction: UInt?,
	val initialVelocity: UInt?,
	val timeToFade: UInt?,
	val timeToCompress: UInt?,
	val timeOffset2ndWave: UInt?,
	val distanceFromShore: UInt?,
	val texture: String?,
	val enablePcaWave: Boolean?,
	val waveParticleFXName: String?
) {

	class Builder {
		private var id: UInt? = null
		private var name: String? = null
		private var layerName: String? = null
		private var uvScrollSpeed: Float? = null
		private var useAdditiveBlending: Boolean? = null
		private var point: List<Vector2>? = null
		private var unknown: UInt? = null
		private var finalWidth: UInt? = null
		private var finalHeight: UInt? = null
		private var initialWidthFraction: UInt? = null
		private var initialHeightFraction: UInt? = null
		private var initialVelocity: UInt? = null
		private var timeToFade: UInt? = null
		private var timeToCompress: UInt? = null
		private var timeOffset2ndWave: UInt? = null
		private var distanceFromShore: UInt? = null
		private var texture: String? = null
		private var enablePcaWave: Boolean? = null
		private var waveParticleFXName: String? = null

		fun id(id: UInt) = apply { this.id = id }
		fun name(name: String) = apply { this.name = name }
		fun layerName(layerName: String) = apply { this.layerName = layerName }
		fun uvScrollSpeed(uvScrollSpeed: Float) = apply { this.uvScrollSpeed = uvScrollSpeed }
		fun useAdditiveBlending(useAdditiveBlending: Boolean) = apply { this.useAdditiveBlending = useAdditiveBlending }
		fun point(point: List<Vector2>) = apply { this.point = point }
		fun unknown(unknown: UInt) = apply { this.unknown = unknown }
		fun finalWidth(finalWidth: UInt) = apply { this.finalWidth = finalWidth }
		fun finalHeight(finalHeight: UInt) = apply { this.finalHeight = finalHeight }
		fun initialWidthFraction(initialWidthFraction: UInt) = apply { this.initialWidthFraction = initialWidthFraction }
		fun initialHeightFraction(initialHeightFraction: UInt) = apply { this.initialHeightFraction = initialHeightFraction }
		fun initialVelocity(initialVelocity: UInt) = apply { this.initialVelocity = initialVelocity }
		fun timeToFade(timeToFade: UInt) = apply { this.timeToFade = timeToFade }
		fun timeToCompress(timeToCompress: UInt) = apply { this.timeToCompress = timeToCompress }
		fun timeOffset2ndWave(timeOffset2ndWave: UInt) = apply { this.timeOffset2ndWave = timeOffset2ndWave }
		fun distanceFromShore(distanceFromShore: UInt) = apply { this.distanceFromShore = distanceFromShore }
		fun texture(texture: String) = apply { this.texture = texture }
		fun enablePcaWave(enablePcaWave: Boolean) = apply { this.enablePcaWave = enablePcaWave }
		fun waveParticleFXName(waveParticleFXName: String) = apply { this.waveParticleFXName = waveParticleFXName }

		fun build() = StandingWaveArea(
			id = id ?: throwIllegalStateExceptionForField("id"),
			name = name ?: throwIllegalStateExceptionForField("name"),
			layerName = layerName ?: throwIllegalStateExceptionForField("layerName"),
			uvScrollSpeed = uvScrollSpeed ?: throwIllegalStateExceptionForField("uvScrollSpeed"),
			useAdditiveBlending = useAdditiveBlending ?: throwIllegalStateExceptionForField("useAdditiveBlending"),
			point = point ?: throwIllegalStateExceptionForField("point"),
			unknown = unknown ?: throwIllegalStateExceptionForField("unknown"),
			finalWidth = finalWidth,
			finalHeight = finalHeight,
			initialWidthFraction = initialWidthFraction,
			initialHeightFraction = initialHeightFraction,
			initialVelocity = initialVelocity,
			timeToFade = timeToFade,
			timeToCompress = timeToCompress,
			timeOffset2ndWave = timeOffset2ndWave,
			distanceFromShore = distanceFromShore,
			texture = texture,
			enablePcaWave = enablePcaWave,
			waveParticleFXName = waveParticleFXName,
		)

		private fun throwIllegalStateExceptionForField(fieldName: String): Nothing {
			throw IllegalStateException("Field '$fieldName' is null.")
		}
	}
}
