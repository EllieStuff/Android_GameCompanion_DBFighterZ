package com.example.dragonballfigtherzcompanion.model

import java.util.*

data class Message (
    val text: String = "",
    val from: String = "",
    val username: String? = "",
    val date: Date = Date(),
    val chatId: String = ""

)