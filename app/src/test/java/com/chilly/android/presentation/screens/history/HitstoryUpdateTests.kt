package com.chilly.android.presentation.screens.history

import com.chilly.android.data.sample.place1
import com.chilly.android.data.sample.place2
import com.chilly.android.domain.model.HistoryItem
import com.chilly.android.utils.testUpdate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class HistoryUpdateTest {

    private lateinit var underTest: HistoryUpdate

    private val historyItem1 = HistoryItem(
        id = 1,
        place = place1,
        timestamp = LocalDateTime.now().minusDays(1)
    )

    private val historyItem2 = HistoryItem(
        id = 2,
        place = place2,
        timestamp = LocalDateTime.now().minusDays(2)
    )

    private val historyItems = listOf(historyItem1, historyItem2)

    @BeforeEach
    fun setup() {
        underTest = HistoryUpdate()
    }

    // UI Event Tests

    @Test
    fun `when item clicked, navigate news is sent`() {
        underTest.testUpdate(
            initialState = HistoryState(),
            event = HistoryEvent.UiEvent.ItemClicked(place1),
            expectedNewsProducer = {
                listOf(HistoryNews.NavigatePlaceInfo(place1))
            }
        )
    }

    @Test
    fun `when clear all confirmed, command is sent`() {
        underTest.testUpdate(
            initialState = HistoryState(historyItems = historyItems),
            event = HistoryEvent.UiEvent.ClearAllConfirmed,
            expectedCommandsProducer = {
                listOf(HistoryCommand.ClearHistory)
            }
        )
    }

    @Test
    fun `when item swiped to delete, command is sent`() {
        underTest.testUpdate(
            initialState = HistoryState(historyItems = historyItems),
            event = HistoryEvent.UiEvent.ItemSwipedToDelete(historyItem1),
            expectedCommandsProducer = {
                listOf(HistoryCommand.DeleteItem(historyItem1))
            }
        )
    }

    @Test
    fun `when delete icon clicked, dialog is shown`() {
        underTest.testUpdate(
            initialState = HistoryState(showDeleteDialog = false),
            event = HistoryEvent.UiEvent.DeleteIconClicked,
            expectedStateProducer = {
                state.copy(showDeleteDialog = true)
            }
        )
    }

    @Test
    fun `when delete dialog dismissed, dialog is hidden`() {
        underTest.testUpdate(
            initialState = HistoryState(showDeleteDialog = true),
            event = HistoryEvent.UiEvent.DeleteDialogDismissed,
            expectedStateProducer = {
                state.copy(showDeleteDialog = false)
            }
        )
    }

    // Command Event Tests

    @Test
    fun `when history loaded, state is updated`() {
        underTest.testUpdate(
            initialState = HistoryState(historyItems = emptyList()),
            event = HistoryEvent.CommandEvent.HistoryLoaded(historyItems),
            expectedStateProducer = {
                state.copy(historyItems = historyItems)
            }
        )
    }

    @Test
    fun `when empty history loaded, state reflects empty list`() {
        underTest.testUpdate(
            initialState = HistoryState(historyItems = historyItems),
            event = HistoryEvent.CommandEvent.HistoryLoaded(emptyList()),
            expectedStateProducer = {
                state.copy(historyItems = emptyList())
            }
        )
    }

    // Complex Scenarios

    @Test
    fun `dialog state doesn't change when history is loaded`() {
        // When dialog is shown
        underTest.testUpdate(
            initialState = HistoryState(showDeleteDialog = true),
            event = HistoryEvent.CommandEvent.HistoryLoaded(historyItems),
            expectedStateProducer = {
                state.copy(historyItems = historyItems, showDeleteDialog = true)
            }
        )

        // When dialog is hidden
        underTest.testUpdate(
            initialState = HistoryState(showDeleteDialog = false),
            event = HistoryEvent.CommandEvent.HistoryLoaded(historyItems),
            expectedStateProducer = {
                state.copy(historyItems = historyItems, showDeleteDialog = false)
            }
        )
    }

    @Test
    fun `history items are replaced not appended when loaded`() {
        val initialItems = listOf(historyItem1)
        val newItems = listOf(historyItem2)

        underTest.testUpdate(
            initialState = HistoryState(historyItems = initialItems),
            event = HistoryEvent.CommandEvent.HistoryLoaded(newItems),
            expectedStateProducer = {
                state.copy(historyItems = newItems)
            }
        )
    }
}
