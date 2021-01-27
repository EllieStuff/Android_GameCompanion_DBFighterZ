package com.example.dragonballfigtherzcompanion.services

import android.content.Context
import com.example.dragonballfigtherzcompanion.model.TwitchChannelResponse

class UserManager (context: Context){
    private val sharedPreferencesFileName = "userInfo"
    private val sharedPreferences = context.getSharedPreferences(sharedPreferencesFileName, Context.MODE_PRIVATE)

    private val accessTokenKey = "accessToken"
    private val channelIdKey = "channelId"

    fun getAccessToken() : String? {
        return sharedPreferences.getString(accessTokenKey, null)
    }
    fun getChannelId() : String? {
        return sharedPreferences.getString(channelIdKey, null)
    }

    fun saveAccessToken (token: String){
        sharedPreferences.edit().putString(accessTokenKey, token).apply()
    }
    fun saveChannelId (id: String){
        sharedPreferences.edit().putString(channelIdKey, id).apply()
    }

}