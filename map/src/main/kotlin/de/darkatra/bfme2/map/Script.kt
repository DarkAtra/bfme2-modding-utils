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

    val evaluationInterval: UInt?,
    val usesEvaluationIntervalType: Boolean?,
    val evaluationIntervalType: EvaluationIntervalType,

    val actionsFireSequentially: Boolean?,

    val loopActions: Boolean?,
    val loopCount: UInt?,

    val sequentialTargetType: SequentialScriptTarget?,
    val sequentialTargetName: String?,

    // allowed values: ALL, Planning, X
    val unknown1: String?,
    val unknown2: Int?,
    val unknown3: UShort?,

    val orConditions: List<ScriptOrCondition>,
    val actionsIfTrue: List<ScriptAction>,
    val actionsIfFalse: List<ScriptAction>
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
        private var evaluationInterval: UInt? = null
        private var usesEvaluationIntervalType: Boolean? = null
        private var evaluationIntervalType: EvaluationIntervalType? = null
        private var actionsFireSequentially: Boolean? = null
        private var loopActions: Boolean? = null
        private var loopCount: UInt? = null
        private var sequentialTargetType: SequentialScriptTarget? = null
        private var sequentialTargetName: String? = null
        private var unknown1: String? = null
        private var unknown2: Int? = null
        private var unknown3: UShort? = null
        private var orConditions: List<ScriptOrCondition>? = null
        private var actionsIfTrue: List<ScriptAction>? = null
        private var actionsIfFalse: List<ScriptAction>? = null

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
        fun evaluationInterval(evaluationInterval: UInt) = apply { this.evaluationInterval = evaluationInterval }
        fun usesEvaluationIntervalType(usesEvaluationIntervalType: Boolean) = apply { this.usesEvaluationIntervalType = usesEvaluationIntervalType }
        fun evaluationIntervalType(evaluationIntervalType: EvaluationIntervalType) = apply { this.evaluationIntervalType = evaluationIntervalType }
        fun actionsFireSequentially(actionsFireSequentially: Boolean) = apply { this.actionsFireSequentially = actionsFireSequentially }
        fun loopActions(loopActions: Boolean) = apply { this.loopActions = loopActions }
        fun loopCount(loopCount: UInt) = apply { this.loopCount = loopCount }
        fun sequentialTargetType(sequentialTargetType: SequentialScriptTarget) = apply { this.sequentialTargetType = sequentialTargetType }
        fun sequentialTargetName(sequentialTargetName: String) = apply { this.sequentialTargetName = sequentialTargetName }
        fun unknown1(unknown1: String) = apply { this.unknown1 = unknown1 }
        fun unknown2(unknown2: Int) = apply { this.unknown2 = unknown2 }
        fun unknown3(unknown3: UShort) = apply { this.unknown3 = unknown3 }
        fun orConditions(orConditions: List<ScriptOrCondition>) = apply { this.orConditions = orConditions }
        fun actionsIfTrue(actionsIfTrue: List<ScriptAction>) = apply { this.actionsIfTrue = actionsIfTrue }
        fun actionsIfFalse(actionsIfFalse: List<ScriptAction>) = apply { this.actionsIfFalse = actionsIfFalse }

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
            usesEvaluationIntervalType = usesEvaluationIntervalType,
            evaluationIntervalType = evaluationIntervalType ?: throwIllegalStateExceptionForField("evaluationIntervalType"),
            actionsFireSequentially = actionsFireSequentially,
            loopActions = loopActions,
            loopCount = loopCount,
            sequentialTargetType = sequentialTargetType,
            sequentialTargetName = sequentialTargetName,
            unknown1 = unknown1,
            unknown2 = unknown2,
            unknown3 = unknown3,
            orConditions = orConditions ?: throwIllegalStateExceptionForField("orConditions"),
            actionsIfTrue = actionsIfTrue ?: throwIllegalStateExceptionForField("actionsIfTrue"),
            actionsIfFalse = actionsIfFalse ?: throwIllegalStateExceptionForField("actionsIfFalse")
        )

        private fun throwIllegalStateExceptionForField(fieldName: String): Nothing {
            throw IllegalStateException("Field '$fieldName' is null.")
        }
    }
}