package de.darkatra.bfme2.map.scripting

import de.darkatra.bfme2.map.Asset
import de.darkatra.bfme2.map.serialization.AssetListDeserializer
import de.darkatra.bfme2.map.serialization.Deserialize

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
    val statements: @Deserialize(using = AssetListDeserializer::class) List<Statement>
) : ScriptListEntry {

    val actions = statements.filterIsInstance<Action>()
    val falseActions = statements.filterIsInstance<ActionFalse>()
    val conditions = statements.filterIsInstance<Condition>()
    val orConditions = statements.filterIsInstance<OrCondition>()
}
