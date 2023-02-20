package de.darkatra.bfme2.v2.map.scripting

class Statements<T : Statement> : ArrayList<T>() {

    val actions = filterIsInstance<Action>()
    val falseActions = filterIsInstance<ActionFalse>()
    val conditions = filterIsInstance<Condition>()
    val orConditions = filterIsInstance<OrCondition>()
}
