package hashtagFrequencyService.controller

import hashtagFrequencyService.apiClients.ApiClient
import hashtagFrequencyService.apiClients.VkApiClient
import hashtagFrequencyService.main.ServiceNetwork
import hashtagFrequencyService.core.InternalServiceException
import hashtagFrequencyService.main.ServiceNetwork.*
import hashtagFrequencyService.validator.Validator
import hashtagFrequencyService.validator.VkValidator

open class Controller(
    private val serviceNetwork: ServiceNetwork,
    private val validator: Validator = when (serviceNetwork) {
        VK -> VkValidator()
    },
    private val apiClient: ApiClient = when (serviceNetwork) {
        VK -> VkApiClient()
    }
) {
    open fun calcFrequency(tag: String, hours: Int): Array<Int> {
        try {
            validator.validateCalcFrequency(tag, hours)

            val currentTime = System.currentTimeMillis() / 1000
            val oneHourInSeconds = 60 * 60

            val startTimes = Array(hours) { i ->
                currentTime - (hours - i) * oneHourInSeconds
            }
            val endTimes = Array(hours) { i ->
                startTimes[i] + oneHourInSeconds
            }

            return apiClient.countPostsByTime(tag, startTimes, endTimes)
        } catch (e: Exception) {
            e.message?.let { error ->
                throw InternalServiceException(error)
            } ?: run {
                throw InternalServiceException("Internal Service Error")
            }
        }

    }
}