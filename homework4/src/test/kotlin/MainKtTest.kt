import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.logging.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import org.example.News
import org.example.getMostRatedNews
import org.example.getNews
import org.example.saveNews
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.assertThrows
import java.io.File
import java.io.IOException
import java.time.LocalDate
import java.util.*

class MainKtTest {
    private val news1String = """{
                                "id": 1,
                                "title": "News Title 1",
                                "place": {
                                    "id": 1
                                },
                                "description": "Description 1",
                                "site_url": "https://example.com/1",
                                "favorites_count": 10,
                                "comments_count": 2,
                                "publication_date": 1726496880
                            }"""
    private val news1 = createNews(1, "News Title 1", "1", "Description 1", "https://example.com/1", 10, 2, 1726496880)
    private val news2String = """{
                                "id": 2,
                                "title": "News Title 2",
                                "place": null,
                                "description": "Description 2",
                                "site_url": "https://example.com/2",
                                "favorites_count": 5,
                                "comments_count": 1,
                                "publication_date": 1726496880
                            }"""
    private val news2 = createNews(2, "News Title 2", "null", "Description 2", "https://example.com/2", 5, 1, 1726496880)

    private fun createMockEngine(responseContent: String) = MockEngine {
        respond(
            content = responseContent,
            status = HttpStatusCode.OK,
            headers = headersOf(HttpHeaders.ContentType, "application/json")
        )
    }

    private fun createHttpClient(mockEngine: MockEngine) = HttpClient(mockEngine) {
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.INFO
        }
    }

    private fun createNews(id: Int, title: String, place: String, description: String, siteUrl: String, favoritesCount: Int, commentsCount: Int, publicationDate: Long) = News(
        id = id,
        title = title,
        place = place,
        description = description,
        siteUrl = siteUrl,
        favoritesCount = favoritesCount,
        commentsCount = commentsCount,
        publicationDate = publicationDate
    )

    @Test
    fun `get news with place`() {
        val mockEngine = createMockEngine("""{
                        "count": 1,
                        "next": null,
                        "previous": null,
                        "results": [$news1String]
                    }""")

        org.example.client = createHttpClient(mockEngine)

        runBlocking {
            val news = getNews(1)
            val expectedNewsList = listOf(news1)
            assertEquals(expectedNewsList, news)
        }
    }

    @Test
    fun `get news without place`() {
        val mockEngine = createMockEngine("""{
                        "count": 1,
                        "next": null,
                        "previous": null,
                        "results": [$news2String]
                    }""")

        org.example.client = createHttpClient(mockEngine)

        runBlocking {
            val news = getNews(1)
            val expectedNewsList = listOf(news2)
            assertEquals(expectedNewsList, news)
        }
    }

    @Test
    fun `get several news`() {
        val mockEngine = createMockEngine("""{
                        "count": 2,
                        "next": null,
                        "previous": null,
                        "results": [$news1String,$news2String]
                    }""",)

        org.example.client = createHttpClient(mockEngine)

        runBlocking {
            val news = getNews(2)
            val expectedNewsList = listOf(news1, news2)
            assertEquals(expectedNewsList, news)
        }
    }

    @Test
    fun `get most rated news successful`() {
        val mockEngine = createMockEngine("""{
                        "count": 2,
                        "next": null,
                        "previous": null,
                        "results": [$news1String,$news2String]
                    }""")

        org.example.client = createHttpClient(mockEngine)

        runBlocking {
            val timeRange: ClosedRange<LocalDate> = LocalDate.parse("2024-09-15")..LocalDate.parse("2024-09-17")
            val list: List<News> = LinkedList<News>().getMostRatedNews(2, timeRange)
            val expected: List<News> = listOf(news1, news2)
            assertEquals(expected, list)
        }
    }

    @Test
    fun `get most rated news reversed period`() {
        val mockEngine = createMockEngine("""{
                        "count": 2,
                        "next": null,
                        "previous": null,
                        "results": [$news1String,$news2String]
                    }""")

        org.example.client = createHttpClient(mockEngine)

        runBlocking {
            val timeRange: ClosedRange<LocalDate> = LocalDate.parse("2024-09-17")..LocalDate.parse("2024-09-15")
            val list: List<News> = LinkedList<News>().getMostRatedNews(2, timeRange)
            val expected: List<News> = emptyList()
            assertEquals(expected, list)
        }
    }

    @Test
    fun `save news successful`() {
        val list = listOf(news1, news2)
        val file = File.createTempFile("test", ".csv")
        try {
            if (file.exists()) {
                file.delete()
            }
            saveNews(file.path, list)
            val expected = "id,title,place,description,site_url,favorites_count,comments_count,publicationDate\n" +
                    "1,\"News Title 1\",\"1\",\"Description 1\",\"https://example.com/1\",10,2,1726496880\n" +
                    "2,\"News Title 2\",\"null\",\"Description 2\",\"https://example.com/2\",5,1,1726496880\n"
            val real = file.readText()
            assertEquals(expected, real)
        } finally {
            file.delete()
        }
    }

    @Test
    fun `save news in existed file`() {
        val list = listOf(news1, news2)
        assertThrows<IOException>{ saveNews("homework4/src/test/resources/existedFile.csv", list) }
    }
}