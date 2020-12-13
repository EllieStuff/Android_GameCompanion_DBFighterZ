package com.example.dragonballfigtherzcompanion.data

import android.content.Context
import kotlinx.android.synthetic.main.activity_register.view.*

class UserRepository (
    private val firestorDataSource: UserFirestorDataSource = UserFirestorDataSource(),
    private val localDataSource: UserLocalDataSource = UserLocalDataSource()
){

    //Tries to get username locally first
    //If not available, get from Firestore
    //And save locally
    fun getUsername(context: Context, userId: String, resultListener: ((String?) -> Unit)) {
        localDataSource.getUsername(context)?.let { username ->
            //Return username
            resultListener(username)
        } ?: run {
            //Get from Firestore
            firestorDataSource.getUser(userId) {user: User? ->
                //Save locally
                localDataSource.saveUsername(context, user?.username =: "") //TODO: Allow null
                //Return result
                resultListener(user?.username)
            }
        }
    }
}