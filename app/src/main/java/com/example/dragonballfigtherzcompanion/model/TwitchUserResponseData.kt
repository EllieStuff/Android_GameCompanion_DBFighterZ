package com.example.dragonballfigtherzcompanion.model
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class TwitchUserResponseData(
        //@SerialName("data") val data: List<String>? = null,

        @SerialName("data") val data: List<TwitchUserResponse>? = null,
        //val scope: List<String>? = null,
        //@SerialName("token_type") val tokenType: String? = null,
)


//val userResponses = mutableListOf<TwitchUserResponse>()
