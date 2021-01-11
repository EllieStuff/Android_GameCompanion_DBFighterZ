package com.example.dragonballfigtherzcompanion.model
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OAuthTokensResponse(
        @SerialName(value: "access_token") val accessToken: String,
        @SerialName(value: "refresh_token") val refreshToken: String? = null,
        @SerialName(value: "expires_in") val expiresIn: Int? = null,
        val scope: List<String>? = null,
        @SerialName(value: "token_type") val tokenType: String? = null,
)