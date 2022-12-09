package gcu.production.stavlenta.repository.rest.source

import io.ktor.client.statement.*

interface AuthSource {

    suspend fun login(login: String, password: String): HttpResponse
}