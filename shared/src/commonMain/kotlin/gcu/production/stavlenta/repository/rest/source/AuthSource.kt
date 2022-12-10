package gcu.production.stavlenta.repository.rest.source

import gcu.production.stavlenta.repository.model.UserModel
import io.ktor.client.statement.*

interface AuthSource {

    suspend fun login(login: String, password: String): HttpResponse

    suspend fun registration(body: UserModel): HttpResponse
}