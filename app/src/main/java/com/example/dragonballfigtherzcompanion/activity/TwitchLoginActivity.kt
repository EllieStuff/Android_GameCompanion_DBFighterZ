package com.example.dragonballfigtherzcompanion.activity

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.dragonballfigtherzcompanion.Constants
import com.example.dragonballfigtherzcompanion.R
import io.ktor.client.request.*
import io.ktor.client.engine.okhttp.*

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
        val uri = Uri.parse("https://id.twitch.tv/oauth2/authorize")
                .buildUpon()
                .appendQueryParameter("client_id", Constants.OAUTH_CLIENT_ID)
                .appendQueryParameter("redirect_uri", Constants.OAUTH_REDIRECT_URI)
                .appendQueryParameter("response_type", "code")
                // Nota: El jointToString{" "} transforma el contingut de l'array en una sola string separada pel contingut entre claus (en aquest cas un espai)
                .appendQueryParameter("scope", listOf("user:edit", "user:read:email").joinToString { " " })
            .build()

        webView.settings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {

                if(request?.url?toString()?.startsWith(Constants.OAUTH_REDIRECT_URI) == true) {
                    // Login succcess
                    Log.i(tag: TAG, msg: "Starting login with URL: ${request.url}")
                    request.url.getQueryParameter(key: "code")?.let {
                        Log.i(tag: TAG, msg: "Got auth CODE $it)
                        webView.visibility = View.GONE
                        // exhange code + client_secret -> access token
                        getAccessTokens(it)
                    } ?: run {
                        // TODO: Handle error
                    }
                }
                return super.shouldOverrideUrlLoading(view, request)
            }
        }
        Log.i(tag: TAG, msg: "Starting login with URL: ${uri.toString()}")
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
        Log.i(TAG, msg: "Getting Access Tokens with auth Code $authorizationCode")
        val httpClient = HttpClient(OkHttp)

        lifecycleScope.launch {
            delay(timeMillis: 3000L)
            Log.i(TAG, msg: "Launching get Tokens request")

            //GlobalScope.launch {
            withContext(Dispatchers.IO) {
                val response =
                    httpClient.post<String>(urlString: "https://id.twitch.tv/oauth2/token") {
                parameter("client_id", Constants.OAUTH_CLIENT_ID)
                parameter("client_secret", Constants.OAUTH_CLIENT_SECRET)
                parameter("code", authorizationCode)
                parameter("grant_type", "authorization_code")
                parameter("redirect_uri", Constants.OAUTH_REDIRECT_URI)
            }
                Log.i(TAG, msg: "Got response from Twitch $response")
            }
        }
    }

}