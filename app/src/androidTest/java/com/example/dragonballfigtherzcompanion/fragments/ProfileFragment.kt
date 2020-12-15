package com.example.dragonballfigtherzcompanion.fragments

import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.dragonballfigtherzcompanion.R
import com.example.dragonballfigtherzcompanion.RegisterActivity
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ProfileFragment : Fragment() {

    private val TAG = "ProfileManager"

    private lateinit var registerButton: Button
    private lateinit var welcomeTextView: TextView

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
        welcomeTextView = parentView.findViewById<Button>(R.id.welcomeTextView)
    }

    override  fun onStart(){
        super.onStart()
        Log.i(TAG, "++ onStart ++")
        // Check User
        checkUserAvailability()
    }

    private fun initListeners() {
        //registerButton: Button! = view.findViewById<Button>(R.id.registerButton)
        registerButton.setOnClickListener {
            // Track register button click
            Firebase.analytics.logEvent("registerButtonClick", null)
            // Open registr activity
            it: View!
            val intent = Intent(activity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun checkUserAvailability() {
        Firebase.auth.currentUser?.let {
            registerButton.visibility = View.GONE
            welcomeTextView.visibility = View.VISIBLE
        } ?: run {
            registerButton.visibility = View.VISIBLE
            welcomeTextView.visibility = View.GONE
        }
    }

    private fun saveData() {
        val sharedPreferences = activity!!.getSharedPreferences("test", Context.MODE_PRIVATE)
        sharedPreferences.edit()
            .putString("firstKey", "value")
            .apply()
    }
}