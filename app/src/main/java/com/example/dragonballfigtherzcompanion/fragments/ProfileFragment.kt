package com.example.dragonballfigtherzcompanion.fragments

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import androidx.fragment.app.Fragment
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.*
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.dragonballfighterzcompanion.model.User
import com.example.dragonballfigtherzcompanion.Constants
import com.example.dragonballfigtherzcompanion.Constants.COLLECTION_USERS
import com.example.dragonballfigtherzcompanion.LoginActivity
import com.example.dragonballfigtherzcompanion.R
import com.example.dragonballfigtherzcompanion.activity.RegisterActivity
import com.example.dragonballfigtherzcompanion.activity.TwitchLoginActivity
import com.example.dragonballfigtherzcompanion.services.NetWorkManager
import com.example.dragonballfigtherzcompanion.services.UserManager
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

class ProfileFragment : Fragment() {

    private val TAG = "ProfileManager"

    private val REQUEST_IMAGE_CAPTURE = 100

    private lateinit var loginButton: Button
    private lateinit var avatarButton: Button
    private lateinit var avatarImageView: ImageView
    private lateinit var logoutButton: Button
    private lateinit var registerButton: Button
    private lateinit var bioTextView: EditText
    private lateinit var usernameTextView: TextView
    private lateinit var twitchLoginButton: ImageButton
    private lateinit var twitchUsernameTextView: TextView
    private lateinit var twitchLanguageTextView: TextView
    private lateinit var twitchFollowersTextView: TextView

    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        Log.i(TAG, "++ onCreateView ++")
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onResume() {
        super.onResume()
        checkUserAvailability()
        checkUserTwitchAvailability()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i(TAG, "++ onViewCreated ++  -> id: $id") //2131230912
        // Init firestore
        firestore = Firebase.firestore
        // Init View
        initViews(view)
        // Init Listeners
        initListeners()
        // Init bio
        getBio()
        // Init Username
        getUsername()
        // Init Twitch textViews
        getTwitchData()
    }

    private fun initViews(parentView: View) {
        registerButton = parentView.findViewById<Button>(R.id.registerButton)
        avatarButton = parentView.findViewById<Button>(R.id.avatarButton)
        avatarImageView = parentView.findViewById<ImageView>(R.id.avatarImageView)
        loginButton = parentView.findViewById<Button>(R.id.loginButton)
        logoutButton = parentView.findViewById<Button>(R.id.logoutButton)
        bioTextView = parentView.findViewById<EditText>(R.id.bioTextView)
        usernameTextView = parentView.findViewById<TextView>(R.id.usernameTextView)
        twitchLoginButton = parentView.findViewById<ImageButton>(R.id.buttonTwitch)
        twitchUsernameTextView = parentView.findViewById<TextView>(R.id.twitchUsernameTextView)
        twitchLanguageTextView = parentView.findViewById<TextView>(R.id.broadcasterLanguageTextView)
        twitchFollowersTextView = parentView.findViewById<TextView>(R.id.twitchFollowersTextView)
    }

    override  fun onStart(){
        super.onStart()
        Log.i(TAG, "++ onStart ++")
        // Check User Availability
        checkUserAvailability()
        checkUserTwitchAvailability()
    }

    private fun initListeners() {
        loginButton.setOnClickListener {
            // Track register button click
            Firebase.analytics.logEvent("loginButtonClick", null)
            // Open login activity
            ///it: View!
            val intent = Intent(activity, LoginActivity::class.java)
            checkUserAvailability()
            startActivity(intent)

        }

        bioTextView.addTextChangedListener{
            val bio = bioTextView.text.toString()
            if (bio.isBlank()) return@addTextChangedListener
            setBio(bio)
        }

        avatarButton.setOnClickListener {
            // TODO: Open Camera
            dispatchTakePictureIntent()
        }

        registerButton.setOnClickListener {
            // Track register button click
            Firebase.analytics.logEvent("registerButtonClick", null)
            // Open register activity
            ///it: View!
            val intent = Intent(activity, RegisterActivity::class.java)
            checkUserAvailability()
            startActivity(intent)

        }

        logoutButton.setOnClickListener{
            //Track register button click
            Firebase.analytics.logEvent("logoutButtonClick", null)
            //Open logout activity
            Firebase.auth.signOut()
            checkUserAvailability()
        }

        twitchLoginButton.setOnClickListener {
            startActivity(Intent(requireActivity(), TwitchLoginActivity::class.java))
        }

    }

    /*override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        checkUserAvailability()
        checkUserTwitchAvailability()
    }*/

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch(e: ActivityNotFoundException) {
            // display error state to the user
        }
    }

    public fun checkUserTwitchAvailability() {
        val isLoggedIn = UserManager(requireContext()).getAccessToken() != null
        if (isLoggedIn) {
            //twitchLoginButton.visibility = View.GONE
            getTwitchData()
        } else{
            //twitchLoginButton.visibility = View.VISIBLE
            readTwitchData()
        }
    }

    public fun checkUserAvailability() {
        Firebase.auth.currentUser?.let {
            // User available
            registerButton.visibility = View.GONE
            loginButton.visibility = View.GONE
            logoutButton.visibility = View.VISIBLE
            avatarButton.visibility = View.VISIBLE
            bioTextView.visibility = View.VISIBLE
            avatarImageView.visibility = View.VISIBLE
            usernameTextView.visibility = View.VISIBLE
            twitchLoginButton.visibility = View.VISIBLE
            twitchUsernameTextView.visibility = View.VISIBLE
            twitchLanguageTextView.visibility = View.VISIBLE
            twitchFollowersTextView.visibility = View.VISIBLE
            // Get User profile
            Firebase.firestore
                    .collection(Constants.COLLECTION_USERS)
                    .document(it.uid)
                    .get()
                    .addOnSuccessListener {
                        it.toObject(User::class.java)?.let { user ->
                            val avatarImgUrl = user.avatarImgUrl
                            // Load avatar Img Url into ImageView
                            // 1 - Open URL
                            // 2 - Download file
                            // 3 - Load image file into ImageView
                            // * - Cache (si ja has descarregat una URL no tornar-ho a fer)
                            // * - Delete cache (borrar als 30 dies si no s'ha utilitzat)
                            Glide.with(avatarImageView)
                                    .load(avatarImgUrl)
                                    .into(avatarImageView)
                        }
                    }
        } ?: run {
            // User not available
            registerButton.visibility = View.VISIBLE
            loginButton.visibility = View.VISIBLE
            logoutButton.visibility = View.GONE
            avatarButton.visibility = View.GONE
            bioTextView.visibility = View.GONE
            avatarImageView.visibility = View.GONE
            usernameTextView.visibility = View.GONE
            twitchLoginButton.visibility = View.GONE
            twitchUsernameTextView.visibility = View.GONE
            twitchLanguageTextView.visibility = View.GONE
            twitchFollowersTextView.visibility = View.GONE
        }
    }

    private fun saveData() {
        val sharedPreferences = activity!!.getSharedPreferences("test", Context.MODE_PRIVATE)

        sharedPreferences.edit()
            .putString("firstKey", "value")
            .apply()
    }

    private fun setBio(bio : String){
        Firebase.auth.currentUser?.uid?.let {userId:String ->
            firestore
                .collection(COLLECTION_USERS)
                .document(userId)
                .get()
                .addOnCompleteListener{
                    if (it.isSuccessful){
                        val user = it.result?.toObject(User::class.java)?.let { user : User ->
                            val userTemp = User (
                                userId = user.userId,
                                username = user.username,
                                email = user.email,
                                avatarImgUrl = user.avatarImgUrl,
                                bio = bio
                            )
                            firestore
                                .collection(COLLECTION_USERS)
                                .document(userId)
                                .set(userTemp)
                        }
                    }
                }
        }
    }

    private fun getBio(){
        Firebase.auth.currentUser?.uid?.let {userId:String ->
            firestore
                .collection(COLLECTION_USERS)
                .document(userId)
                .get()
                .addOnCompleteListener{
                    if (it.isSuccessful){
                        val user = it.result?.toObject(User::class.java)?.let { user : User ->
                            bioTextView.setText(user.bio)
                        }
                    }
                }
        }
    }

    private fun getUsername(){
        Firebase.auth.currentUser?.uid?.let {userId:String ->
            firestore
                .collection(COLLECTION_USERS)
                .document(userId)
                .get()
                .addOnCompleteListener{
                    if (it.isSuccessful){
                        val user = it.result?.toObject(User::class.java)?.let { user : User ->
                            usernameTextView.text = user.username
                        }
                    }
                }
        }
    }

    private fun uploadImageToFirebaseStorage(bitmap: Bitmap){
        val storage = Firebase.storage

        // Convert bitmap to bytes list
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        // Obtain Firebase Storage referenced
        val userId = Firebase.auth.currentUser?.uid
        val storageReference = storage.reference.child("images/avatars/$userId.jpg")

        // Upload to Firebase Storage
        storageReference.putBytes(data)
                .addOnSuccessListener { taskSnapshot ->
                    storageReference.downloadUrl
                            .addOnSuccessListener { uri ->
                                val url = uri.toString()
                                // Set to user profile in Firestore
                                Firebase.firestore
                                        .collection(Constants.COLLECTION_USERS)
                                        .document(userId!!)
                                        .update("avatarImgUrl", url)
                            }
                }
                .addOnFailureListener {

                }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK){
            (data?.extras?.get("data") as? Bitmap)?.let { bitmap ->
                avatarImageView.setImageBitmap(bitmap)
                uploadImageToFirebaseStorage(bitmap)
            }
        }
    }

    fun readTwitchData(){

        var tmp = UserManager(requireContext()).getUsername().orEmpty();

        if(tmp != "")
        {
            // Get Username
            twitchUsernameTextView.setText("Username: " + UserManager(requireContext()).getUsername().orEmpty())
            // Get Language
            twitchLanguageTextView.setText("Language: " + UserManager(requireContext()).getUserLanguage().orEmpty())
            // Get Followers
            twitchFollowersTextView.setText("Followers: " + UserManager(requireContext()).getUserFollowers().orEmpty())
        }
        else
        {
            // Get Username
            twitchUsernameTextView.setText(" ")
            // Get Language
            twitchLanguageTextView.setText(" ")
            // Get Followers
            twitchFollowersTextView.setText(" ")
        }
    }

    fun getTwitchData(){
        getUserId()
        getChannelData()
        getChannelFollowers()
    }

    private fun getUserId(){
        val httpClient = NetWorkManager.createHttpClient()
        viewLifecycleOwner.lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                val accessToken = UserManager(requireContext()).getAccessToken()
                Log.i(TAG, "token2 $accessToken")
                try {
                    val response: String = httpClient.get<String>("https://api.twitch.tv/helix/users") {
                        //header("Accept", "application/vnd.twitchtv.v5+json")
                        header("Client-Id", Constants.OAUTH_CLIENT_ID)
                        header("Authorization", "Bearer $accessToken")
                        //parameter("scope", "channel_read")
                    }
                    Log.i(TAG, "Got User: $response")

                    NetWorkManager.lookFor("id", response)?.let{ id->
                        UserManager(requireContext()).saveChannelId(id)
                    }
                    NetWorkManager.lookFor("login", response)?.let{ name->
                        UserManager(requireContext()).saveLoginName(name)
                    }

                } catch (t : Throwable) {
                    //TODO: Handle error
                    Log.i(TAG, "Couldn't get User Data")
                }
            }
        }
    }

    private fun getChannelData(){
        val httpClient = NetWorkManager.createHttpClient()
        viewLifecycleOwner.lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                val accessToken = com.example.dragonballfigtherzcompanion.services.UserManager(requireContext()).getAccessToken()
                Log.i(TAG, "token2 $accessToken")
                try {
                    val response: String = httpClient.get<String>("https://api.twitch.tv/helix/channels") {
                        //header("Accept", "application/vnd.twitchtv.v5+json")
                        header("Client-Id", Constants.OAUTH_CLIENT_ID)
                        header("Authorization", "Bearer $accessToken")
                        parameter("broadcaster_id", UserManager(requireContext()).getChannelId())
                    }
                    Log.i(TAG, "Got Channel: $response")

                    //twitchLanguageTextView.setText("")
                    NetWorkManager.lookFor("broadcaster_language", response)?.let{ lang->
                        UserManager(requireContext()).saveUserLanguage(lang)
                        twitchLanguageTextView.setText("Language: " + lang)
                    }
                    //twitchUsernameTextView.setText("")
                    NetWorkManager.lookFor("broadcaster_name", response)?.let{ name->
                        UserManager(requireContext()).saveUsername(name)
                        twitchUsernameTextView.setText("Username: " + name)
                    }

                } catch (t : Throwable) {
                    //TODO: Handle error
                    Log.i(TAG, "Couldn't get Channel Data")
                }
            }
        }
    }

    private fun getChannelFollowers(){
        val httpClient = NetWorkManager.createHttpClient()
        viewLifecycleOwner.lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                val accessToken = com.example.dragonballfigtherzcompanion.services.UserManager(requireContext()).getAccessToken()
                try {
                    val response: String = httpClient.get<String>("https://api.twitch.tv/helix/users/follows?to_id=" + UserManager(requireContext()).getChannelId()) {
                        //header("Accept", "application/vnd.twitchtv.v5+json")
                        header("Client-Id", Constants.OAUTH_CLIENT_ID)
                        header("Authorization", "Bearer $accessToken")
                        //parameter("live_only", true)
                    }
                    Log.i(TAG, "Got Channel Followers: $response")

                    NetWorkManager.lookFor("total", response)?.let{ follows->
                        UserManager(requireContext()).saveUserFollowers(follows)
                        twitchFollowersTextView.setText("Followers: " + follows)
                    }

                } catch (t : Throwable) {
                    //TODO: Handle error
                    Log.i(TAG, "Couldn't get the total followers")
                }
            }
        }
    }

}