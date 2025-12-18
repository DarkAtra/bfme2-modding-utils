package de.darkatra.bfme2.map.scripting

import de.darkatra.bfme2.PublicApi
import de.darkatra.bfme2.map.Asset
import de.darkatra.bfme2.map.serialization.AssetListSerde
import de.darkatra.bfme2.map.serialization.Serialize
import io.goodforgod.graalvm.hint.annotation.ReflectionHint

@ReflectionHint(ReflectionHint.AccessType.ALL_DECLARED_CONSTRUCTORS, ReflectionHint.AccessType.ALL_DECLARED_METHODS)
@Asset(name = "Script", version = 4u)
data class Script(
    override val name: String,
    val comment: String,
    val conditionsComment: String,
    val actionsComment: String,
    override val active: Boolean,
    val deactivateUponSuccess: Boolean,
    val activeInEasy: Boolean,
    val activeInMedium: Boolean,
    val activeInHard: Boolean,
    override val subroutine: Boolean,
    val evaluationInterval: UInt,
    val actionsFireSequentially: Boolean,
    val loopActions: Boolean,
    val loopCount: UInt,
    val sequentialTargetType: SequentialScriptTarget,
    val sequentialTargetName: String,
    val unknown1: String,
    val statements: @Serialize(using = AssetListSerde::class) List<Statement>
) : ScriptListEntry {

    @PublicApi
    val actions = statements.filterIsInstance<Action>()

    @PublicApi
    val falseActions = statements.filterIsInstance<ActionFalse>()

    @PublicApi
    val conditions = statements.filterIsInstance<Condition>()

    @PublicApi
    val orConditions = statements.filterIsInstance<OrCondition>()
}
