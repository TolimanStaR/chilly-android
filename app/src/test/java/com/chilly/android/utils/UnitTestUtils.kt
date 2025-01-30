package com.chilly.android.utils

import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import ru.tinkoff.kotea.core.CommandsFlowHandler
import ru.tinkoff.kotea.core.Update

inline fun <
        reified S: Any,
        reified E: Any,
        reified C: Any,
        reified N: Any,
        reified U: Update<S, E, C, N>> U.testUpdate(
    initialState: S,
    event: E,
    expectedStateProducer: StateAndEvent<S, E>.() -> S = { initialState },
    expectedCommandsProducer: StateAndEvent<S, E>.() -> List<C> = { emptyList() },
    expectedNewsProducer: StateAndEvent<S, E>.() -> List<N> = { emptyList() },
    configuration: () -> Unit = {},
    verification: () -> Unit = {}
) {
    val stateAndEvent = StateAndEvent(initialState, event)
    val expectedState = stateAndEvent.expectedStateProducer()
    val expectedCommands = stateAndEvent.expectedCommandsProducer()
    val expectedNews = stateAndEvent.expectedNewsProducer()

    configuration.invoke()

    val next = update(initialState, event)

    assertEquals(next.state, expectedState)
    assertEquals(next.commands, expectedCommands)
    assertEquals(next.news, expectedNews)

    verification.invoke()
}

class StateAndEvent<S: Any, E: Any> (
    val state: S,
    val event: E
)

inline fun <
        reified C: Any,
        reified E: Any,
        reified CFH: CommandsFlowHandler<C, E>> CFH.testCommandFlow(
    noinline commandsProducer: suspend FlowCollector<C>.() -> Unit = {},
    crossinline eventProducer: () -> List<E> = { emptyList() },
    crossinline configuration: () -> Unit = {},
    crossinline assertions: () -> Unit = {}
) = runTest {
    val commands = flow(commandsProducer)
    configuration.invoke()

    val results = eventProducer.invoke()
    val events = handle(commands).toList()
    assertEquals(events, results)
    assertions.invoke()
}