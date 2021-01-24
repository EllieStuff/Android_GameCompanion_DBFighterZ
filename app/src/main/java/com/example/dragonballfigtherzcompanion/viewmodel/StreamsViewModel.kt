package com.example.dragonballfigtherzcompanion.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dragonballfigtherzcompanion.services.UserManager

class StreamsViewModel(private val userManager: UserManager)
{

    val isLoggedIn = MutableLiveData<Boolean>()
    val topGames = MutableLiveData<List<Any>>()
    val errors = MutableLiveData<String?>()



    fun checkUserAvailability() {
        val isLoggedIn = userManager.getAccessToken() != null
        this.isLoggedIn.postValue(isLoggedIn)
        if(isLoggedIn) {
            getTopGames()
        }
    }

    private fun logout() {
        this.isLoggedIn.postValue(value: false)
    }

    private fun getTopGames() {
        val httpClient = NetWorkManager.createHttpClient()
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                val accessToken = com.example.dragonballfigtherzcompanion.services.UserManager(requireContext()).getAccessToken()
                //Get Top Games
                try {
                    val response = httpClient.get<String>("https://api.twitch.tv/helix/games/top") {
                        header("Client-Id", Constants.OAUTH_CLIENT_ID)
                        header("Authorization", "Bearer $accessToken")
                        //parameter()
                    }
                    Log.i("StreamsFragment", "Got Top Games: $response")
                    //Change to Main Thread
                    withContext(Dispatchers.Main) {
                        //TODO: Update UI
                        topGames.postValue(listOf(response))
                    }
                } catch (t : Throwable) {
                    //TODO: Handle error
                    errors.postValue(t.message)
                }
            }
        }
    }

    init {
        topGames.postValue(listOf())
    }

    fun onRefresh() {
        topGames.postValue(listOf())
    }
}