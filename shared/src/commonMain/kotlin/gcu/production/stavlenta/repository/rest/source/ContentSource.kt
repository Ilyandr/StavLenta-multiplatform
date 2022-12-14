package gcu.production.stavlenta.repository.rest.source

import gcu.production.stavlenta.repository.model.AddTapeEntity
import gcu.production.stavlenta.repository.model.TapeModel
import io.ktor.client.statement.*
import io.ktor.http.content.*


interface ContentSource {

    suspend fun addContent(body: AddTapeEntity, content: List<ByteArray>): HttpResponse
    suspend fun getContentPage(search: String, page: Int, size: Int): List<TapeModel>
}