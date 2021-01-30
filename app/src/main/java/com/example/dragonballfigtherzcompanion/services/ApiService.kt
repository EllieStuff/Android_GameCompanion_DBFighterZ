package com.example.dragonballfigtherzcompanion.services

import com.example.dragonballfigtherzcompanion.Constants
import com.example.dragonballfigtherzcompanion.model.TWStreamsResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers

interface ApiService {
    companion object {
        private var retrofit = Retrofit.Builder()
                .baseUrl("https://api.twitch.tv/helix/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val service = retrofit.create<ApiService>(ApiService::class.java)

        /*
        @Headers("Client-ID: " + Constants.OAUTH_CLIENT_ID)
        @GET("streams")
        fun getStreams(): Call<TWStreamsResponse> {
        }
        */
    }

}