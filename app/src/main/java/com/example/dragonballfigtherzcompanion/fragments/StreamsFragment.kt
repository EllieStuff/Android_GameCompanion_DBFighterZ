package com.example.dragonballfigtherzcompanion.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.dragonballfigtherzcompanion.Constants
import com.example.dragonballfigtherzcompanion.MainActivity
import com.example.dragonballfigtherzcompanion.R
import com.example.dragonballfigtherzcompanion.activity.TwitchLoginActivity
import com.example.dragonballfigtherzcompanion.model.TopGamesResponse
import com.example.dragonballfigtherzcompanion.model.TwitchChannelResponse
import com.example.dragonballfigtherzcompanion.services.NetWorkManager
import com.example.dragonballfigtherzcompanion.services.UserManager
import io.ktor.client.request.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StreamsFragment: Fragment() {

    private lateinit var twitchLoginButton: Button

    private val TAG = "StreamsFragment"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //Log.i(TAG, "++ onCreateView ++")
        return inflater.inflate(R.layout.fragment_streams, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        initListeners()
        getTopGames()
        getChannelId()
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

    /*
    private fun GetChannelId(token: String){
        val parseUrl: String = "https://api.twitch.tv/kraken/channel"
        val uri = Uri.parse(parseUrl)
                .buildUpon()
                /*.appendQueryParameter("client_id", Constants.OAUTH_CLIENT_ID)
                .appendQueryParameter("redirect_uri", Constants.OAUTH_REDIRECT_URI)
                .appendQueryParameter("response_type", "code")*/
                .appendQueryParameter("scope", "channel_read")
                .build()

        webView.settings.javaScriptEnabled = true

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                val url = request?.url?.toString()
                Log.i(TAG, "url $url")
                if(request?.url?.toString()?.startsWith(Constants.OAUTH_REDIRECT_URI) == true) {
                    // Login succcess
                    //Log.i(TAG, "Starting login with URL: ${request.url}")
                    request.url.getQueryParameter("code")?.let {
                        Log.i(TAG, "Got auth CODE $it")
                        webView.visibility = View.GONE
                        // exhange code + client_secret -> access token
                        getAccessTokens(it)
                    } ?: run {
                        // TODO: Handle error
                        Log.i(TAG, "mec")
                    }
                }
                else{
                    Log.i(TAG, "muc")
                }
                return super.shouldOverrideUrlLoading(view, request)
            }
        }

        Log.i(TAG, "Starting login with URL: ${uri}")
        // 2 - Load URL
        webView.loadUrl(uri.toString())
    }

    private fun getChanneId(authorizationCode: String){
        Log.i(TAG, "Getting Access Tokens with auth Code $authorizationCode")

        //Config Json Parsing
        val jsonConfig = Json{
            ignoreUnknownKeys = true
            isLenient = true
            encodeDefaults = false
        }

        //Create HttpClient
        val httpClient = HttpClient(OkHttp){
            install(JsonFeature){
                serializer = KotlinxSerializer(jsonConfig)
            }
        }

        //val httpClient = NetWorkManager.createHttpClient()
        lifecycleScope.launch {
            Log.i(TAG,"Launching get Tokens request")

            //GlobalScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val response: OAuthTokensResponse =
                            httpClient.post<OAuthTokensResponse>("https://api.twitch.tv/kraken") {
                                parameter("accept", "application/vnd.twitchtv.v5+json")
                                //parameter("client_secret", Constants.OAUTH_CLIENT_SECRET)
                                parameter("authorization", UserManager(this@TwitchLoginActivity).getAccessToken())
                            }
                    Log.i(TAG, "Got response from Twitch $response")
                    //Save Access token
                    UserManager(this@TwitchLoginActivity).saveAccessToken(response.accessToken)
                    //Close
                    Log.i(TAG, "mac")
                    finish()
                } catch (t: Throwable){
                    //TODO: Handle error
                    Log.i(TAG, "mic")
                }
            }

        }
    }
     */

    /*private fun loadOAuthUrl(){
        // 1 - Prepare URL
        val uri = Uri.parse(Constants.TWITCH_URL)
                .buildUpon()
                .appendQueryParameter("client_id", Constants.OAUTH_CLIENT_ID)
                .appendQueryParameter("redirect_uri", Constants.OAUTH_REDIRECT_URI)
                .appendQueryParameter("response_type", "code")
                // Nota: El jointToString{" "} transforma el contingut de l'array en una sola string separada pel contingut entre claus (en aquest cas un espai)
                .appendQueryParameter("scope", /*"user:edit%20user:read:email"*/listOf("user:edit", "user:read:email").joinToString ( " " ))
                .build()
        //val uri = "https://id.twitch.tv/oauth2/authorize?client_id=tiw703io13l627ubkg16hqt4cpl6p7&redirect_uri=http://localhost&response_type=code&scope=user:edit%20user:read:email"

        webView.settings.javaScriptEnabled = true
        //webView.webViewClient = WebViewClient()
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                val url = request?.url?.toString()
                Log.i(TAG, "url $url")
                if(request?.url?.toString()?.startsWith(Constants.OAUTH_REDIRECT_URI) == true) {
                    // Login succcess
                    //Log.i(TAG, "Starting login with URL: ${request.url}")
                    request.url.getQueryParameter("code")?.let {
                        Log.i(TAG, "Got auth CODE $it")
                        webView.visibility = View.GONE
                        // exhange code + client_secret -> access token
                        getAccessTokens(it)
                    } ?: run {
                        // TODO: Handle error
                        Log.i(TAG, "mec")
                    }
                }
                else{
                    Log.i(TAG, "muc")
                }
                return super.shouldOverrideUrlLoading(view, request)
            }
        }

        Log.i(TAG, "Starting login with URL: ${uri}")
        // 2 - Load URL
        webView.loadUrl(uri.toString())


        /*
        Client id: tiw703io13l627ubkg16hqt4cpl6p7
        Client secret: sy9o3n31mqvn2xemwj7tw21iqp72i9

        Redirect URL: http://localhost


        GET https://id.twitch.tv/oauth2/authorize
            ?client_id=<your client ID>
            &redirect_uri=<your registered redirect URI>
            &response_type=code
            &scope=&scope=user:edit%20user:read:email
        */


    }*/

    /*private fun isLive() : Boolean {
        val httpClient = NetWorkManager.createHttpClient()
        viewLifecycleOwner.lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                val accessToken = com.example.dragonballfigtherzcompanion.services.UserManager(requireContext()).getAccessToken()
                //Get Top Games
                try {
                    val response = httpClient.get<String>("https://api.twitch.tv/helix/search/channels?query=  -UserName-  ") {
                        header("Client-Id", Constants.OAUTH_CLIENT_ID)
                        header("Authorization", "Bearer  $accessToken")
                        //parameter("scope", "channel_read")
                    }
                    Log.i(TAG, "Got User Id: $response")
                    //UserManager(requireContext()).saveChannelId(response.id)
                    //Change to Main Thread
                    withContext(Dispatchers.Main) {
                        //TODO: Update UI
                    }
                } catch (t : Throwable) {
                    //TODO: Handle error
                    Log.i(TAG, "Didn't get User Id")
                }
            }
        }
    }*/

    private fun getChannelId(){
        val httpClient = NetWorkManager.createHttpClient()
        viewLifecycleOwner.lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                val accessToken = com.example.dragonballfigtherzcompanion.services.UserManager(requireContext()).getAccessToken()
                //Get Top Games
                try {
                    val response = httpClient.get<String>("https://api.twitch.tv/kraken/user") {
                        header("Accept", "application/vnd.twitchtv.v5+json")
                        header("Client-Id", Constants.OAUTH_CLIENT_ID)
                        header("Authorization", "Bearer  $accessToken")
                        //parameter("scope", "channel_read")
                    }
                    Log.i(TAG, "Got User Id: $response")
                    //UserManager(requireContext()).saveChannelId(response.id)
                    //Change to Main Thread
                    withContext(Dispatchers.Main) {
                        //TODO: Update UI
                    }
                } catch (t : Throwable) {
                    //TODO: Handle error
                    Log.i(TAG, "Couldn't get User Id")
                }
            }
        }
    }

    private fun getTopGames() {
        val httpClient = NetWorkManager.createHttpClient()
        viewLifecycleOwner.lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                val accessToken = com.example.dragonballfigtherzcompanion.services.UserManager(requireContext()).getAccessToken()
                //Get Top Games
                try {
                    val response: TopGamesResponse = httpClient.get<TopGamesResponse>("https://api.twitch.tv/helix/games/top") {
                        header("Client-Id", Constants.OAUTH_CLIENT_ID)
                        header("Authorization", "Bearer $accessToken")
                        //parameter()
                    }
                    val id = response.id
                    val name = response.name
                    //val boxArtUrl = response.boxArtUrl
                    //Log.i(TAG, "Got Top Games: id: $id, name: $name, box_art_url: $boxArtUrl. ")
                    //Change to Main Thread
                    withContext(Dispatchers.Main) {
                        //TODO: Update UI
                    }
                } catch (t : Throwable) {
                    //TODO: Handle error
                    Log.i(TAG, "Couldn't get Top Games")
                }
            }
        }
    }
}