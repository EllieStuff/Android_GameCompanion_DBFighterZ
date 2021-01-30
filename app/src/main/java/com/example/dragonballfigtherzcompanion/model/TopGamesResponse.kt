package com.example.dragonballfigtherzcompanion.model
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TopGamesResponse(
        @SerialName("id") val id: String,
        @SerialName("name") val name: String? = null,
        //@SerialName("box_art_url") val boxArtUrl: String? = null,
        //@SerialName("pagination") val pagination: String? = null,
        //val scope: List<String>? = null,
)

