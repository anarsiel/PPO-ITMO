package hashtagFrequencyService.apiClients

import hashtagFrequencyService.core.RemoteClientException
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL

class VkApiClient(
    private val newsFeedPrefix: String = "https://api.vk.com/method/newsfeed.search",
    private val accessToken: String = "73edf9f073edf9f073edf9f0f673943ada773ed73edf9f01282ca90c9acbef0d19a8357",
    private val apiVersion: String = "5.131",
    private val defaultPostsCount: String = "200",
) : ApiClient {
    private fun sendRequest(url: String): JSONObject {
        val response = JSONObject(URL(url).readText())

        if (response.has("error")) {
            val errorMessage = response.getJSONObject("error").get("error_msg").toString()
            throw RemoteClientException(errorMessage)
        }

        return response
    }

    private fun getPostsForLastDay(tag: String, count: String = defaultPostsCount): JSONObject {
        val url = "$newsFeedPrefix?" +
                "access_token=$accessToken&" +
                "v=$apiVersion&" +
                "q=$tag&" +
                "count=$count"
        return sendRequest(url)
    }

    private fun countPostsInTimeRange(posts: JSONArray, startTime: Long, endTime: Long): Int = posts.filter { post ->
        val postDate = JSONObject(post.toString())["date"].toString().toLong()
        postDate in startTime..endTime
    }.size

    override fun countPostsByTime(tag: String, startTimes: Array<Long>, endTimes: Array<Long>): Array<Int> {
        val response = getPostsForLastDay(tag)
        val posts = response.getJSONObject("response").getJSONArray("items")
        val postsCountByTime = Array(startTimes.size) { i ->
            countPostsInTimeRange(posts, startTimes[i], endTimes[i])
        }
        return postsCountByTime
    }
}