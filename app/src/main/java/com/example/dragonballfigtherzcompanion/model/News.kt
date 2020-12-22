package com.example.dragonballfigtherzcompanion.model

data class News(
        val user_name: String? = "Player",
        val rank: String? = "Saiyan",
        val fav_char: String? = "Piccolo",
        val victory: String? = "You Won",
        val ranking: Int? = 0,
        val victory_rate: Int? = 0,
        val play_time: Int? = 0,
        val max_combo: Int? = 0
        )