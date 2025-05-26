package com.chilly.android.data.repository

import com.chilly.android.data.local.dao.HistoryDao
import com.chilly.android.data.local.dao.PlaceDao
import com.chilly.android.data.local.entity.EntryWithPlace
import com.chilly.android.data.local.entity.FavoriteItem
import com.chilly.android.data.local.entity.HistoryEntry
import com.chilly.android.data.local.entity.PlaceEntity
import com.chilly.android.data.mapper.HistoryMapper
import com.chilly.android.data.mapper.PlaceMapper
import com.chilly.android.data.remote.dto.PlaceDto
import com.chilly.android.domain.model.HistoryItem
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

@ExperimentalCoroutinesApi
class PlaceRepositoryImplTest {

    @MockK
    private lateinit var placeDao: PlaceDao

    @MockK
    private lateinit var historyDao: HistoryDao

    @MockK
    private lateinit var placeMapper: PlaceMapper

    @MockK
    private lateinit var historyMapper: HistoryMapper

    private lateinit var placeRepository: PlaceRepositoryImpl

    // Test data
    private val placeId = 1
    private val placeEntity = PlaceEntity(
        id = placeId,
        name = "Test Place",
        address = "Test Address",
        imageUrls = listOf("image1.jpg", "image2.jpg"),
        openHours = listOf("9-5 Mon-Fri"),
        phone = "+1234567890",
        rating = 4.5f,
        socials = listOf("instagram.com/testplace"),
        website = "testplace.com",
        yandexMapsLink = "maps.yandex.com/testplace",
        latitude = 55.123,
        longitude = 37.456
    )
    private val placeDto = PlaceDto(
        id = placeId,
        name = "Test Place",
        address = "Test Address",
        imageUrls = listOf("image1.jpg", "image2.jpg"),
        openHours = listOf("9-5 Mon-Fri"),
        phone = "+1234567890",
        rating = 4.5f,
        socials = listOf("instagram.com/testplace"),
        website = "testplace.com",
        yandexMapsLink = "maps.yandex.com/testplace",
        latitude = 55.123,
        longitude = 37.456
    )
    private val timestamp = LocalDateTime.now()
    private val historyEntry = HistoryEntry(placeId = placeId, timestamp = timestamp, id = 1)
    private val entryWithPlace = EntryWithPlace(entry = historyEntry, place = placeEntity)
    private val historyItem = HistoryItem(place = placeDto, timestamp = timestamp, id = 1)

    @BeforeEach
    fun setup() {
        MockKAnnotations.init(this)
        placeRepository = PlaceRepositoryImpl(
            placeDao = placeDao,
            historyDao = historyDao,
            placeMapper = placeMapper,
            historyMapper = historyMapper
        )

        // Common mappings
        every { placeMapper.toDto(placeEntity) } returns placeDto
        every { placeMapper.toEntity(placeDto) } returns placeEntity
        every { historyMapper.toModel(entryWithPlace) } returns historyItem
        every { historyMapper.toEntity(historyItem) } returns historyEntry
    }

    @Test
    fun `placeById returns success result when place exists`() = runTest {
        // Arrange
        coEvery { placeDao.findById(placeId) } returns placeEntity

        // Act
        val result = placeRepository.placeById(placeId)

        // Assert
        assertTrue(result.isSuccess)
        assertEquals(placeDto, result.getOrNull())
        coVerify(exactly = 1) { placeDao.findById(placeId) }
        verify(exactly = 1) { placeMapper.toDto(placeEntity) }
    }

    @Test
    fun `placeById returns failure result when place does not exist`() = runTest {
        // Arrange
        coEvery { placeDao.findById(placeId) } returns null

        // Act
        val result = placeRepository.placeById(placeId)

        // Assert
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is NoSuchElementException)
        coVerify(exactly = 1) { placeDao.findById(placeId) }
        verify(exactly = 0) { placeMapper.toDto(any()) }
    }

    @Test
    fun `savePlaces with writeToHistory false only inserts places`() = runTest {
        // Arrange
        val places = listOf(placeDto)
        val entities = listOf(placeEntity).toTypedArray()

        coEvery { placeDao.insertPlaces(*entities) } just Runs

        // Act
        placeRepository.savePlaces(places, writeToHistory = false)

        // Assert
        coVerify { placeDao.insertPlaces(*entities) }
        coVerify(exactly = 0) { historyDao.insertEntry(*anyVararg()) }
    }

    @Test
    fun `checkInFavorites returns true when place is in favorites`() = runTest {
        // Arrange
        val favoriteItem = FavoriteItem(placeId)
        coEvery { placeDao.searchInFavorites(placeId) } returns favoriteItem

        // Act
        val result = placeRepository.checkInFavorites(placeId)

        // Assert
        assertTrue(result)
        coVerify(exactly = 1) { placeDao.searchInFavorites(placeId) }
    }

    @Test
    fun `checkInFavorites returns false when place is not in favorites`() = runTest {
        // Arrange
        coEvery { placeDao.searchInFavorites(placeId) } returns null

        // Act
        val result = placeRepository.checkInFavorites(placeId)

        // Assert
        assertFalse(result)
        coVerify(exactly = 1) { placeDao.searchInFavorites(placeId) }
    }

    @Test
    fun `updateFavorites adds place to favorites when isInFavorites is true`() = runTest {
        // Arrange
        val favoriteItem = FavoriteItem(placeId)
        coEvery { placeDao.markAsFavorite(favoriteItem) } just Runs

        // Act
        placeRepository.updateFavorites(placeId, isInFavorites = true)

        // Assert
        coVerify(exactly = 1) { placeDao.markAsFavorite(favoriteItem) }
        coVerify(exactly = 0) { placeDao.removeFromFavorites(any()) }
    }

    @Test
    fun `updateFavorites removes place from favorites when isInFavorites is false`() = runTest {
        // Arrange
        val favoriteItem = FavoriteItem(placeId)
        coEvery { placeDao.removeFromFavorites(favoriteItem) } just Runs

        // Act
        placeRepository.updateFavorites(placeId, isInFavorites = false)

        // Assert
        coVerify(exactly = 0) { placeDao.markAsFavorite(any()) }
        coVerify(exactly = 1) { placeDao.removeFromFavorites(favoriteItem) }
    }

    @Test
    fun `getHistoryFlow returns mapped history items`() = runTest {
        // Arrange
        val entriesWithPlaces = listOf(entryWithPlace)
        val expectedHistoryItems = listOf(historyItem)

        every { historyDao.getHistory() } returns flowOf(entriesWithPlaces)

        // Act
        val result = placeRepository.getHistoryFlow().first()

        // Assert
        assertEquals(expectedHistoryItems, result)
        verify(exactly = 1) { historyDao.getHistory() }
        verify(exactly = 1) { historyMapper.toModel(entryWithPlace) }
    }

    @Test
    fun `getFavoritesFlow returns mapped place DTOs`() = runTest {
        // Arrange
        val placeEntities = listOf(placeEntity)
        val expectedPlaceDtos = listOf(placeDto)

        every { placeDao.getFavorites() } returns flowOf(placeEntities)

        // Act
        val result = placeRepository.getFavoritesFlow().first()

        // Assert
        assertEquals(expectedPlaceDtos, result)
        verify(exactly = 1) { placeDao.getFavorites() }
        verify(exactly = 1) { placeMapper.toDto(placeEntity) }
    }

    @Test
    fun `clearHistory calls the DAO's clearHistory method`() = runTest {
        // Arrange
        coEvery { historyDao.clearHistory() } just Runs

        // Act
        placeRepository.clearHistory()

        // Assert
        coVerify(exactly = 1) { historyDao.clearHistory() }
    }

    @Test
    fun `removeItem maps the item to entity and calls the DAO's removeHistoryEntry method`() = runTest {
        // Arrange
        coEvery { historyDao.removeHistoryEntry(historyEntry) } just Runs

        // Act
        placeRepository.removeItem(historyItem)

        // Assert
        verify(exactly = 1) { historyMapper.toEntity(historyItem) }
        coVerify(exactly = 1) { historyDao.removeHistoryEntry(historyEntry) }
    }
}
