package com.example.dragonballfigtherzcompanion.data

import android.content.Context
class UserLocalDataSource {
    private val preferencesFileName = "userPreferences"

    fun getUsername(context: Context): String {
        val sharedPrefs = context.getSharedPreferences(preferencesFileName, Context.MODE_PRIVATE)
        return sharedPrefs.getString("username", null)
    }

    fun saveUsername(context: Context, username: String) {
        val sharedPrefs = context.getSharedPreferences(preferencesFileName, Context.MODE_PRIVATE)
        return sharedPrefs.putString ("username", username).apply()
    }
}