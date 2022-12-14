package gcu.production.stavlenta.repository.rest.data

import gcu.production.stavlenta.repository.model.AddTapeEntity
import gcu.production.stavlenta.repository.model.TapeModel
import gcu.production.stavlenta.repository.rest.source.ContentSource
import io.ktor.client.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


class ContentDataSource(private val client: HttpClient) : ContentSource {

    private val format by lazy { Json { prettyPrint = true } }

    override suspend fun addContent(body: AddTapeEntity, content: List<ByteArray>) =
        client.submitFormWithBinaryData(
            url = "/api/addContentWithFiles",
            formData =  formData {
                Logger.DEFAULT.log( format.encodeToString(body))
                append("content", format.encodeToString(body))
                content.forEach { singleFile ->
                    append("files", singleFile, Headers.build {
                        append(HttpHeaders.ContentDisposition, "filename=${singleFile.size}.png")
                    })
                }
            }
        )

    override suspend fun getContentPage(search: String, page: Int, size: Int): List<TapeModel> =
        Json.decodeFromString(client.get {
            url {
                path("/api/getContent")
                parameters.append("search", search)
                parameters.append("page", page.toString())
                parameters.append("size", size.toString())
            }
        }.bodyAsText())
}