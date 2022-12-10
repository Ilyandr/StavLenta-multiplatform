package gcu.production.stavlenta.repository.rest.repository

import gcu.production.stavlenta.repository.model.UserModel
import gcu.production.stavlenta.repository.rest.source.AuthSource

class AuthRepository(private val source: AuthSource) {

    suspend fun login(login: String, password: String) = source.login(login, password)

    suspend fun registration(body: UserModel) = source.registration(body)
}