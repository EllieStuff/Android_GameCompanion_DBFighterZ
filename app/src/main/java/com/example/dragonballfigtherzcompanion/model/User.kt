package com.example.dragonballfighterzcompanion.model

data class User (
    val userId: String = "",
    val username: String? = null,
    val email: String? = null,
    var avatarImgUrl: String? = null,
    var bio : String? = "",
    //val age: Int? = null,
    //val level: Int? = null,
    //val friends: List<User>? = null
)

