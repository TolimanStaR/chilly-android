package com.chilly.android.data.repository

import com.chilly.android.data.remote.api.UserApi
import com.chilly.android.data.remote.dto.UserDto
import com.chilly.android.data.remote.dto.request.ChangeInfoRequest
import com.chilly.android.data.remote.dto.request.ChangePasswordRequest
import com.chilly.android.data.remote.dto.request.ChangeUsernameRequest
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.IOException

@ExperimentalCoroutinesApi
class UserRepositoryImplTest {

    @MockK
    private lateinit var userApi: UserApi

    private lateinit var userRepository: UserRepositoryImpl

    // Test data
    private val testUser = UserDto(
        phone = "+1234567890",
        email = "test@example.com",
        name = "John",
        lastname = "Doe"
    )

    private val updatedUser = UserDto(
        phone = "+9876543210",
        email = "updated@example.com",
        name = "Jane",
        lastname = "Smith"
    )

    @BeforeEach
    fun setup() {
        MockKAnnotations.init(this)
        userRepository = UserRepositoryImpl(userApi)
    }

    @Test
    fun `getLoggedUser fetches user from API when not cached`() = runTest {
        // Arrange
        coEvery { userApi.getLoggedUser() } returns Result.success(testUser)

        // Act
        val result = userRepository.getLoggedUser()

        // Assert
        assertEquals(testUser, result)
        coVerify(exactly = 1) { userApi.getLoggedUser() }
    }

    @Test
    fun `getLoggedUser throws exception when API call fails`() = runTest {
        // Arrange
        val exception = IOException("Network error")
        coEvery { userApi.getLoggedUser() } returns Result.failure(exception)

        // Act & Assert
        val thrownException = assertThrows(IOException::class.java) {
            runBlocking {
                userRepository.getLoggedUser()
            }
        }

        assertEquals("Network error", thrownException.message)
        coVerify(exactly = 1) { userApi.getLoggedUser() }
    }

    @Test
    fun `getLoggedUser returns cached user on subsequent calls`() = runTest {
        // Arrange
        coEvery { userApi.getLoggedUser() } returns Result.success(testUser)

        // Act - first call should fetch from API
        val firstResult = userRepository.getLoggedUser()

        // Act - second call should use cache
        val secondResult = userRepository.getLoggedUser()

        // Assert
        assertEquals(testUser, firstResult)
        assertEquals(testUser, secondResult)
        coVerify(exactly = 1) { userApi.getLoggedUser() } // Should only call API once
    }

    @Test
    fun `editUserInfo calls both edit endpoints and updates cache on success`() = runTest {
        // Arrange
        val infoRequest = ChangeInfoRequest(name = updatedUser.name, lastname = updatedUser.lastname)
        val usernameRequest = ChangeUsernameRequest(email = updatedUser.email, phone = updatedUser.phone)

        coEvery { userApi.editUserInfo(infoRequest) } returns Result.success(Unit)
        coEvery { userApi.editPhoneAndEmail(usernameRequest) } returns Result.success(Unit)
        coEvery { userApi.getLoggedUser() } returns Result.success(updatedUser)

        // Act
        val result = userRepository.editUserInfo(updatedUser)

        // Assert
        assertTrue(result.isSuccess)
        assertEquals(updatedUser, result.getOrNull())

        // Verify all API calls were made
        coVerify(exactly = 1) { userApi.editUserInfo(infoRequest) }
        coVerify(exactly = 1) { userApi.editPhoneAndEmail(usernameRequest) }
        coVerify(exactly = 1) { userApi.getLoggedUser() }

        // Verify the cache is updated
        assertEquals(updatedUser, userRepository.getLoggedUser())
        // Second getLoggedUser should use cache, so API call count doesn't increase
        coVerify(exactly = 1) { userApi.getLoggedUser() }
    }

    @Test
    fun `editUserInfo returns failure when editUserInfo call fails`() = runTest {
        // Arrange
        val infoRequest = ChangeInfoRequest(name = updatedUser.name, lastname = updatedUser.lastname)
        val exception = IOException("Network error")

        coEvery { userApi.editUserInfo(infoRequest) } returns Result.failure(exception)

        // Act
        val result = userRepository.editUserInfo(updatedUser)

        // Assert
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())

        // Verify only the first API call was made
        coVerify(exactly = 1) { userApi.editUserInfo(infoRequest) }
        coVerify(exactly = 0) { userApi.editPhoneAndEmail(any()) }
        coVerify(exactly = 0) { userApi.getLoggedUser() }
    }

    @Test
    fun `editUserInfo returns failure when editPhoneAndEmail call fails`() = runTest {
        // Arrange
        val infoRequest = ChangeInfoRequest(name = updatedUser.name, lastname = updatedUser.lastname)
        val usernameRequest = ChangeUsernameRequest(email = updatedUser.email, phone = updatedUser.phone)
        val exception = IOException("Network error")

        coEvery { userApi.editUserInfo(infoRequest) } returns Result.success(Unit)
        coEvery { userApi.editPhoneAndEmail(usernameRequest) } returns Result.failure(exception)

        // Act
        val result = userRepository.editUserInfo(updatedUser)

        // Assert
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())

        // Verify first two API calls were made, but not the final one
        coVerify(exactly = 1) { userApi.editUserInfo(infoRequest) }
        coVerify(exactly = 1) { userApi.editPhoneAndEmail(usernameRequest) }
        coVerify(exactly = 0) { userApi.getLoggedUser() }
    }

    @Test
    fun `editUserInfo returns failure when getLoggedUser call fails`() = runTest {
        // Arrange
        val infoRequest = ChangeInfoRequest(name = updatedUser.name, lastname = updatedUser.lastname)
        val usernameRequest = ChangeUsernameRequest(email = updatedUser.email, phone = updatedUser.phone)
        val exception = IOException("Network error")

        coEvery { userApi.editUserInfo(infoRequest) } returns Result.success(Unit)
        coEvery { userApi.editPhoneAndEmail(usernameRequest) } returns Result.success(Unit)
        coEvery { userApi.getLoggedUser() } returns Result.failure(exception)

        // Act
        val result = userRepository.editUserInfo(updatedUser)

        // Assert
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())

        // Verify all API calls were made
        coVerify(exactly = 1) { userApi.editUserInfo(infoRequest) }
        coVerify(exactly = 1) { userApi.editPhoneAndEmail(usernameRequest) }
        coVerify(exactly = 1) { userApi.getLoggedUser() }
    }

    @Test
    fun `changePassword delegates to API and returns result`() = runTest {
        // Arrange
        val request = ChangePasswordRequest(
            oldPassword = "oldPass123",
            newPassword = "newPass456"
        )
        coEvery { userApi.changePassword(request) } returns Result.success(Unit)

        // Act
        val result = userRepository.changePassword(request)

        // Assert
        assertTrue(result.isSuccess)
        coVerify(exactly = 1) { userApi.changePassword(request) }
    }

    @Test
    fun `changePassword returns failure when API call fails`() = runTest {
        // Arrange
        val request = ChangePasswordRequest(
            oldPassword = "oldPass123",
            newPassword = "newPass456"
        )
        val exception = IOException("Network error")
        coEvery { userApi.changePassword(request) } returns Result.failure(exception)

        // Act
        val result = userRepository.changePassword(request)

        // Assert
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
        coVerify(exactly = 1) { userApi.changePassword(request) }
    }
}
