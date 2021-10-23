package hashtagFrequencyService.apiClients

interface ApiClient {
    fun countPostsByTime(tag: String, startTimes: Array<Long>, endTimes: Array<Long>) :  Array<Int>
}