package org.example

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import java.time.LocalDate

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
data class News(
    val id: Int,
    val title: String,
    val place: String,
    val description: String,
    val siteUrl: String,
    val favoritesCount: Int,
    val commentsCount: Int
)

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
    val client: HttpClient = HttpClient(CIO) {
        install(Logging) {
            logger = Logger.DEFAULT
            filter { request -> request.url.host.contains("ktor.io") }
        }
    }

    return client.use {
        try {
            val response: HttpResponse = it.get("https://kudago.com/public-api/v1.4/news/") {
                url {
                    parameters.append("page", "1")
                    parameters.append("page_size", count.toString())
                    parameters.append("location", "spb")
                }
            }
            response.body<List<News>>()
        } catch (e: Exception) {
            println("Error fetching news: ${e.message}")
            emptyList()
        }
    }
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
fun List<News>.getMostRatedNews(count: Int, period: ClosedRange<LocalDate>): List<News> {
    TODO()
}

/**
 * Задание #4 - Сохранение новостей в CSV-файл
 *
 * Напишите функцию saveNews, которая сохранить новости в файл по указанному пути. Не забудьте проверить, что путь - валиден и по нему нет уже существующих файлов.
 */
fun saveNews(path: String, news: Collection<News>) {
    TODO()
}

/**
 * Задание со * - Pretty Print
 *
 * Напишите свой собственный DSL на Kotlin, с помощью которого можно было бы красиво печатать новости в консоль / файл.
 * В качестве примера можете использовать пример, представленный в документации - https://kotlinlang.org/docs/type-safe-builders.html.
 */
fun main() {
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