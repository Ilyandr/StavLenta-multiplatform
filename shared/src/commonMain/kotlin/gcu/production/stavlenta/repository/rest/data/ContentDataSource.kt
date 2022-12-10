package gcu.production.stavlenta.repository.rest.data

import gcu.production.stavlenta.repository.feature.other.toJson
import gcu.production.stavlenta.repository.model.TapeModel
import gcu.production.stavlenta.repository.rest.source.ContentSource
import io.ktor.client.*
import io.ktor.client.request.forms.*
import io.ktor.http.*


class ContentDataSource(private val client: HttpClient) : ContentSource {

    // testing stage
    override suspend fun addContent(model: TapeModel, filesByte: List<ByteArray>) =
        client.submitFormWithBinaryData(
            url = "/api/addContentWithFiles",
            formData = formData {
                append("content", model.toJson())

                filesByte.forEach { singleBytes ->
                    append(
                        "files",
                        singleBytes,
                        Headers.build { append(HttpHeaders.ContentType, "image/png") })
                }
            })

    // develop stage
    /* @GET(value = "/api/getContent")
     suspend fun getContentWithoutWrapperAsync(
         @Query("search") search: String = "",
         @Query("page") page: Int = 1,
         @Query("size") size: Int = 10
     ): List<TapeModel>*/
}