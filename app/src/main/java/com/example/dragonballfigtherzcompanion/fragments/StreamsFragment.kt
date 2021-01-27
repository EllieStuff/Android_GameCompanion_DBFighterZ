package com.example.dragonballfigtherzcompanion.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.dragonballfigtherzcompanion.Constants
import com.example.dragonballfigtherzcompanion.LoginActivity
import com.example.dragonballfigtherzcompanion.R
import com.example.dragonballfigtherzcompanion.RegisterActivity
import com.example.dragonballfigtherzcompanion.activity.TwitchLoginActivity
import com.example.dragonballfigtherzcompanion.services.NetWorkManager
import com.example.dragonballfigtherzcompanion.services.UserManager
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import io.ktor.client.request.*
import io.ktor.utils.io.*
import kotlinx.android.synthetic.main.fragment_news.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StreamsFragment: Fragment() {

    private lateinit var twitchLoginButton: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //Log.i(TAG, "++ onCreateView ++")
        return inflater.inflate(R.layout.fragment_streams, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        initListeners()
        getTopGames()
    }

    override fun onResume(){
        super.onResume()
        checkUserAvailability()
    }

    private fun initViews(view: View){
        twitchLoginButton = view.findViewById(R.id.twitchLoginButton)
    }

    private fun initListeners() {
        twitchLoginButton.setOnClickListener{
            startActivity(Intent(requireActivity(), TwitchLoginActivity::class.java))
        }
    }

    private fun checkUserAvailability() {
        val isLoggedIn = UserManager(requireContext()).getAccessToken() != null
        if (isLoggedIn) {
            twitchLoginButton.visibility = View.GONE
        } else{
            twitchLoginButton.visibility = View.VISIBLE
        }
    }

    private fun getTopGames() {
        val httpClient = NetWorkManager.createHttpClient()
        viewLifecycleOwner.lifecycleScope.launch {
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
                    }
                } catch (t : Throwable) {
                    //TODO: Handle error
                }
            }
        }
    }
}