package gcu.production.stavlenta.repository.rest.data

import gcu.production.stavlenta.repository.feature.AUTH_HEADER_KEY
import gcu.production.stavlenta.repository.feature.requireAuthToken
import gcu.production.stavlenta.repository.rest.source.AuthSource
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*


class AuthDataSource(private val client: HttpClient) : AuthSource {

    override suspend fun login(login: String, password: String) =
        client.get {
            url {
                path("/api/login")
                header(AUTH_HEADER_KEY, Pair(login, password).requireAuthToken())
            }
        }
}