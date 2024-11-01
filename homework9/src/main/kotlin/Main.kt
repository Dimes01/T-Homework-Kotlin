import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.actor
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import org.slf4j.LoggerFactory
import java.io.File
import java.lang.Thread.sleep
import kotlin.time.measureTime


val logger: org.slf4j.Logger = LoggerFactory.getLogger("Main")
val json = Json { ignoreUnknownKeys = true }
var client: HttpClient = HttpClient(CIO) {
    install(Logging) {
        logger = Logger.DEFAULT
        filter { request -> request.url.host.contains("ktor.io") }
    }
}

suspend fun getNews(page: Int, pageSize: Int): List<News> {
    logger.info("Method `getNews`: started")
    logger.debug("Method `getNews`: page=$page, pageSize=$pageSize")
    val response = client.get {
        url("https://kudago.com/public-api/v1.4/news/")
        parameter("page", page)
        parameter("page_size", pageSize)
        parameter("location", "spb")
        parameter("fields", "id,title,place,description,site_url,favorites_count,comments_count,publication_date")
    }
    val bodyString = response.body<String>()
    logger.debug("Method `getNews`: responseBody=$bodyString")
    val jsonElement = json.parseToJsonElement(bodyString)
    val newsList = jsonElement.jsonObject["results"]?.jsonArray
    logger.info("Method `getNews`: finished")
    return newsList?.map { news -> json.decodeFromJsonElement<News>(news) } ?: emptyList()
}

sealed class ProcessorMessage {
    data class NewsData(val news: List<News>) : ProcessorMessage()
    data object Done : ProcessorMessage()
}

fun CoroutineScope.processorActor(outputFile: File) = actor<ProcessorMessage>(Dispatchers.IO) {
    logger.info("Block in method `processorActor`: started")
    outputFile.bufferedWriter().use { writer ->
        for (msg in channel) {
            when (msg) {
                is ProcessorMessage.NewsData -> {
                    for (news in msg.news) {
                        writer.write(news.toString())
                        writer.newLine()
                    }
                    logger.debug(
                        "Block in method `processorActor`: retrieve processor messages `NewsData` - {}",
                        msg.news
                    )
                }
                is ProcessorMessage.Done -> {
                    logger.debug("Block in method `processorActor`: retrieve processor message `Done`")
                    break
                }
            }
        }
    }
    logger.info("Block in method `processorActor`: finished")
}

fun CoroutineScope.newsFetcher(channel: SendChannel<ProcessorMessage>, pageSize: Int, startPage: Int, step: Int, maxCountNews: Int) = launch {
    logger.info("Method `newsFetcher`: started")
    var page = startPage
    var remainingCountNews = maxCountNews
    val actualPageSize = if (pageSize > maxCountNews) maxCountNews else pageSize
    while (remainingCountNews > 0) {
        val news = getNews(page, actualPageSize)
        if (news.isEmpty()) break
        channel.send(ProcessorMessage.NewsData(news))
        page += step
        remainingCountNews -= actualPageSize
    }
    channel.send(ProcessorMessage.Done)
    logger.info("Method `newsFetcher`: finished")
}

suspend fun main() {
    val outputFile = File("news.txt")
    val config: Config = ConfigFactory.load().getConfig("news-fetcher")
    val pageSize = config.getInt("page-size")
    val maxCountNews = config.getInt("max-count-news")
//    val threadCount = config.getInt("threads")
    var threadCount = 5
    while (threadCount in 5..25) {
        val countTests = 5
        var times = 0L
        var res = ""
        var tempCountTests = countTests
        val coroutineMaxCountNews: Int = maxCountNews / threadCount
        while (tempCountTests > 0) {
            val time = measureTime {
                runBlocking {
                    val processor = processorActor(outputFile)

                    withContext(newFixedThreadPoolContext(threadCount, "newsFetchPool")) {
                        repeat(threadCount) {
                            newsFetcher(processor, pageSize, it + 1, threadCount, coroutineMaxCountNews)
                        }
                    }

                    processor.close()
                }
            }
            sleep(1000)
            tempCountTests -= 1
            times += time.inWholeMilliseconds
            res += " ${time.inWholeMilliseconds} |"
            logger.info("Test ${countTests - tempCountTests} is finished.")
        }
        println("| $threadCount | $res ${times / countTests} |")
        threadCount += 5
        sleep(2000)
    }
    client.close()
}