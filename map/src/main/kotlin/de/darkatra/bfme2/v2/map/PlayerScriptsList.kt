package de.darkatra.bfme2.v2.map

import de.darkatra.bfme2.v2.map.deserialization.AssetListDeserializer
import de.darkatra.bfme2.v2.map.deserialization.Deserialize
import de.darkatra.bfme2.v2.map.deserialization.ScriptListEntryDeserializer

@Asset(name = "PlayerScriptsList", version = 1u)
data class PlayerScriptsList(
    val scriptLists: @Deserialize(using = AssetListDeserializer::class) List<ScriptList>
) {

    @Asset(name = "ScriptList", version = 1u)
    data class ScriptList(
        val scripts:
        @Deserialize(using = AssetListDeserializer::class)
        List<ScriptListEntry>
    )

    @Deserialize(using = ScriptListEntryDeserializer::class)
    sealed interface ScriptListEntry {
        val name: String
        val active: Boolean
        val subroutine: Boolean
    }

    @Asset(name = "ScriptGroup", version = 3u)
    data class ScriptFolder(
        override val name: String,
        override val active: Boolean,
        override val subroutine: Boolean,
        val scripts: List<ScriptListEntry>
    ) : ScriptListEntry

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

        enum class EvaluationIntervalType(
            internal val uInt: UInt
        ) {
            OPERATION(0u),
            MOVE_FORCES(1u),
            BATTLE(2u),
            UPKEEP(3u),
            COMPLETE(4u),
            ANY(5u),
            FRAME_OR_SECONDS(6u)
        }

        enum class SequentialScriptTarget(
            internal val byte: Byte
        ) {
            TEAM(0),
            UNIT(1)
        }
    }
}
