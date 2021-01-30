package com.example.dragonballfigtherzcompanion.model
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class TwitchUserResponse(
        //@SerialName("data") val data: List<String>? = null,
        @SerialName("id") val id: String? = null,
        @SerialName("login") val loginName: String? = null,
        @SerialName("display_name") val displayName: String? = null,
        @SerialName("type") val type: String? = null,
        @SerialName("broadcaster_type") val broadcasterType: String? = null,
        @SerialName("description") val description: String? = null,
        @SerialName("profile_image_url") val profileImgUrl: String? = null,
        @SerialName("offline_image_url") val offlineImageUrl: String? = null,
        @SerialName("view_count") val viewCount: String? = null,
        @SerialName("email") val email: String? = null,
        //val scope: List<String>? = null,
        //@SerialName("token_type") val tokenType: String? = null,
)


//val userResponses = mutableListOf<TwitchUserResponse>()
