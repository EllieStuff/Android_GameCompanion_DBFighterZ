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

    fun lookFor(valueName: String, response: String): String? {
        val idPos = response.indexOf(valueName)
        if(idPos < 0){
            Log.e("NetWorkManager", "$valueName was not found")
            return null
        }
        else {
            val margin = 3
            var currPos = idPos + valueName.length + margin
            var value: String = ""
            while (response[currPos] != '"') {
                value += response[currPos]
                currPos++
            }
            Log.i("NetWorkManager", "$valueName found: $value")

            return value
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
