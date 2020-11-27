package com.example.dragonballfighterzcompanion.model

data class User {
    val userId: String,
    val usernamae: String? = null,
    val age: Int? = null,
    val level: Int? = null,
    val friends: List<User>? = null
}