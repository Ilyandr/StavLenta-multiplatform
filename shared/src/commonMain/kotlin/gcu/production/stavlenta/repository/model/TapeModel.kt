package gcu.production.stavlenta.repository.model

import kotlinx.serialization.Serializable

// Объявление класса-сущности с возможностью сериализации / десереализации в json. (для rest взаимодействия).
@Serializable
data class TapeModel(
    val id: Long? = null,
    val name: String? = null,
    val body: String = "",
    var images: List<String>? = null,
    val type: ContentType = ContentType.EVENT,
    val creatorId: Long? = null,
    val sourceUrl: String? = null
)

@Serializable
data class AddTapeEntity(
    val name: String,
    val body: String,
    val sourceUrl: String = "https://google.com/",
    val type: ContentType = ContentType.EVENT,
)

enum class ContentType {
    ADD, EVENT, INSTITUTION
}