package gcu.production.stavlenta.repository.rest.repository

import gcu.production.stavlenta.repository.model.AddTapeEntity
import gcu.production.stavlenta.repository.rest.source.ContentSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class ContentRepository(private val source: ContentSource) {

    suspend fun getContentPage(search: String = "", page: Int = 0, size: Int = 10) =
        source.getContentPage(search, page, size)


    suspend fun addContent(body: AddTapeEntity, content: List<ByteArray>) =
        withContext(Dispatchers.Default) {
            source.addContent(body, content)
        }
}