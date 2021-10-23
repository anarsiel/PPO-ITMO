package hashtagFrequencyService.main

import hashtagFrequencyService.controller.Controller
import hashtagFrequencyService.core.InternalServiceException
import hashtagFrequencyService.main.ServiceNetwork.VK
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.kotlin.*

open class ServiceTest {
    private val controller = mock<Controller>()
    private val vkService = Service(VK, controller)

    @After
    fun noMoreInteractions() {
        verifyNoMoreInteractions(controller)
    }

    @Test
    fun ` Successful CalcFrequency Test`(): Unit {
        whenever(controller.calcFrequency(anyString(), anyInt())).thenReturn(arrayOf(1, 2, 3, 4, 5))

        val expectedResponse = SuccessfulResponse(arrayOf(1, 2, 3, 4, 5))
        assertThat(vkService.calcFrequency("tag", 1)).isEqualTo(expectedResponse)

        verify(controller).calcFrequency("tag", 1)
    }

    @Test
    fun ` Unsuccessful CalcFrequency Test`(): Unit {
        whenever(controller.calcFrequency(anyString(), anyInt())).doAnswer { throw InternalServiceException("msg") }

        val expectedResponse = UnsuccessfulResponse(error = "msg")
        assertThat(vkService.calcFrequency("tag", 1)).isEqualTo(expectedResponse)

        verify(controller).calcFrequency("tag", 1)
    }
}