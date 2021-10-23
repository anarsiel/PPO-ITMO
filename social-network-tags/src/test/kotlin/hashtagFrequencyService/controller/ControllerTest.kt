package hashtagFrequencyService.controller

import hashtagFrequencyService.apiClients.ApiClient
import hashtagFrequencyService.core.InternalServiceException
import hashtagFrequencyService.core.ValidationException
import hashtagFrequencyService.main.ServiceNetwork.VK
import hashtagFrequencyService.validator.Validator
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.kotlin.*
import kotlin.test.assertFailsWith

class ControllerTest {
    private val validator = mock<Validator>()
    private val apiClient = mock<ApiClient>()
    private val serviceNetwork = VK
    private val controller = Controller(serviceNetwork, validator, apiClient)

    @After
    fun noMoreInteractions() {
        verifyNoMoreInteractions(validator, apiClient)
    }

    @Test
    fun ` calcFrequency correct response `() {
        val expectedResponse = arrayOf(1, 2, 3, 4, 5)

        whenever(apiClient.countPostsByTime(anyString(), any(), any())).thenReturn(expectedResponse)

        assertThat(controller.calcFrequency("tag", 0)).isEqualTo(expectedResponse)

        inOrder(validator, apiClient) {
            verify(validator).validateCalcFrequency("tag", 0)
            verify(apiClient).countPostsByTime("tag", arrayOf(), arrayOf())
        }
    }

    @Test
    fun ` validation error `() {
        whenever(validator.validateCalcFrequency(anyString(), anyInt())).doAnswer {
            throw ValidationException("msg")
        }

        val exception = assertFailsWith<InternalServiceException> {
            controller.calcFrequency("tag", 239)
        }
        assertThat("msg").isEqualTo(exception.message)

        verify(validator).validateCalcFrequency("tag", 239)
    }

    @Test
    fun ` error without message `() {
        whenever(apiClient.countPostsByTime(anyString(), any(), any())).doAnswer {
            throw Exception()
        }

        val exception = assertFailsWith<InternalServiceException> {
            controller.calcFrequency("tag", 0)
        }
        assertThat("Internal Service Error").isEqualTo(exception.message)

        inOrder(validator, apiClient) {
            verify(validator).validateCalcFrequency("tag", 0)
            verify(apiClient).countPostsByTime("tag", arrayOf(), arrayOf())
        }
    }
}