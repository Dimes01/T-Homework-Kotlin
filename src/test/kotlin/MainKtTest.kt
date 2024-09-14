import io.ktor.client.*
import io.ktor.client.engine.cio.*
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mock

class MainKtTest {
    @Mock
    lateinit var client: HttpClient

    @BeforeEach
    fun setUp() {
        client = HttpClient(CIO)
    }

    @Test
    fun getNews() {

    }

    @Test
    fun getMostRatedNews() {

    }

    @Test
    fun saveNews() {

    }
}