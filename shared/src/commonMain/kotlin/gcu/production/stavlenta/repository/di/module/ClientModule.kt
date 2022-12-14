package gcu.production.stavlenta.repository.di.module

import gcu.production.stavlenta.repository.di.CommonSDK
import gcu.production.stavlenta.repository.feature.other.*
import gcu.production.stavlenta.repository.feature.other.API_HOST
import gcu.production.stavlenta.repository.feature.other.AUTH_CLIENT
import gcu.production.stavlenta.repository.feature.other.DEFAULT_CLIENT
import gcu.production.stavlenta.repository.feature.other.KTOR_MODULE
import gcu.production.stavlenta.repository.rest.core.HttpEngineFactory
import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.*
import kotlinx.serialization.json.Json
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

// Реализация модуля di. (clientDi - клиент-серверное взаимодействие).
val clientModule = DI.Module(name = KTOR_MODULE, init = {

    bind<HttpEngineFactory>() with singleton { HttpEngineFactory() } // Инициализация данных (by Codein di).

    bind<HttpClient>(tag = DEFAULT_CLIENT) with singleton {

        HttpClient(instance<HttpEngineFactory>().createEngine()) {
            this requireBaseHttpConfig API_HOST
        }
    }

    bind<HttpClient>(tag = AUTH_CLIENT) with singleton {

        HttpClient(instance<HttpEngineFactory>().createEngine()) {

            this requireBaseHttpConfig API_HOST

            defaultRequest {
                headers {
                    set(
                        "Authorization",
                        convertToBase64(
                            CommonSDK.storageSource.getString(LOGIN_KEY),
                            CommonSDK.storageSource.getString(PASSWORD_KEY)
                        )
                    )
                }
            }
        }
    }
})

// Общая логика для создание clint. Используется паттерн builder by kotlin. (внутри apply).
// infix - синтаксический сахар при вызове.
// Является ф-кцией расширением, эквивалетно: private HttpClientConfig<HttpClientEngineConfig> requireBaseHttpConfig(HttpClientConfig<HttpClientEngineConfig> value1, String host)
private infix fun HttpClientConfig<HttpClientEngineConfig>.requireBaseHttpConfig(host: String) =
    // apply - инициализция переменной с введением собственной области видимости вместо переменной. (см. this внутри).
    apply {
        // Параметры для вывода информации о запросах в логи.
        install(Logging) {
            level = LogLevel.ALL
            logger = Logger.SIMPLE
        }
        // Параметры для обработки данных (current - json format).
        install(WebSockets) {
            contentConverter = KotlinxWebsocketSerializationConverter(Json)
        }

        // Параметры по умолчанию.
        defaultRequest {
            this.host = host // base url сервера.
            url { protocol = URLProtocol.HTTP }
            headers { set("Content-Type", "application/json") }
        }
    }
