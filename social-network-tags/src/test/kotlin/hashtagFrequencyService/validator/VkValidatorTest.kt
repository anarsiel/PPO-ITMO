package hashtagFrequencyService.validator

import hashtagFrequencyService.core.ValidationException
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import kotlin.test.assertFailsWith

class VkValidatorTest {
    private val validator = VkValidator()

    @Test
    fun ` successful validation `() {
        assertThat(validator.validateCalcFrequency("some tag", 1))
    }

    @Test
    fun ` tag doesn't match regex `() {
        val exception = assertFailsWith<ValidationException> {
            validator.validateCalcFrequency("tag%", 1)
        }

        assertThat("`tag`: tag% doesn't match regex [а-яА-Яa-zA-Z0-9_# ]+").isEqualTo(exception.message)
    }

    @Test
    fun ` tag is too long `() {
        val exception = assertFailsWith<ValidationException> {
            validator.validateCalcFrequency(
                "toooooooooooo     Loooooooooooooong Taaaaaaaaaag        ",
                1
            )
        }

        assertThat("`tag` must be not longer than 50 characters").isEqualTo(exception.message)
    }

    @Test
    fun ` hours value is too big `() {
        val exception = assertFailsWith<ValidationException> {
            validator.validateCalcFrequency("tag", 25)
        }

        assertThat("`hours` value must be in range [1 .. 24]").isEqualTo(exception.message)
    }

    @Test
    fun ` hours value is too small `() {
        val exception = assertFailsWith<ValidationException> {
            validator.validateCalcFrequency("tag", 0)
        }

        assertThat("`hours` value must be in range [1 .. 24]").isEqualTo(exception.message)
    }
}