package de.darkatra.bfme2.map

data class Script(
	val name: String,
	val comment: String,
	val conditionsComment: String,
	val actionsComment: String,

	val active: Boolean,
	val deactivateUponSuccess: Boolean,
	val activeInEasy: Boolean,
	val activeInMedium: Boolean,
	val activeInHard: Boolean,

	val subroutine: Boolean, // TODO: is this a setting in the worldbuilder?

	val evaluationInterval: Int?,
	val usesEvaluationIntervalType: Boolean,
	val evaluationIntervalType: EvaluationIntervalType,

	val actionsFireSequentially: Boolean?,

	val loopActions: Boolean?,
	val loopCount: Int?,

	val sequentialTargetType: SequentialScriptTarget?,
	val sequentialTargetName: String?,

	// allowed values: ALL, Planning, X
	val unknown1: String?,
	val unknown2: Int?,
	val unknown3: Short?
) {

	class Builder {
		private var name: String? = null
		private var comment: String? = null
		private var conditionsComment: String? = null
		private var actionsComment: String? = null
		private var active: Boolean? = null
		private var deactivateUponSuccess: Boolean? = null
		private var activeInEasy: Boolean? = null
		private var activeInMedium: Boolean? = null
		private var activeInHard: Boolean? = null
		private var subroutine: Boolean? = null
		private var evaluationInterval: Int? = null
		private var usesEvaluationIntervalType: Boolean? = null
		private var evaluationIntervalType: EvaluationIntervalType? = null
		private var actionsFireSequentially: Boolean? = null
		private var loopActions: Boolean? = null
		private var loopCount: Int? = null
		private var sequentialTargetType: SequentialScriptTarget? = null
		private var sequentialTargetName: String? = null
		private var unknown1: String? = null
		private var unknown2: Int? = null
		private var unknown3: Short? = null

		fun name(name: String) = apply { this.name = name }
		fun comment(comment: String) = apply { this.comment = comment }
		fun conditionsComment(conditionsComment: String) = apply { this.conditionsComment = conditionsComment }
		fun actionsComment(actionsComment: String) = apply { this.actionsComment = actionsComment }
		fun active(active: Boolean) = apply { this.active = active }
		fun deactivateUponSuccess(deactivateUponSuccess: Boolean) = apply { this.deactivateUponSuccess = deactivateUponSuccess }
		fun activeInEasy(activeInEasy: Boolean) = apply { this.activeInEasy = activeInEasy }
		fun activeInMedium(activeInMedium: Boolean) = apply { this.activeInMedium = activeInMedium }
		fun activeInHard(activeInHard: Boolean) = apply { this.activeInHard = activeInHard }
		fun subroutine(subroutine: Boolean) = apply { this.subroutine = subroutine }
		fun evaluationInterval(evaluationInterval: Int) = apply { this.evaluationInterval = evaluationInterval }
		fun usesEvaluationIntervalType(usesEvaluationIntervalType: Boolean) = apply { this.usesEvaluationIntervalType = usesEvaluationIntervalType }
		fun evaluationIntervalType(evaluationIntervalType: EvaluationIntervalType) = apply { this.evaluationIntervalType = evaluationIntervalType }
		fun actionsFireSequentially(actionsFireSequentially: Boolean) = apply { this.actionsFireSequentially = actionsFireSequentially }
		fun loopActions(loopActions: Boolean) = apply { this.loopActions = loopActions }
		fun loopCount(loopCount: Int) = apply { this.loopCount = loopCount }
		fun sequentialTargetType(sequentialTargetType: SequentialScriptTarget) = apply { this.sequentialTargetType = sequentialTargetType }
		fun sequentialTargetName(sequentialTargetName: String) = apply { this.sequentialTargetName = sequentialTargetName }
		fun unknown1(unknown1: String) = apply { this.unknown1 = unknown1 }
		fun unknown2(unknown2: Int) = apply { this.unknown2 = unknown2 }
		fun unknown3(unknown3: Short) = apply { this.unknown3 = unknown3 }

		fun build() = Script(
			name = name ?: throwIllegalStateExceptionForField("name"),
			comment = comment ?: throwIllegalStateExceptionForField("comment"),
			conditionsComment = conditionsComment ?: throwIllegalStateExceptionForField("conditionsComment"),
			actionsComment = actionsComment ?: throwIllegalStateExceptionForField("actionsComment"),
			active = active ?: throwIllegalStateExceptionForField("active"),
			deactivateUponSuccess = deactivateUponSuccess ?: throwIllegalStateExceptionForField("deactivateUponSuccess"),
			activeInEasy = activeInEasy ?: throwIllegalStateExceptionForField("activeInEasy"),
			activeInMedium = activeInMedium ?: throwIllegalStateExceptionForField("activeInMedium"),
			activeInHard = activeInHard ?: throwIllegalStateExceptionForField("activeInHard"),
			subroutine = subroutine ?: throwIllegalStateExceptionForField("subroutine"),
			evaluationInterval = evaluationInterval ?: throwIllegalStateExceptionForField("evaluationInterval"),
			usesEvaluationIntervalType = usesEvaluationIntervalType ?: throwIllegalStateExceptionForField("usesEvaluationIntervalType"),
			evaluationIntervalType = evaluationIntervalType ?: throwIllegalStateExceptionForField("evaluationIntervalType"),
			actionsFireSequentially = actionsFireSequentially ?: throwIllegalStateExceptionForField("actionsFireSequentially"),
			loopActions = loopActions ?: throwIllegalStateExceptionForField("loopActions"),
			loopCount = loopCount ?: throwIllegalStateExceptionForField("loopCount"),
			sequentialTargetType = sequentialTargetType ?: throwIllegalStateExceptionForField("sequentialTargetType"),
			sequentialTargetName = sequentialTargetName ?: throwIllegalStateExceptionForField("sequentialTargetName"),
			unknown1 = unknown1 ?: throwIllegalStateExceptionForField("unknown1"),
			unknown2 = unknown2 ?: throwIllegalStateExceptionForField("unknown2"),
			unknown3 = unknown3 ?: throwIllegalStateExceptionForField("unknown3")
		)

		private fun throwIllegalStateExceptionForField(fieldName: String): Nothing {
			throw IllegalStateException("Field '$fieldName' is null.")
		}
	}
}
