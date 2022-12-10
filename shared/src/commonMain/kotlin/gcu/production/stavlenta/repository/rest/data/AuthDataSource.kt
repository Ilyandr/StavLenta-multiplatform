package gcu.production.stavlenta.repository.rest.data

import gcu.production.stavlenta.repository.feature.other.convertToBase64
import gcu.production.stavlenta.repository.model.UserModel
import gcu.production.stavlenta.repository.rest.source.AuthSource
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*


class AuthDataSource(private val client: HttpClient) : AuthSource {

    override suspend fun login(login: String, password: String) =
        client.get {
            url {
                path("/api/login")
                header("Authorization", convertToBase64(login, password))
            }
        }

    override suspend fun registration(body: UserModel) =
        client.post {
            url { path("/api/registration") }
            setBody(body)
        }
}