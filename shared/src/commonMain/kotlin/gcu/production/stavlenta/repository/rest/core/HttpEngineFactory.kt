package gcu.production.stavlenta.repository.rest.core

import io.ktor.client.engine.*


// Объявление основы для сетевых запросов (модифиактор expect - собственная реализация под каждую платформу)
expect class HttpEngineFactory constructor() {
    fun createEngine(): HttpClientEngineFactory<HttpClientEngineConfig>
}