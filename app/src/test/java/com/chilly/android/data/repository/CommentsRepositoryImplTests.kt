package com.chilly.android.data.repository

import com.chilly.android.data.remote.api.CommentsApi
import com.chilly.android.data.remote.dto.CommentDto
import com.chilly.android.data.remote.dto.request.CommentRequest
import com.chilly.android.domain.repository.CommentsRepository
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

@ExperimentalCoroutinesApi
class CommentsRepositoryImplTest {

    @MockK
    private lateinit var commentsApi: CommentsApi

    private lateinit var repository: CommentsRepositoryImpl

    @BeforeEach
    fun setup() {
        MockKAnnotations.init(this)
        repository = CommentsRepositoryImpl(commentsApi)
    }

    @Test
    fun `sendReview should return ReviewCreated on successful comment creation`() = runTest {
        // Arrange
        val request = CommentRequest(placeId = 1, rating = 5f, commentText = "Great place!")
        coEvery { commentsApi.sendComment(request) } returns Result.success(CommentsApi.CommentResult.CommentCreated)

        // Act
        val result = repository.sendReview(request)

        // Assert
        assertTrue(result.isSuccess)
        assertEquals(CommentsRepository.SendResult.ReviewCreated, result.getOrNull())
        coVerify(exactly = 1) { commentsApi.sendComment(request) }
    }

    @Test
    fun `sendReview should return ReviewModified on successful comment modification`() = runTest {
        // Arrange
        val request = CommentRequest(placeId = 1, rating = 4f, commentText = "Updated review")
        coEvery { commentsApi.sendComment(request) } returns Result.success(CommentsApi.CommentResult.CommentModified)

        // Act
        val result = repository.sendReview(request)

        // Assert
        assertTrue(result.isSuccess)
        assertEquals(CommentsRepository.SendResult.ReviewModified, result.getOrNull())
        coVerify(exactly = 1) { commentsApi.sendComment(request) }
    }

    @Test
    fun `sendReview should propagate error from API`() = runTest {
        // Arrange
        val request = CommentRequest(placeId = 1, rating = 5f, commentText = "Great place!")
        val exception = RuntimeException("Network error")
        coEvery { commentsApi.sendComment(request) } returns Result.failure(exception)

        // Act
        val result = repository.sendReview(request)

        // Assert
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
        coVerify(exactly = 1) { commentsApi.sendComment(request) }
    }

    @Test
    fun `getComments should return empty flow initially`() = runTest {
        // Arrange
        val placeId = 1

        // Act
        val comments = repository.getComments(placeId).first()

        // Assert
        assertTrue(comments.isEmpty())
    }

    @Test
    fun `getComments should reset flow and lastFetchedPage when placeId changes`() = runTest {
        // Arrange
        val placeId1 = 1
        val placeId2 = 2
        val commentsList = listOf(
            CommentDto(id = 1, placeId = placeId1, userId = 10, text = "Great", rating = 5f, timestamp = 1672531200000) // 2023-01-01
        )

        coEvery { commentsApi.getCommentsPage(placeId1, 0) } returns Result.success(commentsList)

        // Act - fetch for first place
        repository.fetchNextCommentsPage(placeId1)
        val commentsForPlace1 = repository.getComments(placeId1).first()

        // Verify first place comments
        assertEquals(commentsList, commentsForPlace1)

        // Act - change place ID
        val commentsForPlace2 = repository.getComments(placeId2).first()

        // Assert - should be empty for new place
        assertTrue(commentsForPlace2.isEmpty())
    }

    @Test
    fun `fetchNextCommentsPage should return PageWithContent and update flow when comments exist`() = runTest {
        // Arrange
        val placeId = 1
        val commentsList = listOf(
            CommentDto(id = 1, placeId = placeId, userId = 10, text = "Great", rating = 5f, timestamp = 1672531200000), // 2023-01-01
            CommentDto(id = 2, placeId = placeId, userId = 20, text = "Good", rating = 4f, timestamp = 1672617600000)  // 2023-01-02
        )

        coEvery { commentsApi.getCommentsPage(placeId, 0) } returns Result.success(commentsList)

        // Act
        val result = repository.fetchNextCommentsPage(placeId)
        val comments = repository.getComments(placeId).first()

        // Assert
        assertTrue(result.isSuccess)
        assertEquals(CommentsRepository.FetchResult.PageWithContent, result.getOrNull())
        assertEquals(commentsList, comments)
        coVerify(exactly = 1) { commentsApi.getCommentsPage(placeId, 0) }
    }

    @Test
    fun `fetchNextCommentsPage should return EmptyPage when no comments exist`() = runTest {
        // Arrange
        val placeId = 1
        val emptyList = emptyList<CommentDto>()

        coEvery { commentsApi.getCommentsPage(placeId, 0) } returns Result.success(emptyList)

        // Act
        val result = repository.fetchNextCommentsPage(placeId)
        val comments = repository.getComments(placeId).first()

        // Assert
        assertTrue(result.isSuccess)
        assertEquals(CommentsRepository.FetchResult.EmptyPage, result.getOrNull())
        assertEquals(emptyList, comments)
        coVerify(exactly = 1) { commentsApi.getCommentsPage(placeId, 0) }
    }

    @Test
    fun `fetchNextCommentsPage should propagate error from API`() = runTest {
        // Arrange
        val placeId = 1
        val exception = RuntimeException("Network error")

        coEvery { commentsApi.getCommentsPage(placeId, 0) } returns Result.failure(exception)

        // Act
        val result = repository.fetchNextCommentsPage(placeId)

        // Assert
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
        coVerify(exactly = 1) { commentsApi.getCommentsPage(placeId, 0) }
    }

    @Test
    fun `fetchNextCommentsPage should append new comments to existing ones`() = runTest {
        // Arrange
        val placeId = 1
        val firstPageComments = listOf(
            CommentDto(id = 1, placeId = placeId, userId = 10, text = "Great", rating = 5f, timestamp = 1672531200000) // 2023-01-01
        )

        val secondPageComments = listOf(
            CommentDto(id = 2, placeId = placeId, userId = 20, text = "Good", rating = 4f, timestamp = 1672617600000) // 2023-01-02
        )

        coEvery { commentsApi.getCommentsPage(placeId, 0) } returns Result.success(firstPageComments)
        coEvery { commentsApi.getCommentsPage(placeId, 1) } returns Result.success(secondPageComments)

        // Act - fetch first page
        repository.fetchNextCommentsPage(placeId)
        val commentsAfterFirstPage = repository.getComments(placeId).first()

        // Assert first page
        assertEquals(firstPageComments, commentsAfterFirstPage)

        // Act - fetch second page
        repository.fetchNextCommentsPage(placeId)
        val commentsAfterSecondPage = repository.getComments(placeId).first()

        // Assert combined pages
        assertEquals(firstPageComments + secondPageComments, commentsAfterSecondPage)
        coVerify(exactly = 1) { commentsApi.getCommentsPage(placeId, 0) }
        coVerify(exactly = 1) { commentsApi.getCommentsPage(placeId, 1) }
    }

    @Test
    fun `fetchNextCommentsPage should increment page number on each successful call`() = runTest {
        // Arrange
        val placeId = 1
        val emptyList = emptyList<CommentDto>()

        coEvery { commentsApi.getCommentsPage(placeId, 0) } returns Result.success(emptyList)
        coEvery { commentsApi.getCommentsPage(placeId, 1) } returns Result.success(emptyList)
        coEvery { commentsApi.getCommentsPage(placeId, 2) } returns Result.success(emptyList)

        // Act & Assert - verify page increments
        repository.fetchNextCommentsPage(placeId)
        coVerify(exactly = 1) { commentsApi.getCommentsPage(placeId, 0) }

        repository.fetchNextCommentsPage(placeId)
        coVerify(exactly = 1) { commentsApi.getCommentsPage(placeId, 1) }

        repository.fetchNextCommentsPage(placeId)
        coVerify(exactly = 1) { commentsApi.getCommentsPage(placeId, 2) }
    }
}
