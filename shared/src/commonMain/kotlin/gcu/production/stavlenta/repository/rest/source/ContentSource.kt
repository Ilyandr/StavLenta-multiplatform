package gcu.production.stavlenta.repository.rest.source

import gcu.production.stavlenta.repository.model.TapeModel
import io.ktor.client.statement.*

interface ContentSource {

    suspend fun addContent(model: TapeModel, filesByte: List<ByteArray>): HttpResponse
}