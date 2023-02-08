package de.darkatra.bfme2.map.reader

import de.darkatra.bfme2.InvalidDataException
import de.darkatra.bfme2.Vector3
import de.darkatra.bfme2.map.AssetName
import de.darkatra.bfme2.map.EvaluationIntervalType
import de.darkatra.bfme2.map.Script
import de.darkatra.bfme2.map.ScriptAction
import de.darkatra.bfme2.map.ScriptActionType
import de.darkatra.bfme2.map.ScriptArgument
import de.darkatra.bfme2.map.ScriptArgumentType
import de.darkatra.bfme2.map.ScriptCondition
import de.darkatra.bfme2.map.ScriptConditionType
import de.darkatra.bfme2.map.ScriptOrCondition
import de.darkatra.bfme2.map.SequentialScriptTarget
import de.darkatra.bfme2.readBoolean
import de.darkatra.bfme2.readByte
import de.darkatra.bfme2.readFloat
import de.darkatra.bfme2.readInt
import de.darkatra.bfme2.readUInt
import de.darkatra.bfme2.readUIntAsBoolean
import de.darkatra.bfme2.readUShort
import de.darkatra.bfme2.readUShortPrefixedString
import org.apache.commons.io.input.CountingInputStream

class ScriptReader(
    private val propertyKeyReader: PropertyKeyReader
) {

    companion object {
        const val MINIMUM_VERSION_WITH_INTERNAL_NAME_IN_SCRIPT_ACTION = 2u
        const val MINIMUM_VERSION_WITH_ENABLED_FLAG_IN_SCRIPT_ACTION = 3u
        const val MINIMUM_VERSION_WITH_INTERNAL_NAME_IN_SCRIPT_CONDITION = 4u
        const val MINIMUM_VERSION_WITH_ENABLED_FLAG_IN_SCRIPT_CONDITION = 5u
    }

    fun read(reader: CountingInputStream, context: MapFileParseContext): Script {

        val scriptBuilder = Script.Builder()

        MapFileReader.readAsset(reader, context, AssetName.SCRIPT.assetName) { version ->

            scriptBuilder.name(reader.readUShortPrefixedString())
            scriptBuilder.comment(reader.readUShortPrefixedString())
            scriptBuilder.conditionsComment(reader.readUShortPrefixedString())
            scriptBuilder.actionsComment(reader.readUShortPrefixedString())

            scriptBuilder.active(reader.readBoolean())
            scriptBuilder.deactivateUponSuccess(reader.readBoolean())

            scriptBuilder.activeInEasy(reader.readBoolean())
            scriptBuilder.activeInMedium(reader.readBoolean())
            scriptBuilder.activeInHard(reader.readBoolean())

            scriptBuilder.subroutine(reader.readBoolean())

            if (version >= 2u) {
                scriptBuilder.evaluationInterval(reader.readUInt())

                if (version == 5.toUShort()) {
                    // default to false?
                    scriptBuilder.usesEvaluationIntervalType(reader.readBoolean())
                    scriptBuilder.evaluationIntervalType(EvaluationIntervalType.ofUInt(reader.readUInt()))
                } else {
                    scriptBuilder.evaluationIntervalType(EvaluationIntervalType.FRAME_OR_SECONDS)
                }
            }

            if (version >= 3u) {
                scriptBuilder.actionsFireSequentially(reader.readBoolean())
                scriptBuilder.loopActions(reader.readBoolean())
                scriptBuilder.loopCount(reader.readUInt())
                scriptBuilder.sequentialTargetType(SequentialScriptTarget.ofByte(reader.readByte()))
                scriptBuilder.sequentialTargetName(reader.readUShortPrefixedString())
            }

            if (version >= 4u) {
                val unknown = reader.readUShortPrefixedString()
                if (unknown != "ALL" && unknown == "Planning" && unknown != "X") {
                    throw InvalidDataException("Unexpected value '$unknown'.")
                }
                scriptBuilder.unknown1(unknown)
            }

            if (version >= 6u) {
                scriptBuilder.unknown2(reader.readInt())
                val unknown3 = reader.readUShort()
                if (unknown3 != 0.toUShort()) {
                    throw InvalidDataException("Unexpected value '$unknown3'.")
                }
                scriptBuilder.unknown3(unknown3)
            }

            val orConditions = mutableListOf<ScriptOrCondition>()
            val actionsIfTrue = mutableListOf<ScriptAction>()
            val actionsIfFalse = mutableListOf<ScriptAction>()

            MapFileReader.readAssets(reader, context) { assetName ->
                when (assetName) {
                    AssetName.OR_CONDITION.assetName -> orConditions.add(
                        readScriptOrCondition(reader, context)
                    )

                    AssetName.SCRIPT_ACTION.assetName -> MapFileReader.readAsset(reader, context, assetName) { version ->
                        actionsIfTrue.add(
                            readScriptAction(reader, context, version)
                        )
                    }

                    AssetName.SCRIPT_ACTION_FALSE.assetName -> MapFileReader.readAsset(reader, context, assetName) { version ->
                        actionsIfFalse.add(
                            readScriptAction(reader, context, version)
                        )
                    }
                }
            }

            scriptBuilder.orConditions(orConditions)
            scriptBuilder.actionsIfTrue(actionsIfTrue)
            scriptBuilder.actionsIfFalse(actionsIfFalse)
        }

        return scriptBuilder.build()
    }

    private fun readScriptOrCondition(reader: CountingInputStream, context: MapFileParseContext): ScriptOrCondition {

        val conditions = mutableListOf<ScriptCondition>()

        MapFileReader.readAsset(reader, context, AssetName.OR_CONDITION.assetName) {
            MapFileReader.readAssets(reader, context) { assetName ->
                when (assetName) {
                    AssetName.CONDITION.assetName -> MapFileReader.readAsset(reader, context, AssetName.CONDITION.assetName) { version ->
                        conditions.add(
                            readScriptCondition(reader, context, version)
                        )
                    }

                    else -> throw InvalidDataException("Unexpected asset with name '$assetName' in ScriptOrCondition.")
                }
            }
        }

        return ScriptOrCondition(
            conditions = conditions
        )
    }

    private fun readScriptAction(reader: CountingInputStream, context: MapFileParseContext, version: UShort): ScriptAction {

        val type = ScriptActionType.ofId(reader.readUInt())

        val internalName = when {
            version >= MINIMUM_VERSION_WITH_INTERNAL_NAME_IN_SCRIPT_ACTION -> propertyKeyReader.read(reader, context)
            else -> null
        }

        val numberOfScriptArguments = reader.readUInt()

        val scriptArguments = mutableListOf<ScriptArgument>()
        for (i in 0u until numberOfScriptArguments step 1) {
            scriptArguments.add(
                readScriptArgument(reader)
            )
        }

        val enabled = when {
            version >= MINIMUM_VERSION_WITH_ENABLED_FLAG_IN_SCRIPT_ACTION -> reader.readUIntAsBoolean()
            else -> true
        }

        return ScriptAction(
            type = type,
            internalName = internalName,
            arguments = scriptArguments,
            enabled = enabled
        )
    }

    private fun readScriptCondition(reader: CountingInputStream, context: MapFileParseContext, version: UShort): ScriptCondition {

        val type = ScriptConditionType.ofId(reader.readUInt())

        val internalName = when {
            version >= MINIMUM_VERSION_WITH_INTERNAL_NAME_IN_SCRIPT_CONDITION -> propertyKeyReader.read(reader, context)
            else -> null
        }

        val numberOfScriptArguments = reader.readUInt()

        val scriptArguments = mutableListOf<ScriptArgument>()
        for (i in 0u until numberOfScriptArguments step 1) {
            scriptArguments.add(
                readScriptArgument(reader)
            )
        }

        val enabled = when {
            version >= MINIMUM_VERSION_WITH_ENABLED_FLAG_IN_SCRIPT_CONDITION -> reader.readUIntAsBoolean()
            else -> true
        }

        val inverted = when {
            version >= MINIMUM_VERSION_WITH_ENABLED_FLAG_IN_SCRIPT_CONDITION -> reader.readUIntAsBoolean()
            else -> true
        }

        return ScriptCondition(
            type = type,
            internalName = internalName,
            arguments = scriptArguments,
            enabled = enabled,
            inverted = inverted
        )
    }

    private fun readScriptArgument(reader: CountingInputStream): ScriptArgument {

        val argumentType = ScriptArgumentType.ofId(reader.readUInt())

        return if (argumentType == ScriptArgumentType.POSITION_COORDINATE) {
            ScriptArgument(
                argumentType = argumentType,
                position = Vector3(
                    x = reader.readFloat(),
                    y = reader.readFloat(),
                    z = reader.readFloat()
                )
            )
        } else {
            ScriptArgument(
                argumentType = argumentType,
                intValue = reader.readInt(),
                floatValue = reader.readFloat(),
                stringValue = reader.readUShortPrefixedString()
            )
        }
    }
}

