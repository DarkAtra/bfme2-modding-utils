package de.darkatra.bfme2.map.reader

import de.darkatra.bfme2.InvalidDataException
import de.darkatra.bfme2.map.EvaluationIntervalType
import de.darkatra.bfme2.map.Script
import de.darkatra.bfme2.map.SequentialScriptTarget
import de.darkatra.bfme2.readBoolean
import de.darkatra.bfme2.readByte
import de.darkatra.bfme2.readInt
import de.darkatra.bfme2.readUInt
import de.darkatra.bfme2.readUShort
import de.darkatra.bfme2.readUShortPrefixedString
import org.apache.commons.io.input.CountingInputStream

class ScriptReader {

	companion object {
		const val ASSET_NAME = "Script"
	}

	fun read(reader: CountingInputStream, context: MapFileParseContext): Script {

		val scriptBuilder = Script.Builder()

		MapFileReader.readAsset(reader, context, ASSET_NAME) { version ->

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
		}

		return scriptBuilder.build()
	}
}

