package de.darkatra.bfme2.v2.map.scripting

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
