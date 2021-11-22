package de.darkatra.bfme2.map

abstract class ScriptContent<T>(
	open val type: T,
	open val internalName: PropertyKey?,
	open val arguments: List<ScriptArgument>,
	open val enabled: Boolean
)

data class ScriptAction(
	override val type: ScriptActionType,
	override val internalName: PropertyKey?,
	override val arguments: List<ScriptArgument>,
	override val enabled: Boolean
) : ScriptContent<ScriptActionType>(type, internalName, arguments, enabled)

data class ScriptCondition(
	override val type: ScriptConditionType,
	override val internalName: PropertyKey?,
	override val arguments: List<ScriptArgument>,
	override val enabled: Boolean,
	val inverted: Boolean
) : ScriptContent<ScriptConditionType>(type, internalName, arguments, enabled)

data class ScriptOrCondition(
	val conditions: List<ScriptCondition>
)
