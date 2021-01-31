package com.example.dragonballfigtherzcompanion

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private val TAG = "LoginActivity"

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var loginButton: Button

    //Views
    private lateinit var emailLoginEditText: EditText
    private lateinit var passwordLoginEditText: EditText

    override fun onCreate (savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        //Init Views
        initViews()
        //Initialize Firebase Auth
        auth = Firebase.auth
        //Get FireBase auth
        val auth: FirebaseAuth = Firebase.auth
        //Initialize Firestore
        firestore = Firebase.firestore
        //Init listeners
        initListeners()

    }

    private fun initViews(){
        emailLoginEditText = findViewById<EditText>(R.id.emailLoginEditText)
        passwordLoginEditText = findViewById<EditText>(R.id.passwordLoginEditText)
        loginButton = findViewById<Button>(R.id.login_Button)
        //Initialize firebase auth
        auth = Firebase.auth
    }

    private fun initListeners() {
        loginButton.setOnClickListener {
            it
            var email : String = emailLoginEditText.text.toString()
            if (!isEmailValid(email)){
                Log.i(TAG, "Email not valid")
                emailLoginEditText.error = getString(R.string.error_email_invalid)
                return@setOnClickListener
            }
            val password : String = passwordLoginEditText.text.toString()
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful){
                        Log.i(TAG, "signInWithEmail:success")

                    } else {
                        Log.i(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    }
                }
            //visibility
            loginButton.isEnabled = false;
            Toast.makeText(baseContext, "Logged in", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
    }
    }


    private fun isEmailValid(email: String): Boolean {
        val emailRegex = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"

        return email.isNotBlank()
                && email.contains("@")
                && email.contains(Regex(emailRegex))
    }
}