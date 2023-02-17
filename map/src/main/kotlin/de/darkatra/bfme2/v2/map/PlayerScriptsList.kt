package de.darkatra.bfme2.v2.map

import de.darkatra.bfme2.ConversionException
import de.darkatra.bfme2.v2.map.deserialization.AssetListDeserializer
import de.darkatra.bfme2.v2.map.deserialization.Deserialize
import de.darkatra.bfme2.v2.map.deserialization.ScriptListEntryDeserializer
import de.darkatra.bfme2.v2.map.deserialization.StatementDeserializer

@Asset(name = "PlayerScriptsList", version = 1u)
data class PlayerScriptsList(
    val scriptLists: @Deserialize(using = AssetListDeserializer::class) List<ScriptList>
) {

    @Asset(name = "ScriptList", version = 1u)
    data class ScriptList(
        val scripts:
        @Deserialize(using = AssetListDeserializer::class)
        List<@Deserialize(using = ScriptListEntryDeserializer::class) ScriptListEntry>
    )

    interface ScriptListEntry {
        val name: String
        val active: Boolean
        val subroutine: Boolean
    }

    @Asset(name = "ScriptGroup", version = 3u)
    data class ScriptFolder(
        override val name: String,
        override val active: Boolean,
        override val subroutine: Boolean,
        val scripts: List<@Deserialize(using = ScriptListEntryDeserializer::class) ScriptListEntry>
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
        val statements:
        @Deserialize(using = AssetListDeserializer::class)
        List<@Deserialize(using = StatementDeserializer::class) Statement>
    ) : ScriptListEntry {

        enum class EvaluationIntervalType(
            val uInt: UInt
        ) {
            OPERATION(0u),
            MOVE_FORCES(1u),
            BATTLE(2u),
            UPKEEP(3u),
            COMPLETE(4u),
            ANY(5u),
            FRAME_OR_SECONDS(6u);

            companion object {
                fun ofUInt(uInt: UInt): EvaluationIntervalType {
                    return values().find { it.uInt == uInt } ?: throw ConversionException("Unknown EvaluationIntervalType for uInt '$uInt'.")
                }
            }
        }

        enum class SequentialScriptTarget(
            val byte: Byte
        ) {
            TEAM(0),
            UNIT(1);

            companion object {
                fun ofByte(byte: Byte): SequentialScriptTarget {
                    return values().find { it.byte == byte } ?: throw ConversionException("Unknown SequentialScriptTarget for byte '$byte'.")
                }
            }
        }
    }

}
