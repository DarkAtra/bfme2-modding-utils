package de.darkatra.bfme2.map

import de.darkatra.bfme2.Point3D
import de.darkatra.bfme2.Vector2
import java.awt.Color

data class PolygonTrigger(
	val name: String,
	val layerName: String?,
	val id: UInt,
	val triggerType: PolygonTriggerType,
	val riverStartControlPoint: UInt,
	val useAdditiveBlending: Boolean?,
	val uvScrollSpeed: Vector2?,
	val riverTexture: String?,
	val noiseTexture: String?,
	val alphaEdgeTexture: String?,
	val sparkleTexture: String?,
	val bumpMapTexture: String?,
	val skyTexture: String?,
	val unknown: Byte?,
	val riverColor: Color?,
	val riverAlpha: Float?,
	val points: List<Point3D>
) {

	class Builder {
		private var name: String? = null
		private var layerName: String? = null
		private var id: UInt? = null
		private var triggerType: PolygonTriggerType? = null
		private var riverStartControlPoint: UInt? = null
		private var useAdditiveBlending: Boolean? = null
		private var uvScrollSpeed: Vector2? = null
		private var riverTexture: String? = null
		private var noiseTexture: String? = null
		private var alphaEdgeTexture: String? = null
		private var sparkleTexture: String? = null
		private var bumpMapTexture: String? = null
		private var skyTexture: String? = null
		private var unknown: Byte? = null
		private var riverColor: Color? = null
		private var riverAlpha: Float? = null
		private var points: List<Point3D>? = null

		fun name(name: String) = apply { this.name = name }
		fun layerName(layerName: String?) = apply { this.layerName = layerName }
		fun id(id: UInt) = apply { this.id = id }
		fun triggerType(triggerType: PolygonTriggerType) = apply { this.triggerType = triggerType }
		fun riverStartControlPoint(riverStartControlPoint: UInt) = apply { this.riverStartControlPoint = riverStartControlPoint }
		fun useAdditiveBlending(useAdditiveBlending: Boolean?) = apply { this.useAdditiveBlending = useAdditiveBlending }
		fun uvScrollSpeed(uvScrollSpeed: Vector2?) = apply { this.uvScrollSpeed = uvScrollSpeed }
		fun riverTexture(riverTexture: String?) = apply { this.riverTexture = riverTexture }
		fun noiseTexture(noiseTexture: String?) = apply { this.noiseTexture = noiseTexture }
		fun alphaEdgeTexture(alphaEdgeTexture: String?) = apply { this.alphaEdgeTexture = alphaEdgeTexture }
		fun sparkleTexture(sparkleTexture: String?) = apply { this.sparkleTexture = sparkleTexture }
		fun bumpMapTexture(bumpMapTexture: String?) = apply { this.bumpMapTexture = bumpMapTexture }
		fun skyTexture(skyTexture: String?) = apply { this.skyTexture = skyTexture }
		fun unknown(unknown: Byte) = apply { this.unknown = unknown }
		fun riverColor(riverColor: Color) = apply { this.riverColor = riverColor }
		fun riverAlpha(riverAlpha: Float) = apply { this.riverAlpha = riverAlpha }
		fun points(points: List<Point3D>) = apply { this.points = points }

		fun build() = PolygonTrigger(
			name = name ?: throwIllegalStateExceptionForField("name"),
			layerName = layerName,
			id = id ?: throwIllegalStateExceptionForField("id"),
			triggerType = triggerType ?: throwIllegalStateExceptionForField("triggerType"),
			riverStartControlPoint = riverStartControlPoint ?: throwIllegalStateExceptionForField("riverStartControlPoint"),
			useAdditiveBlending = useAdditiveBlending,
			uvScrollSpeed = uvScrollSpeed,
			riverTexture = riverTexture,
			noiseTexture = noiseTexture,
			alphaEdgeTexture = alphaEdgeTexture,
			sparkleTexture = sparkleTexture,
			bumpMapTexture = bumpMapTexture,
			skyTexture = skyTexture,
			unknown = unknown,
			riverColor = riverColor,
			riverAlpha = riverAlpha,
			points = points ?: throwIllegalStateExceptionForField("points")
		)

		private fun throwIllegalStateExceptionForField(fieldName: String): Nothing {
			throw IllegalStateException("Field '$fieldName' is null.")
		}
	}
}
