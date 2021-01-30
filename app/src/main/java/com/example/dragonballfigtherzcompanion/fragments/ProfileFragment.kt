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
import android.widget.*
import androidx.core.widget.addTextChangedListener
import com.bumptech.glide.Glide
import com.example.dragonballfighterzcompanion.model.User
import com.example.dragonballfigtherzcompanion.Constants
import com.example.dragonballfigtherzcompanion.Constants.COLLECTION_USERS
import com.example.dragonballfigtherzcompanion.LoginActivity
import com.example.dragonballfigtherzcompanion.R
import com.example.dragonballfigtherzcompanion.RegisterActivity
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
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

    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        Log.i(TAG, "++ onCreateView ++")
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i(TAG, "++ onViewCreated ++")
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
    }

    private fun initViews(parentView: View) {
        registerButton = parentView.findViewById<Button>(R.id.registerButton)
        avatarButton = parentView.findViewById<Button>(R.id.avatarButton)
        avatarImageView = parentView.findViewById<ImageView>(R.id.avatarImageView)
        loginButton = parentView.findViewById<Button>(R.id.loginButton)
        logoutButton = parentView.findViewById<Button>(R.id.logoutButton)
        bioTextView = parentView.findViewById<EditText>(R.id.bioTextView)
        usernameTextView = parentView.findViewById<TextView>(R.id.usernameTextView)
    }

    override  fun onStart(){
        super.onStart()
        Log.i(TAG, "++ onStart ++")
        // Check User
        checkUserAvailability()
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

    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch(e: ActivityNotFoundException) {
            // display error state to the user
        }
    }

    private fun checkUserAvailability() {
        Firebase.auth.currentUser?.let {
            // User available
            registerButton.visibility = View.GONE
            loginButton.visibility = View.GONE
            logoutButton.visibility = View.VISIBLE
            avatarButton.visibility = View.VISIBLE
            bioTextView.visibility = View.VISIBLE
            avatarImageView.visibility = View.VISIBLE
            usernameTextView.visibility = View.VISIBLE
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
}