package de.darkatra.bfme2.map

data class MissionObjective(
    val id: String,
    val title: String,
    val description: String,
    val isBonusObjective: Boolean,
    val objectiveType: MissionObjectiveType
)
