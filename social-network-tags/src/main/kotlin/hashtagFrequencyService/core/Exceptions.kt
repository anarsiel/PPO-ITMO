package hashtagFrequencyService.core

open class ValidationException(string: String) : Exception(string)

open class InternalServiceException(string: String) : Exception(string)

open class RemoteClientException(string: String) : Exception(string)
