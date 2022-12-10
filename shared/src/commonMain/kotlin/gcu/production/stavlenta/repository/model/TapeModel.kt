package gcu.production.stavlenta.repository.model


data class TapeModel(
    val id: Long? = null,
    val name: String? = null,
    val body: String = "",
    var images: List<Any>? = null,
    val type: ContentType = ContentType.EVENT,
    val creatorId: Long? = null
)

data class AddTapeEntity(
    val name: String,
    val body: String,
    val type: ContentType = ContentType.EVENT,
)

enum class ContentType {
    ADD, EVENT, INSTITUTION
}