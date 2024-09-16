import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.logging.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import org.example.News
import org.example.getNews
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach

class MainKtTest {
    @Test
    fun `successful get news`() {
        val mockEngine = MockEngine {
            respond(
                content = """{
                        "count": 2,
                        "next": null,
                        "previous": null,
                        "results": [
                            {
                                "id": 1,
                                "title": "News Title 1",
                                "place": "Place 1",
                                "description": "Description 1",
                                "site_url": "https://example.com/1",
                                "favorites_count": 10,
                                "comments_count": 2,
                                "publication_date": 1726496880
                            },
                            {
                                "id": 2,
                                "title": "News Title 2",
                                "place": "Place 2",
                                "description": "Description 2",
                                "site_url": "https://example.com/2",
                                "favorites_count": 5,
                                "comments_count": 1,
                                "publication_date": 1726496880
                            }
                        ]
                    }""",
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        org.example.client = HttpClient(mockEngine) {
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.INFO
            }
        }

        runBlocking {
            val news = getNews(2)
            val expectedNewsList = listOf(
                News(
                    id = 1,
                    title = "News Title 1",
                    place = "Place 1",
                    description = "Description 1",
                    siteUrl = "https://example.com/1",
                    favoritesCount = 10,
                    commentsCount = 2,
                    publicationDate = 1726496880
                ),
                News(
                    id = 1,
                    title = "News Title 2",
                    place = "Place 2",
                    description = "Description 2",
                    siteUrl = "https://example.com/2",
                    favoritesCount = 5,
                    commentsCount = 1,
                    publicationDate = 1726496880
                )
            )
            assertEquals(expectedNewsList, news)
        }
    }

    @Test
    fun getMostRatedNews() {

    }

    @Test
    fun saveNews() {

    }
}