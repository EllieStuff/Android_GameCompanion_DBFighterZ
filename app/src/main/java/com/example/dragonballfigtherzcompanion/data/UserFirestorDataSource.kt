package com.example.dragonballfigtherzcompanion.data

import com.example.dragonballfighterzcompanion.model.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.example.dragonballfigtherzcompanion.Constants.COLLECTION_USERS

class UserFirestorDataSource {
    fun getUser(userId: String, resultListener: ((User?) -> Unit) ){
        Firebase.firestore
            .collection(COLLECTION_USERS)
            .document(userId)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val user = it.result?.toObject(User::class.java)
                    resultListener(user)
                } else {
                    resultListener(null)
                }
            }
    }
}