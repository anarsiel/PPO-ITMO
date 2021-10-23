package hashtagFrequencyService.validator

interface Validator {
    fun validateCalcFrequency(tag: String, hours: Int)
}