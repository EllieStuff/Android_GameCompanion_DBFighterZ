package com.example.dragonballfigtherzcompanion.model
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TwitchChannelResponse(
        @SerialName("_id") val id: String,
        @SerialName("bio") val bio: String? = null,
        @SerialName("created_at") val createdAt: String? = null,
        @SerialName("display_name") val displayName: String? = null,
        @SerialName("logo") val logo: String? = null,
        @SerialName("name") val name: String,
        @SerialName("type") val type: String? = null,
        @SerialName("updated_at") val updatedAt: String? = null,
        //val scope: List<String>? = null,
        //@SerialName("token_type") val tokenType: String? = null,
)

