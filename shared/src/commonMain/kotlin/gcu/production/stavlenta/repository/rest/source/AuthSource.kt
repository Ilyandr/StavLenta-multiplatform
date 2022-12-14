package gcu.production.stavlenta.repository.rest.source

import gcu.production.stavlenta.repository.model.UserModel
import io.ktor.http.*

// Интерфейс для DataSource реализации.
interface AuthSource {

    suspend fun login(login: String, password: String): HttpStatusCode

    suspend fun registration(body: UserModel): HttpStatusCode
}