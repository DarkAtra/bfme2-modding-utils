package de.darkatra.bfme2.map.scripting

enum class ScriptArgumentType(
    internal val id: UInt
) {

    INTEGER(0u),
    REAL_NUMBER(1u),
    SCRIPT_NAME(2u),
    TEAM_NAME(3u),
    COUNTER_NAME(4u),
    FLAG_NAME(5u),
    COMPARISON(6u),
    WAYPOINT_NAME(7u),
    BOOLEAN(8u),
    TRIGGER_AREA_NAME(9u),
    TEXT(10u),
    PLAYER_NAME(11u),
    SOUND_NAME(12u),
    SUBROUTINE_NAME(13u),
    UNIT_NAME(14u),
    OBJECT_NAME(15u),
    POSITION_COORDINATE(16u),
    ANGLE(17u),
    TEAM_STATE(18u),
    RELATION(19u),
    AI_MOOD(20u),
    SPEECH_NAME(21u),
    MUSIC_NAME(22u),
    MOVIE_NAME(23u),
    WAYPOINT_PATH_NAME(24u),
    LOCALIZED_STRING_NAME(25u),
    BRIDGE_NAME(26u),
    UNIT_OR_STRUCTURE_KIND(27u),
    ATTACK_PRIORITY_SET_NAME(28u),
    RADAR_EVENT_TYPE(29u),
    SPECIAL_POWER_NAME(30u),
    SCIENCE_NAME(31u),
    UPGRADE_NAME(32u),
    UNIT_ABILITY_NAME(33u),
    BOUNDARY_NAME(34u),
    BUILDABILITY(35u),
    SURFACE_TYPE(36u),
    CAMERA_SHAKE_INTENSITY(37u),
    COMMAND_BUTTON_NAME(38u),
    FONT_NAME(39u),
    OBJECT_STATUS(40u),
    TEAM_ABILITY_NAME(41u),
    SKIRMISH_APPROACH_PATH(42u),
    COLOR(43u),
    EMOTICON_NAME(44u),
    OBJECT_PANEL_FLAG(45u),
    FACTION_NAME(46u),
    OBJECT_TYPE_LIST_NAME(47u),
    MAP_REVEAL_NAME(48u),
    SCIENCE_AVAILABILITY_NAME(49u),
    EVACUATE_CONTAINER_SIDE(50u),
    PERCENTAGE(51u),
    PERCENTAGE_2(52u),
    UNIT_REFERENCE(54u),
    TEAM_REFERENCE(55u),
    NEAR_OR_FAR(56u),
    MATH_OPERATOR(57u),
    MODEL_CONDITION(58u),
    AUDIO_NAME(59u),
    REVERB_ROOM_TYPE(60u),
    OBJECT_TYPE(61u),
    HERO(62u),
    EMOTION(63u),
    UNKNOWN_1(64u),
    OBJECTIVE_COMPLETE(77u)
}
