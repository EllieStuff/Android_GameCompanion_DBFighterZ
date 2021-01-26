package com.example.dragonballfigtherzcompanion.model

data class News(
        val user_name: String? = "Player",
        val victory: String? = "You Win",
        val rank: String? = "Saiyan",
        val fav_char: String? = "Piccolo",
        val ranking: Int? = 0,
        val victory_rate: Int? = 0,
        val play_time: Int? = 0,
        val max_combo: Int? = 0
        )