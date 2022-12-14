package gcu.production.stavlenta.repository.rest.data

import gcu.production.stavlenta.repository.feature.other.convertToBase64
import gcu.production.stavlenta.repository.model.UserModel
import gcu.production.stavlenta.repository.rest.source.AuthSource
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*

// Реализация api для авторизации клиента. Инициализация через di.
class AuthDataSource(private val client: HttpClient) : AuthSource {

   /* suspend - объявление приостанавливаемой ф-кции, может быть вызвана из:
    Coroutine(...) - android
    Task(...) - ios */
    override suspend fun login(login: String, password: String) =
        client.get { // Объявление типа запроса.
            url { // Параметры запроса.
                path("/api/login") // Endpoint запроса.
                header("Authorization", convertToBase64(login, password)) // Заголовок запроса в виде auth header.
            }
        }.status // Вовзращаемое значение ф-кции.

    override suspend fun registration(body: UserModel) =
        client.post {
            url { path("/api/registration") }
            setBody(body)
        }.status
}