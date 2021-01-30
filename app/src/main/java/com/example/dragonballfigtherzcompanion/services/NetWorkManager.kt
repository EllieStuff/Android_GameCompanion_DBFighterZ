package com.example.dragonballfigtherzcompanion.services

import android.app.Application
import android.util.Log
import com.example.dragonballfigtherzcompanion.model.TwitchUserResponse
import io.ktor.application.call
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.json.*
//import io.ktor.routing.route
import io.ktor.client.features.json.serializer.*
//import io.ktor.locations.Locations
import kotlinx.serialization.json.Json
import io.ktor.features.*
import io.ktor.http.HttpStatusCode
import io.ktor.locations.Locations
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.application.*

object NetWorkManager {
    /*fun Application.module(){
        install(ContentNegotiation){
            json()
        }
    }
    fun Application.registerCustomerRoutes(){
        routing{
            customerRouting()
        }
    }*/
    fun createHttpClient() : HttpClient {
        //Configure Json parsing
        val jsonConfig = Json {
            ignoreUnknownKeys = true
            isLenient = true
            encodeDefaults = false
        }
        //Configure client
        return HttpClient(OkHttp){
            //install(Routing)
            install(JsonFeature) {
                serializer = KotlinxSerializer(jsonConfig)
            }


        }

        //Route.customerRouting()
    }

    fun lookFor(valueName: String, response: String, getPos: Int = 0): String? {
        var idPos = response.indexOf(valueName)
        if(idPos < 0 || getPos < 0){
            Log.e("NetWorkManager", "$valueName was not found")
            return null
        }
        else {
            var margin = 3
            if(valueName == "total") margin = 2
            var value: String = ""
            if(getPos >= 0){
                var count: Int = 0
                while(count < getPos){
                    idPos = response.indexOf(valueName, idPos + 1)
                    if(idPos < 0){
                        Log.e("NetWorkManager", "$valueName out of range")
                        return null
                    }
                    else{
                        count++
                    }
                }
                var currPos = idPos + valueName.length + margin
                while (response[currPos] != '"' && response[currPos] != ',') {
                    value += response[currPos]
                    currPos++
                }
            }
            Log.i("NetWorkManager", "$valueName found: $value")

            return value
        }
    }

    fun lookForAll(valueName: String, response: String, maximum: Int = 10): List<String>? {
        var idPos = response.indexOf(valueName)
        if(idPos < 0){
            Log.e("NetWorkManager", "$valueName was not found")
            return null
        }
        else {
            val margin = 3
            var listOfValues = mutableListOf<String>()
            var maxCount = 0
            while (idPos >= 0 && maxCount < maximum) {
                maxCount++

                listOfValues.add("")
                var currPos = idPos + valueName.length + margin
                val lastIndex = listOfValues.lastIndex
                while (response[currPos] != '"') {
                    listOfValues.set(lastIndex, listOfValues.get(lastIndex) + response[currPos])
                    currPos++
                }

                val stringValue = listOfValues[lastIndex]
                Log.i("NetWorkManager", "$valueName n$lastIndex found: $stringValue")
                idPos = response.indexOf(valueName)
            }
            val message = listOfValues.toString()
            Log.i("NetWorkManager", "$valueName found: $message")

            return listOfValues.toList()
        }
    }

    /*
    val userResponses = mutableListOf<TwitchUserResponse>()
    fun Route.customerRouting(){
        route("/users"){
            get{
                if(userResponses.isNotEmpty()){
                    call.respond(userResponses)
                } else {
                    call.respondText("No users found", status = HttpStatusCode.NotFound)
                }
            }
            get("{id}"){

            }
            post{

            }
            delete("{id}"){

            }
        }
    }
    fun twitchUserResponse(){
        routing{

        }
    }*/
}
