package gcu.production.stavlenta.repository.rest.core

import io.ktor.client.engine.*
import io.ktor.client.engine.okhttp.*

actual class HttpEngineFactory actual constructor() {
    actual fun createEngine(): HttpClientEngineFactory<HttpClientEngineConfig> = OkHttp
}