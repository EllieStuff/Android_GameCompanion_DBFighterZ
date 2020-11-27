package com.example.dragonballfigtherzcompanion

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

class RegisterActivity : AppCompatActivity() {

    private val TAG = "RegisterActivity"

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private val MIN_PASSWORD_LENGTH = 6
    // Views
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var usernameEditText: EditText
    private lateinit var registerButton: Button
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        //Init Views
        initViews()
        //Initialize Firebase Auth
        auth = Firebase.auth
        //Get FireBase Auth
        val auth : FirebaseAuth = Firebase.auth
        //Initialize FireStore
        firestore = Firebase.firestore
        //Exemple de contingut
        /*firestore
                .collection("users")
                .document("ascdfg")
                .whereEqualTo("userId", "gdsgtv")
                .addNoseComContinuavaAixo()
         */

        //Init Listeners
        initListeners()

    }

    private fun initViews(){
        emailEditText = findViewById<EditText>(R.id.emailEditText)
        passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        registerButton = findViewById<Button>(R.id.registerButton)
        usernameEditText = findViewById<Button>(R.id.usernameEditText)
        progressBar = findViewById(R.id.progressBar)

        //Initialize Firebase Auth
        auth = Firebase.auth
    }

    private fun initListeners(){
        registerButton.setOnClickListener{ it
            // TODO: Get email and password from EditText
            var username = usernameEditText.text.toString()
            if(username.isBlank()){
                usernameEditText.error = " Username cannot be empty"
                return@setOnClickListener
            }
            val email :String = emailEditText.text.toString()
            if(!isEmailValid(email)){
                Log.i(TAG, "Email not valid")
                //showMessage(getString(R.string.error_email_invalid))  //Forma alternativa de mostrar missatge
                emailEditText.error = getString(R.string.error_email_invalid)
                return@setOnClickListener
            }
            val password : String = passwordEditText.text.toString()
            if(!isPasswordValid(password)){
                Log.i(TAG, "Password not valid")
                passwordEditText.error = getString(R.string.error_password_invalid, MIN_PASSWORD_LENGTH)
                return@setOnClickListener
            }
            // Register user
            registerUser(email, password, username)

            private fun registerUser(email: String, password: String, username: String) {
                progressBar.visibility = View.VISIBLE
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { it
                        // After 2 seconds, this will be called with the result
                        if (it.isSuccessful) {
                            //Yay!!
                            auth.curentUser?.uid?.let { userId ->
                                val user = User(userId = UserId, username = username)
                                firestore
                                    .collection(Constants.COLLECTION_USERS)
                                    .document(auth.currentUser?.uid)
                                    .set(user)
                                .addOnCompleteListener {
                                    progressBar.visibility = View.GONE
                                    if(it.isSuccessful) {

                                    }
                                    else
                                    {
                                         showMessage(text: "Errrrorrrr ${it.exception?.message ?. ""}")
                                         progressBar.visibility = View.GONE
                                    }
                                }
                            } ?: kotlin.run {
                                Log.i(TAG, "User Registered!") // LOG.I o LOG.E ??
                                showMessage(text: "Error ${it.exception?.message ?. ""}")
                            }
                        } else {
                            //TODO: Handle error
                            Log.i(TAG, "Error: ${it.exception}")
                            showMessage("Error logging up ${it.exception?.message ?: ""}")
                            progressBar.visibility = View.GONE
                        }
                    }

            //Do other things
            // ...
        }
    }

    private fun isEmailValid(email: String) : Boolean{
        //Regex: forma de comprovar si un email pot ser valid comprovant totes les seves parts (nom)@(domini).(adreça)
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

    private fun isPasswordValid(password: String): Boolean{
        // 1 - No buit
        // 2 - Min 6 characters
        // 3 - Contains letters & numbers

        val passwordRegex = "[a-zA-Z0-9-.]"

        return password.isNotBlank()
                && password.count() >= MIN_PASSWORD_LENGTH
                && password.contains(Regex(passwordRegex))
    }

    private fun showMessage(text: String){
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }

    //Forma alternativa per comprovar si te numeros o lletres
    /*
    private fun containsLetterAndNumber(text: String): Boolean{
        var containsLetter = false
        var containsNumber = false
        for(char: Char in text){
            if(char.isDigit())
                containsNumber = true;
            if(char.isLetter())
                containsLetter = true;

            if(containsLetter && containsNumber)
                return true
        }

        return false
    }
    */

}