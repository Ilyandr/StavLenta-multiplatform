package gcu.production.stavlenta.repository.di.module

import gcu.production.stavlenta.repository.feature.API_HOST
import gcu.production.stavlenta.repository.feature.DEFAULT_CLIENT
import gcu.production.stavlenta.repository.feature.KTOR_MODULE
import gcu.production.stavlenta.repository.rest.core.HttpEngineFactory
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton


val ktorModule = DI.Module(name = KTOR_MODULE, init = {

    bind<HttpEngineFactory>() with singleton { HttpEngineFactory() }

    bind<HttpClient>(tag = DEFAULT_CLIENT) with singleton {
        HttpClient(instance<HttpEngineFactory>().createEngine()) {
            install(Logging) {
                level = LogLevel.ALL
                logger = Logger.SIMPLE
            }
            defaultRequest {
                host = API_HOST
                url { protocol = URLProtocol.HTTP }
                headers { set("Content-Type", "application/json") }
            }
        }
    }
})
