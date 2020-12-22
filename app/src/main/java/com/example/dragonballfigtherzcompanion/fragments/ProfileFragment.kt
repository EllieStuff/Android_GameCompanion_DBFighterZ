package com.example.dragonballfigtherzcompanion.fragments

import android.content.Context
import androidx.fragment.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.dragonballfigtherzcompanion.LoginActivity
import com.example.dragonballfigtherzcompanion.R
import com.example.dragonballfigtherzcompanion.RegisterActivity
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ProfileFragment : Fragment() {

    private val TAG = "ProfileManager"

    private lateinit var loginButton: Button
    private lateinit var registerButton: Button

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
        // Init View
        initViews(view)
        // Init Listeners
        initListeners()
    }

    private fun initViews(parentView: View) {
        registerButton = parentView.findViewById<Button>(R.id.registerButton)
        loginButton = parentView.findViewById<Button>(R.id.loginButton)
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
            startActivity(intent)
        }
        registerButton.setOnClickListener {
            // Track register button click
            Firebase.analytics.logEvent("registerButtonClick", null)
            // Open register activity
            ///it: View!
            val intent = Intent(activity, RegisterActivity::class.java)
            startActivity(intent)
        }

    }

    private fun checkUserAvailability() {
        Firebase.auth.currentUser?.let {
            registerButton.visibility = View.GONE
            loginButton.visibility = View.GONE
        } ?: run {
            registerButton.visibility = View.VISIBLE
            loginButton.visibility = View.VISIBLE
        }
    }

    private fun saveData() {
        val sharedPreferences = activity!!.getSharedPreferences("test", Context.MODE_PRIVATE)

        sharedPreferences.edit()
            .putString("firstKey", "value")
            .apply()
    }
}