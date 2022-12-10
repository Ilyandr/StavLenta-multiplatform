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
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
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


val clientModule = DI.Module(name = KTOR_MODULE, init = {

    bind<HttpEngineFactory>() with singleton { HttpEngineFactory() }

    bind<HttpClient>(tag = DEFAULT_CLIENT) with singleton {

        HttpClient(instance<HttpEngineFactory>().createEngine()) {
            this requireBaseHttpConfig API_HOST
        }
    }

    bind<HttpClient>(tag = AUTH_CLIENT) with singleton {

        HttpClient(instance<HttpEngineFactory>().createEngine()) {

            this requireBaseHttpConfig API_HOST

            install(Auth) {
                basic {
                    credentials {
                        BasicAuthCredentials(
                            username = CommonSDK.storageSource.getString(LOGIN_KEY) ?: "",
                            password = CommonSDK.storageSource.getString(PASSWORD_KEY) ?: ""
                        )
                    }
                    realm = "Access to the '/' path"
                }
            }
        }
    }
})

private infix fun HttpClientConfig<HttpClientEngineConfig>.requireBaseHttpConfig(host: String) =
    apply {
        install(Logging) {
            level = LogLevel.ALL
            logger = Logger.SIMPLE
        }
        install(WebSockets) {
            contentConverter = KotlinxWebsocketSerializationConverter(Json)
        }
        defaultRequest {
            this.host = host
            url { protocol = URLProtocol.HTTP }
            headers { set("Content-Type", "application/json") }
        }
    }
