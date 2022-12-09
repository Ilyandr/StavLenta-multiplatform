package gcu.production.stavlenta.repository.di.module

import gcu.production.stavlenta.repository.feature.SERIALIZATION_MODULE
import kotlinx.serialization.json.Json
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton

val serializationModule = DI.Module(name = SERIALIZATION_MODULE, init = {
    bind<Json>() with singleton {
        Json {
            isLenient = true
            ignoreUnknownKeys = true
        }
    }
})