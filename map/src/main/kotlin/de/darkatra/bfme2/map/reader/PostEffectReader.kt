package de.darkatra.bfme2.map.reader

import de.darkatra.bfme2.InvalidDataException
import de.darkatra.bfme2.Vector4
import de.darkatra.bfme2.map.MapFile
import de.darkatra.bfme2.map.PostEffect
import de.darkatra.bfme2.map.PostEffectParameter
import de.darkatra.bfme2.readByte
import de.darkatra.bfme2.readFloat
import de.darkatra.bfme2.readUInt
import de.darkatra.bfme2.readUShortPrefixedString
import org.apache.commons.io.input.CountingInputStream

class PostEffectReader : AssetReader {

	companion object {
		const val ASSET_NAME = "PostEffectsChunk"
	}

	override fun read(reader: CountingInputStream, context: MapFileParseContext, builder: MapFile.Builder) {

		MapFileReader.readAsset(reader, context, PolygonTriggersReader.ASSET_NAME) { version ->

			val numberOfPostEffects = when {
				version >= 2u -> reader.readUInt()
				else -> reader.readByte().toUInt()
			}

			val postEffects = mutableListOf<PostEffect>()
			for (i in 0u until numberOfPostEffects step 1) {
				postEffects.add(
					readPostEffect(reader, version)
				)
			}

			builder.postEffects(postEffects)
		}
	}

	/**
	 * v1 (BFME II) was hardcoded to store LookupTable's parameters.
	 * v2 added a more flexible structure, generically storing the effect name and parameters.
	 */
	private fun readPostEffect(reader: CountingInputStream, version: UShort): PostEffect {

		val name = reader.readUShortPrefixedString()

		return if (version >= 2u) {

			val numberOfParameters = reader.readUInt()

			val parameters = mutableListOf<PostEffectParameter>()
			for (i in 0u until numberOfParameters step 1) {
				parameters.add(
					parsePostEffectParameter(reader)
				)
			}

			PostEffect(
				name = name,
				blendFactor = null,
				lookupImage = null,
				parameters = listOf()
			)
		} else {
			PostEffect(
				name = name,
				blendFactor = reader.readFloat(),
				lookupImage = reader.readUShortPrefixedString(),
				parameters = null
			)
		}
	}

	private fun parsePostEffectParameter(reader: CountingInputStream): PostEffectParameter {

		val name = reader.readUShortPrefixedString()
		val type = reader.readUShortPrefixedString()

		val value: Any = when (type) {
			"Float" -> reader.readFloat()
			"Float4" -> Vector4(
				x = reader.readFloat(),
				y = reader.readFloat(),
				z = reader.readFloat(),
				w = reader.readFloat()
			)
			"Int" -> reader.readUInt()
			"Texture" -> reader.readUShortPrefixedString()
			else -> throw InvalidDataException("Unknown type '$type' for PostEffectParameter with name '$name'.")
		}

		return PostEffectParameter(
			name = name,
			type = type,
			value = value
		)
	}
}
