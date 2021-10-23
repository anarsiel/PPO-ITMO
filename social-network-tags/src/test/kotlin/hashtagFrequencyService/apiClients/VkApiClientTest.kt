package hashtagFrequencyService.apiClients

import com.xebialabs.restito.builder.stub.StubHttp.whenHttp
import com.xebialabs.restito.builder.verify.VerifyHttp.verifyHttp
import com.xebialabs.restito.semantics.Action.stringContent
import com.xebialabs.restito.semantics.Condition.*
import com.xebialabs.restito.server.StubServer
import hashtagFrequencyService.core.InternalServiceException
import hashtagFrequencyService.core.RemoteClientException
import org.assertj.core.api.Assertions.assertThat
import org.glassfish.grizzly.http.Method
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFailsWith

class VkApiClientTest {
    private val port = 30000
    private var server: StubServer? = null
    private val oneHourInSeconds = 60 * 60L
    private val endpoint = "news.prefix"

    private val vkApiClient = VkApiClient(
            newsFeedPrefix = "http://localhost:$port/$endpoint"
    )

    private val fakeCorrectResponse = """
        {
            "response": {
                "items": [
                    {
                        "date": 1634927114,
                    }   
                ]    
            }
        }
    """.trimIndent()

    private val fakeIncorrectResponse = """
        {
            "error": {
                "error_msg": "msg"
            }
        }
    """.trimIndent()

    private fun generateStartTimes(hours: Int, currentTime: Long): Array<Long> {
        return Array(hours) { i ->
            currentTime - (hours - i) * oneHourInSeconds
        }
    }

    private fun generateEndTimes(startTimes: Array<Long>): Array<Long> {
        return Array(startTimes.size) { i ->
            startTimes[i] + oneHourInSeconds
        }
    }

    @Before
    fun start() {
        server = StubServer(port).run()
    }

    @After
    fun stop() {
        server?.stop()
    }


    @Test
    fun ` correct response `() {
        whenHttp(server)
            .match(
                method(Method.GET),
                startsWithUri("/$endpoint")
            )
            .then(
                stringContent(fakeCorrectResponse)
            )

        val expectedAnswer = arrayOf(0, 0, 0, 0, 1)

        val currentTime = 1634927120L
        val hours = 5
        val startTimes = generateStartTimes(hours, currentTime)
        val endTimes = generateEndTimes(startTimes)

        assertThat(vkApiClient.countPostsByTime("tag", startTimes, endTimes))
            .isEqualTo(expectedAnswer)

        verifyHttp(server).once(
            method(Method.GET),
            uri("/$endpoint")
        )
    }

    @Test
    fun ` error response `() {
        whenHttp(server)
            .match(
                method(Method.GET),
                startsWithUri("/$endpoint")
            )
            .then(
                stringContent(fakeIncorrectResponse)
            )

        val currentTime = 1634927120L
        val hours = 1
        val startTimes = generateStartTimes(hours, currentTime)
        val endTimes = generateEndTimes(startTimes)

        val exception = assertFailsWith<RemoteClientException> {
            vkApiClient.countPostsByTime("tag", startTimes, endTimes)
        }
        assertThat("msg").isEqualTo(exception.message)

        verifyHttp(server).once(
            method(Method.GET),
            uri("/$endpoint")
        )
    }
}
