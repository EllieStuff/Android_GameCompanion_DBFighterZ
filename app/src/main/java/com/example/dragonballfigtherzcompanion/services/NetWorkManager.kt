package com.example.dragonballfigtherzcompanion.services

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import kotlinx.serialization.json.Json

object NetWorkManager {
    fun createHttpClient() : HttpClient {
        //Configure Json parsing
        val jsonConfig = Json {
            ignoreUnknownKeys = true
            isLenient = true
            encodeDefaults = false
        }
        //Configure client
        return HttpClient(OkHttp){
            install(JsonFeature) {
                serializer = KotlinxSerializer(jsonConfig)
            }
        }
    }
}