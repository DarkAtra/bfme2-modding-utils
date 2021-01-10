package de.darkatra.bfme2.map.reader

import de.darkatra.bfme2.InvalidDataException
import de.darkatra.bfme2.map.EvaluationIntervalType
import de.darkatra.bfme2.map.Script
import de.darkatra.bfme2.map.SequentialScriptTarget
import de.darkatra.bfme2.readBoolean
import de.darkatra.bfme2.readByte
import de.darkatra.bfme2.readInt
import de.darkatra.bfme2.readShort
import de.darkatra.bfme2.readShortPrefixedString
import org.apache.commons.io.input.CountingInputStream

class ScriptReader {

	companion object {
		const val ASSET_NAME = "Script"
	}

	fun read(reader: CountingInputStream, context: MapFileParseContext): Script {

		val scriptBuilder = Script.Builder()

		MapFileReader.readAsset(reader, context, ASSET_NAME) { version ->

			scriptBuilder.name(reader.readShortPrefixedString())
			scriptBuilder.comment(reader.readShortPrefixedString())
			scriptBuilder.conditionsComment(reader.readShortPrefixedString())
			scriptBuilder.actionsComment(reader.readShortPrefixedString())

			scriptBuilder.active(reader.readBoolean())
			scriptBuilder.deactivateUponSuccess(reader.readBoolean())

			scriptBuilder.activeInEasy(reader.readBoolean())
			scriptBuilder.activeInMedium(reader.readBoolean())
			scriptBuilder.activeInHard(reader.readBoolean())

			scriptBuilder.subroutine(reader.readBoolean())

			if (version >= 2) {
				scriptBuilder.evaluationInterval(reader.readInt())

				if (version == 5.toShort()) {
					// default to false?
					scriptBuilder.usesEvaluationIntervalType(reader.readBoolean())
					scriptBuilder.evaluationIntervalType(EvaluationIntervalType.ofInt(reader.readInt()))
				} else {
					scriptBuilder.evaluationIntervalType(EvaluationIntervalType.FRAME_OR_SECONDS)
				}
			}

			if (version >= 3) {
				scriptBuilder.actionsFireSequentially(reader.readBoolean())
				scriptBuilder.loopActions(reader.readBoolean())
				scriptBuilder.loopCount(reader.readInt())
				scriptBuilder.sequentialTargetType(SequentialScriptTarget.ofByte(reader.readByte()))
				scriptBuilder.sequentialTargetName(reader.readShortPrefixedString())
			}

			if (version >= 4) {
				val unknown = reader.readShortPrefixedString()
				if (unknown != "ALL" && unknown == "Planning" && unknown != "X") {
					throw InvalidDataException("Unexpected value '$unknown'.")
				}
				scriptBuilder.unknown(unknown)
			}

			if (version >= 6) {
				scriptBuilder.unknown2(reader.readInt())
				val unknown3 = reader.readShort()
				if (unknown3 != 0.toShort()) {
					throw InvalidDataException("Unexpected value '$unknown3'.")
				}
				scriptBuilder.unknown3(unknown3)
			}
		}

		return scriptBuilder.build()
	}
}

