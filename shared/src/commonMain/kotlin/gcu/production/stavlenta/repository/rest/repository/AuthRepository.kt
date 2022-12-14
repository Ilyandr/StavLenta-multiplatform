package gcu.production.stavlenta.repository.rest.repository

import gcu.production.stavlenta.repository.model.UserModel
import gcu.production.stavlenta.repository.rest.source.AuthSource
import kotlinx.coroutines.*
import kotlin.coroutines.coroutineContext

// Общедоступный репозиторий для взаимодействия с api (инициализация с помощью di).
class AuthRepository(private val source: AuthSource) {

    // По умолчанию вызов ф-кции происходит не в главном потоке (by kotlin coroutines)
    suspend fun login(login: String, password: String) = withContext(Dispatchers.Default) {
        source.login(login, password)
    }

    suspend fun registration(body: UserModel) = withContext(Dispatchers.Default) {
        source.registration(body)
    }
}