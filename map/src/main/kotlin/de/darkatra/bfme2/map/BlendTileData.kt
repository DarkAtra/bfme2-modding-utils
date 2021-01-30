package de.darkatra.bfme2.map

data class BlendTileData(
	val tiles: Map<UInt, Map<UInt, UShort>>,
	val blends: Map<UInt, Map<UInt, UInt>>,
	val threeWayBlends: Map<UInt, Map<UInt, UInt>>,
	val cliffTextures: Map<UInt, Map<UInt, UInt>>,
	val impassability: Map<UInt, Map<UInt, Boolean>>?,
	val impassabilityToPlayers: Map<UInt, Map<UInt, Boolean>>?,
	val passageWidths: Map<UInt, Map<UInt, Boolean>>?,
	val taintability: Map<UInt, Map<UInt, Boolean>>?,
	val extraPassability: Map<UInt, Map<UInt, Boolean>>?,
	val flammability: Map<UInt, Map<UInt, TileFlammability>>?,
	val visibility: Map<UInt, Map<UInt, Boolean>>?,
	val buildability: Map<UInt, Map<UInt, Boolean>>?,
	val impassabilityToAirUnits: Map<UInt, Map<UInt, Boolean>>?,
	val tiberiumGrowability: Map<UInt, Map<UInt, Boolean>>?,
	val dynamicShrubberyDensity: Map<UInt, Map<UInt, Byte>>?,
	val textureCellCount: UInt,
	val textures: List<BlendTileTexture>,
	val magicValue1: UInt,
	val magicValue2: UInt,
	val blendDescriptions: List<BlendDescription>,
	val cliffTextureMappings: List<CliffTextureMapping>
) {

	class Builder {
		private var tiles: Map<UInt, Map<UInt, UShort>>? = null
		private var blends: Map<UInt, Map<UInt, UInt>>? = null
		private var threeWayBlends: Map<UInt, Map<UInt, UInt>>? = null
		private var cliffTextures: Map<UInt, Map<UInt, UInt>>? = null
		private var impassability: Map<UInt, Map<UInt, Boolean>>? = null
		private var impassabilityToPlayers: Map<UInt, Map<UInt, Boolean>>? = null
		private var passageWidths: Map<UInt, Map<UInt, Boolean>>? = null
		private var taintability: Map<UInt, Map<UInt, Boolean>>? = null
		private var extraPassability: Map<UInt, Map<UInt, Boolean>>? = null
		private var flammability: Map<UInt, Map<UInt, TileFlammability>>? = null
		private var visibility: Map<UInt, Map<UInt, Boolean>>? = null
		private var buildability: Map<UInt, Map<UInt, Boolean>>? = null
		private var impassabilityToAirUnits: Map<UInt, Map<UInt, Boolean>>? = null
		private var tiberiumGrowability: Map<UInt, Map<UInt, Boolean>>? = null
		private var dynamicShrubberyDensity: Map<UInt, Map<UInt, Byte>>? = null
		private var textureCellCount: UInt? = null
		private var textures: List<BlendTileTexture>? = null
		private var magicValue1: UInt? = null
		private var magicValue2: UInt? = null
		private var blendDescriptions: List<BlendDescription>? = null
		private var cliffTextureMappings: List<CliffTextureMapping>? = null

		fun tiles(tiles: Map<UInt, Map<UInt, UShort>>?) = apply { this.tiles = tiles }
		fun blends(blends: Map<UInt, Map<UInt, UInt>>?) = apply { this.blends = blends }
		fun threeWayBlends(threeWayBlends: Map<UInt, Map<UInt, UInt>>?) = apply { this.threeWayBlends = threeWayBlends }
		fun cliffTextures(cliffTextures: Map<UInt, Map<UInt, UInt>>?) = apply { this.cliffTextures = cliffTextures }
		fun impassability(impassability: Map<UInt, Map<UInt, Boolean>>?) = apply { this.impassability = impassability }
		fun impassabilityToPlayers(impassabilityToPlayers: Map<UInt, Map<UInt, Boolean>>?) = apply { this.impassabilityToPlayers = impassabilityToPlayers }
		fun passageWidths(passageWidths: Map<UInt, Map<UInt, Boolean>>?) = apply { this.passageWidths = passageWidths }
		fun taintability(taintability: Map<UInt, Map<UInt, Boolean>>?) = apply { this.taintability = taintability }
		fun extraPassability(extraPassability: Map<UInt, Map<UInt, Boolean>>?) = apply { this.extraPassability = extraPassability }
		fun flammability(flammability: Map<UInt, Map<UInt, TileFlammability>>?) = apply { this.flammability = flammability }
		fun visibility(visibility: Map<UInt, Map<UInt, Boolean>>?) = apply { this.visibility = visibility }
		fun buildability(buildability: Map<UInt, Map<UInt, Boolean>>?) = apply { this.buildability = buildability }
		fun impassabilityToAirUnits(impassabilityToAirUnits: Map<UInt, Map<UInt, Boolean>>?) = apply { this.impassabilityToAirUnits = impassabilityToAirUnits }
		fun tiberiumGrowability(tiberiumGrowability: Map<UInt, Map<UInt, Boolean>>?) = apply { this.tiberiumGrowability = tiberiumGrowability }
		fun dynamicShrubberyDensity(dynamicShrubberyDensity: Map<UInt, Map<UInt, Byte>>?) = apply { this.dynamicShrubberyDensity = dynamicShrubberyDensity }
		fun textureCellCount(textureCellCount: UInt?) = apply { this.textureCellCount = textureCellCount }
		fun textures(textures: List<BlendTileTexture>?) = apply { this.textures = textures }
		fun magicValue1(magicValue1: UInt?) = apply { this.magicValue1 = magicValue1 }
		fun magicValue2(magicValue2: UInt?) = apply { this.magicValue2 = magicValue2 }
		fun blendDescriptions(blendDescriptions: List<BlendDescription>?) = apply { this.blendDescriptions = blendDescriptions }
		fun cliffTextureMappings(cliffTextureMappings: List<CliffTextureMapping>?) = apply { this.cliffTextureMappings = cliffTextureMappings }

		fun build() = BlendTileData(
			tiles = tiles ?: throwIllegalStateExceptionForField("tiles"),
			blends = blends ?: throwIllegalStateExceptionForField("blends"),
			threeWayBlends = threeWayBlends ?: throwIllegalStateExceptionForField("threeWayBlends"),
			cliffTextures = cliffTextures ?: throwIllegalStateExceptionForField("cliffTextures"),
			impassability = impassability,
			impassabilityToPlayers = impassabilityToPlayers,
			passageWidths = passageWidths,
			taintability = taintability,
			extraPassability = extraPassability,
			flammability = flammability,
			visibility = visibility,
			buildability = buildability,
			impassabilityToAirUnits = impassabilityToAirUnits,
			tiberiumGrowability = tiberiumGrowability,
			dynamicShrubberyDensity = dynamicShrubberyDensity,
			textureCellCount = textureCellCount ?: throwIllegalStateExceptionForField("textureCellCount"),
			textures = textures ?: throwIllegalStateExceptionForField("textures"),
			magicValue1 = magicValue1 ?: throwIllegalStateExceptionForField("magicValue1"),
			magicValue2 = magicValue2 ?: throwIllegalStateExceptionForField("magicValue2"),
			blendDescriptions = blendDescriptions ?: throwIllegalStateExceptionForField("blendDescriptions"),
			cliffTextureMappings = cliffTextureMappings ?: throwIllegalStateExceptionForField("cliffTextureMappings")
		)

		private fun throwIllegalStateExceptionForField(fieldName: String): Nothing {
			throw IllegalStateException("Field '$fieldName' is null.")
		}
	}
}
