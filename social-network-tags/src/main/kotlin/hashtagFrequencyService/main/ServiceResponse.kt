package hashtagFrequencyService.main


interface Response

data class SuccessfulResponse(
    val frequency: Array<Int>,
    val error: String? = null,
) : Response {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SuccessfulResponse

        if (!frequency.contentEquals(other.frequency)) return false
        if (error != other.error) return false

        return true
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}

data class UnsuccessfulResponse(
    val statistics: Array<Int>? = null,
    val error: String,
) : Response {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UnsuccessfulResponse

        if (statistics != null) {
            if (other.statistics == null) return false
            if (!statistics.contentEquals(other.statistics)) return false
        } else if (other.statistics != null) return false
        if (error != other.error) return false

        return true
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}