package com.example.dragonballfigtherzcompanion.model

import com.google.gson.annotations.SerializedName

data class TWStream(
        var title: String? = null,
        @SerializedName("user_name")
        var username: String? = null,
        private var thumbnail_url: String? = null
) {
    val imageUrl: String?
        get() {
            return thumbnail_url?.replace("{width}x{height}", "500x500")
        }
}


data class TWStreamsResponse(
        var data: ArrayList<TWStream>? = null
)
