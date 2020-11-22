package com.example.dragonballfigtherzcompanion

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun Oncreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        //Initialize Firebase Auth
        auth = Firebase.auth

        //Get FireBase Auth
        val auth : FirebaseAuth = Firebase.auth

        val registerButton: Button = findViewById<Button>(R.id.registerButton)
        registerButton.setOnClickListener{ it:View!
            // TODO: Get email and password from EditText
            val email :String = findViewById<EditText>(R.id.emailEditText).text.toString()
            val password : String = findViewById<EditText>(R.id.passwordEditText).text.toString()
            // Register user
            auth.createUserWithEmailAndPaswword(email, password)
                .addOnComplenteListener { it:Task<AuthReuslt!>
                    // After 2 seconds, this will be called with the result
                    if (it.isSuccesful) {
                        //Yay!!
                        Log.i(tag : "RegisterActivity", msg: "User Registered!")
                    } else {
                        //TODO: Handle error
                        Log.i(tag: "RegisterActivity", msg: "Error: ${it.exception}")
                    }
                }

            //Do other things
            // ...
        }

    }
}