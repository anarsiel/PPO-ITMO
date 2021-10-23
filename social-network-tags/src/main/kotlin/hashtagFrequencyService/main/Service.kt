package hashtagFrequencyService.main

import hashtagFrequencyService.main.ServiceNetwork.VK
import hashtagFrequencyService.controller.Controller
import hashtagFrequencyService.core.InternalServiceException

class Service(
    private val serviceNetwork: ServiceNetwork = VK,
    private val controller: Controller = Controller(serviceNetwork)
) {
    fun calcFrequency(tag: String, hours: Int = 1): Response {
        return try {
            val frequency = controller.calcFrequency(tag, hours)
            SuccessfulResponse(frequency = frequency)
        } catch (e: InternalServiceException) {
            UnsuccessfulResponse(error = e.message ?: "Unexpected error")
        }
    }
}