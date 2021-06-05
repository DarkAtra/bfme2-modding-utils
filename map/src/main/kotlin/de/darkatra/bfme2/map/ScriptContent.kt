package de.darkatra.bfme2.map

abstract class ScriptContent<T>(
	open val type: T,
	open val internalName: PropertyKey?,
	open val arguments: List<ScriptArgument>,
	open val enabled: Boolean
) {
	abstract class Builder<T> {
		protected var type: T? = null
		protected var internalName: PropertyKey? = null
		protected var arguments: List<ScriptArgument>? = null
		protected var enabled: Boolean? = null

		fun type(type: T?) = apply { this.type = type }
		fun internalName(internalName: PropertyKey?) = apply { this.internalName = internalName }
		fun arguments(arguments: List<ScriptArgument>?) = apply { this.arguments = arguments }
		fun enabled(enabled: Boolean?) = apply { this.enabled = enabled }

		protected fun throwIllegalStateExceptionForField(fieldName: String): Nothing {
			throw IllegalStateException("Field '$fieldName' is null.")
		}
	}
}

data class ScriptAction(
	override val type: ScriptActionType,
	override val internalName: PropertyKey?,
	override val arguments: List<ScriptArgument>,
	override val enabled: Boolean
) : ScriptContent<ScriptActionType>(type, internalName, arguments, enabled) {

	class Builder : ScriptContent.Builder<ScriptActionType>() {
		fun build() = ScriptAction(
			type = type ?: throwIllegalStateExceptionForField("type"),
			internalName = internalName,
			arguments = arguments ?: throwIllegalStateExceptionForField("arguments"),
			enabled = enabled ?: throwIllegalStateExceptionForField("enabled")
		)
	}
}

data class ScriptCondition(
	override val type: ScriptConditionType,
	override val internalName: PropertyKey?,
	override val arguments: List<ScriptArgument>,
	override val enabled: Boolean,
	val inverted: Boolean
) : ScriptContent<ScriptConditionType>(type, internalName, arguments, enabled) {

	class Builder : ScriptContent.Builder<ScriptConditionType>() {

		private var inverted: Boolean? = null

		fun inverted(inverted: Boolean?) = apply { this.inverted = inverted }

		fun build() = ScriptCondition(
			type = type ?: throwIllegalStateExceptionForField("type"),
			internalName = internalName,
			arguments = arguments ?: throwIllegalStateExceptionForField("arguments"),
			enabled = enabled ?: throwIllegalStateExceptionForField("enabled"),
			inverted = inverted ?: throwIllegalStateExceptionForField("inverted")
		)
	}
}

data class ScriptOrCondition(
	val conditions: List<ScriptCondition>
)
