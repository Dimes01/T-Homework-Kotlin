package org.example

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.*
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.LinkedList
import kotlin.math.*

/**
 * Задание №1 - Подготовка DTO для работы с входными / выходными данными.
 *
 * Создайте DTO для работы с новостями. Он должен содержать следующие поля:
 * - id - идентификатор новости
 * - title - заголовок новости
 * - place - место, где произошло событие
 * - description - описание новости
 * - siteUrl - ссылка на страницу новости на сайте KudaGo
 * - favoritesCount - число пользователей, добавивших новость в избранное
 * - commentsCount - число комментариев
 */
@Serializable
data class News(
    val id: Int,
    val title: String,
    @Serializable(with = PlaceAsStringSerializer::class) val place: String,
    val description: String,
    @SerialName("site_url") val siteUrl: String,
    @SerialName("favorites_count") val favoritesCount: Int,
    @SerialName("comments_count") val commentsCount: Int,
    @SerialName("publication_date") val publicationDate: Long,
) {
    var rating: Double = 0.0

    fun calculateRating() {
        rating = 1 / (1 + exp(-(favoritesCount.toDouble() / (commentsCount + 1))))
    }
}

private val json = Json { ignoreUnknownKeys = true }

object PlaceAsStringSerializer : KSerializer<String> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("News", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): String {
        return when (val jsonElement = (decoder as JsonDecoder).decodeJsonElement()) {
            is JsonObject -> jsonElement["id"].toString()
            else -> "null"
        }
    }

    override fun serialize(encoder: Encoder, value: String) {
        encoder.encodeString(value)
    }

}

// client вынесен сюда для упрощения тестирования
var client: HttpClient = HttpClient(CIO) {
    install(Logging) {
        logger = Logger.DEFAULT
        filter { request -> request.url.host.contains("ktor.io") }
    }
}

/**
 * Задание #2 - Получение новостей из API
 *
 * Создайте функцию, которая будет получать список новостей из API KudaGo. Новости должны быть отсортированы по дате их публикации,
 * а их кол-во должно быть аргументом вашей функции и иметь значение по умолчанию, равное 100.

 * Интересующий вас эндпойнт описан здесь - https://docs.kudago.com/api/#page:новости,header:новости-список-новостей.
 *
 * Вам необходимо отправить на него запрос, используя любой http-клиент (я рекомендую использовать ktor-client - https://ktor.io/docs/client-requests.html
 * и kotlinx.serialization для десериализации), и вернуть список новостей.
 *
 * Обратите внимание на следующие параметры:
 * - [page], [page_size] и [order_by] - по заданию вам необходимо получить первые N новостей, отсортированных по дате публикации.
 * - [location] - в качестве значения вам необходимо выбрать ваш регион, или [spb], если вашего региона нет в списке.
 */

suspend fun getNews(count: Int = 100): List<News> {
    val maxPageSize = if (count in 1..100) count else 100
    val pages = ceil(count / maxPageSize.toDouble()).toInt()
    val list = LinkedList<News>()
    var index = 1
    while (index <= pages) {
        val temp = client.use {
            try {
                val response: HttpResponse = it.get("https://kudago.com/public-api/v1.4/news/") {
                    url {
                        parameters.append("page", index.toString())
                        parameters.append("page_size", maxPageSize.toString())
                        parameters.append("location", "spb")
                        parameters.append(
                            "fields",
                            "id,title,place,description,site_url,favorites_count,comments_count,publication_date"
                        )
                    }
                }
                val bodyString: String = response.body()
                val jsonElement = json.parseToJsonElement(bodyString)
                val newsList = jsonElement.jsonObject["results"]?.jsonArray
                newsList?.map { news -> json.decodeFromJsonElement<News>(news) } ?: emptyList()
            } catch (e: Exception) {
                println("Error fetching news: ${e.message}")
                emptyList()
            }
        }
        list.addAll(temp)
        ++index
    }
    return list
}


/**
 * Задание #3 - Фильтрация и обработка новостей
 *
 * Напишите функцию getMostRatedNews, которая получает все новости за указанный промежуток времени,
 * после чего сортирует их по возрастанию по рейтингу.
 *
 * Рейтинг рассчитывается согласно следующей функции:
 * `1 / (1 + math.exp(-(favoritesCount / (commentsCount + 1))`
 *
 * Данное значение должно храниться в [News] в виде отдельного поля [rating].
 * При этом значение данного поля должно высчитываться только один раз именно в тот момент, когда оно будет необходимо.
 *
 * Результатом работы данной функции должен быть список, состоящий максимум из [count] новостей
 * с наибольшим рейтингом за указанный [period].
 *
 * Note: попробуйте выполнить данное задание с помощью списков и циклов, и с помощью последовательностей.
 */
suspend fun List<News>.getMostRatedNews(count: Int, period: ClosedRange<LocalDate>): List<News> {
    val list: MutableList<News> = LinkedList()
    var isEnd = false
    var lastCount = count
    while (!isEnd) {
        val filtered = getNews().filter {
            val instant = Instant.ofEpochSecond(it.publicationDate)
            val localDate = instant.atOffset(ZoneOffset.ofHours(3)).toLocalDate()
            localDate in period
        }
        if (filtered.isNotEmpty()) {
            list.addAll(filtered.take(lastCount))
            lastCount -= filtered.size
        }
        isEnd = lastCount <= 0
    }

    for (news in list) news.calculateRating()
    list.sortByDescending { it.rating }
    return list
}

/**
 * Задание #4 - Сохранение новостей в CSV-файл
 *
 * Напишите функцию saveNews, которая сохранить новости в файл по указанному пути. Не забудьте проверить, что путь - валиден и по нему нет уже существующих файлов.
 */
fun saveNews(path: String, news: Collection<News>) {
    val file = File(path)

    if (!file.parentFile.exists()) {
        throw IOException("Parent directory does not exist: ${file.parentFile.absolutePath}")
    }
    if (file.exists()) {
        throw IOException("File already exists: ${file.absolutePath}")
    }

    FileWriter(file).use { writer ->
        writer.write("id,title,place,description,site_url,favorites_count,comments_count,publicationDate\n")
        for (elem in news) {
            var str = "${elem.id},"
            str += "${escapeCsv(elem.title)},"
            str += "${escapeCsv(elem.place)},"
            str += "${escapeCsv(elem.description)},"
            str += "${escapeCsv(elem.siteUrl)},"
            str += "${elem.favoritesCount},"
            str += "${elem.commentsCount},"
            str += "${elem.publicationDate}\n"
            writer.write(str)
        }
    }
}

private fun escapeCsv(value: String?): String {
    if (value.isNullOrEmpty()) return ""
    return "\"${value.replace("\"", "\"\"")}\"" // Экранирование кавычек
}

/**
 * Задание со * - Pretty Print
 *
 * Напишите свой собственный DSL на Kotlin, с помощью которого можно было бы красиво печатать новости в консоль / файл.
 * В качестве примера можете использовать пример, представленный в документации - https://kotlinlang.org/docs/type-safe-builders.html.
 */
suspend fun main() {
//    val list = getNews(5)

//    val timeRange: ClosedRange<LocalDate> = LocalDate.parse("2024-09-15")..LocalDate.parse("2024-09-17")
//    val list: List<News> = LinkedList<News>().getMostRatedNews(5, timeRange)
//
//    for (news in list) {
//        println(news.toString())
//    }

    val list = getNews(5)
    saveNews("src/main/resources/top5news.csv", list)

//    readme {
//        header(level = 1) { +"Kotlin Lecture" }
//        header(level = 2) { +"DSL" }
//
//        text {
//            +("Today we will try to recreate ${bold("DSL")} from this article: ${link(link = "https://kotlinlang.org/docs/type-safe-builders.html", text = "Kotlin Docs")}!!!")
//            +"It is so ${underlined("fascinating and interesting")}!"
//            +code(language = ProgrammingLanguage.KOTLIN) {
//                +"""
//                    fun main() {
//                        println("Hello world!")
//                    }
//                """.trimIndent()
//            }
//        }
//    }
}