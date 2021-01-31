package com.example.dragonballfigtherzcompanion.activity

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.dragonballfigtherzcompanion.Constants
import com.example.dragonballfigtherzcompanion.R
import com.example.dragonballfigtherzcompanion.fragments.ProfileFragment
import com.example.dragonballfigtherzcompanion.model.OAuthTokensResponse
import com.example.dragonballfigtherzcompanion.services.NetWorkManager
import com.example.dragonballfigtherzcompanion.services.UserManager
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.http.ContentType.Application.Json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class TwitchLoginActivity : AppCompatActivity() {

    private var TAG = "TwitchLoginActivity"

    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_twitch_login)
        initViews()
        loadOAuthUrl()
    }

    private fun initViews(){
        webView = findViewById(R.id.webView)
    }

    private fun loadOAuthUrl(){
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


    }

    private fun getAccessTokens(authorizationCode: String) {
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
                        httpClient.post<OAuthTokensResponse>("https://id.twitch.tv/oauth2/token") {
                            parameter("client_id", Constants.OAUTH_CLIENT_ID)
                            parameter("client_secret", Constants.OAUTH_CLIENT_SECRET)
                            parameter("code", authorizationCode)
                            parameter("grant_type", "authorization_code")
                            parameter("redirect_uri", Constants.OAUTH_REDIRECT_URI)
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

}