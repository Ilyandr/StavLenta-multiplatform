package gcu.production.stavlenta.repository.model

import kotlinx.serialization.SerialName


@kotlinx.serialization.Serializable
data class UserModel(
    @SerialName(value = "id")
    val id: Long? = null,
    @SerialName(value = "name")
    val name: String? = null,
    @SerialName(value = "email")
    val email: String? = null,
    @SerialName(value = "password")
    val password: String? = null
)