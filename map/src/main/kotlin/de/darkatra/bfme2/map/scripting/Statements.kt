package de.darkatra.bfme2.map.scripting

import de.darkatra.bfme2.PublicApi
import io.goodforgod.graalvm.hint.annotation.ReflectionHint

@ReflectionHint(ReflectionHint.AccessType.ALL_DECLARED_CONSTRUCTORS, ReflectionHint.AccessType.ALL_DECLARED_METHODS)
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
