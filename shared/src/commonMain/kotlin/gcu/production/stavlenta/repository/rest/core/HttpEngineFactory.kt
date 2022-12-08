package gcu.production.stavlenta.repository.rest.core

import io.ktor.client.engine.*


expect class HttpEngineFactory constructor() {
    fun createEngine(): HttpClientEngineFactory<HttpClientEngineConfig>
}