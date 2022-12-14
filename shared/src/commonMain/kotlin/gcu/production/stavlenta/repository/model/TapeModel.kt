package gcu.production.stavlenta.repository.model

import kotlinx.serialization.SerialName
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
    @SerialName("name") val name: String,
    @SerialName("body") val body: String,
    @SerialName("sourceUrl") val sourceUrl: String,
    @SerialName("type") val type: String,
)

enum class ContentType {
    ADD, EVENT, INSTITUTION
}