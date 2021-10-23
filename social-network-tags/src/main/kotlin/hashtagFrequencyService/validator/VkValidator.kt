package hashtagFrequencyService.validator

import hashtagFrequencyService.core.ValidationException

open class VkValidator : Validator {
    private val tagRegex = Regex("[а-яА-Яa-zA-Z0-9_# ]+")

    override fun validateCalcFrequency(tag: String, hours: Int) {
        if (!tagRegex.matches(tag))
            throw ValidationException("`tag`: $tag doesn't match regex $tagRegex")

        if (tag.length >= 50)
            throw ValidationException("`tag` must be not longer than 50 characters")

        if (hours <= 0 || hours >= 25)
            throw ValidationException("`hours` value must be in range [1 .. 24]")
    }
}