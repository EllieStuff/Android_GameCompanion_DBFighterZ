package com.example.dragonballfigtherzcompanion.services

import android.content.Context
import com.example.dragonballfigtherzcompanion.model.TwitchChannelResponse

class UserManager (context: Context){
    private val sharedPreferencesFileName = "userInfo"
    private val sharedPreferences = context.getSharedPreferences(sharedPreferencesFileName, Context.MODE_PRIVATE)

    private val accessTokenKey = "accessToken"
    private val channelIdKey = "channelId"
    private val loginNameKey = "loginName"
    private val usernameKey = "username"
    //private val isLiveKey = "isLive"
    private val languageKey = "language"
    private val followersKey = "followers"

    fun getAccessToken() : String? {
        return sharedPreferences.getString(accessTokenKey, null)
    }
    fun getChannelId() : String? {
        return sharedPreferences.getString(channelIdKey, null)
    }
    fun getLoginName() : String? {
        return sharedPreferences.getString(loginNameKey, null)
    }
    fun getUsername() : String? {
        return sharedPreferences.getString(usernameKey, null)
    }
    fun getUserLanguage() : String? {
        return sharedPreferences.getString(languageKey, null)
    }
    fun getUserFollowers() : String? {
        return sharedPreferences.getString(followersKey, null)
    }

    fun saveAccessToken (token: String){
        sharedPreferences.edit().putString(accessTokenKey, token).apply()
    }
    fun saveChannelId (id: String){
        sharedPreferences.edit().putString(channelIdKey, id).apply()
    }
    fun saveLoginName (name: String){
        sharedPreferences.edit().putString(loginNameKey, name).apply()
    }
    fun saveUsername (name: String){
        sharedPreferences.edit().putString(usernameKey, name).apply()
    }
    fun saveUserLanguage (lang: String){
        sharedPreferences.edit().putString(languageKey, lang).apply()
    }
    fun saveUserFollowers (follows: String){
        sharedPreferences.edit().putString(followersKey, follows).apply()
    }

}