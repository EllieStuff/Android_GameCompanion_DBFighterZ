package com.example.dragonballfigtherzcompanion.model

data class Chat(
        val message: String? = null,
        val userId: String? = null,
        val sentAt: Long? = null,
        val isSent: Boolean? = null,    //1 check
        val imageUrl: String? = null,
        //User
        val username: String? = null,
        val avatarUrl: String? = null
        //Speed vs Consistence
)