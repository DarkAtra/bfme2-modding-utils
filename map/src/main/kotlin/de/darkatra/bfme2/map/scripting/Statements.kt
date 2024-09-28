package de.darkatra.bfme2.map.scripting

import de.darkatra.bfme2.PublicApi

class Statements<T : Statement> : ArrayList<T>() {

    @PublicApi
    val actions = filterIsInstance<Action>()

    @PublicApi
    val falseActions = filterIsInstance<ActionFalse>()

    @PublicApi
    val conditions = filterIsInstance<Condition>()

    @PublicApi
    val orConditions = filterIsInstance<OrCondition>()
}
