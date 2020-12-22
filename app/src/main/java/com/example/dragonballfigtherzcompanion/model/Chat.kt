package com.example.dragonballfigtherzcompanion.model

import java.util.*

data class Chat(
        val id: String = "",
        val name: String = "",
        val users: List<String?> = emptyList(),
        val date: Date = Date(),
        var messagesToRead: Int = 0

        /*
        val message: String? = null,
        val userId: String? = null,
        val sentAt: Long? = null,
        val isSent: Boolean? = null,    //1 check
        val imageUrl: String? = null,
        //User
        val username: String? = null,
        val avatarUrl: String? = null

         */
        //Speed vs Consistence
)