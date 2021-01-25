package com.example.dragonballfigtherzcompanion.fragments

import android.content.Intent
import android.os.Bundle
import android.os.UserManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.dragonballfigtherzcompanion.R
import com.example.dragonballfigtherzcompanion.activity.TwitchLoginActivity
import com.example.dragonballfigtherzcompanion.model.StreamsViewModel

class StreamsFragment: Fragment() {

    private lateinit var twitchLoginButton: Button
    private val streamsViewModel by lazy { StreamsViewModel(com.example.dragonballfigtherzcompanion.services.UserManager(requireContext())) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //Log.i(TAG, "++ onCreateView ++")
        return inflater.inflate(R.layout.fragment_streams, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        initListeners()
        //initObservers()
    }

    override fun onResume() {
        super.onResume()
        streamsViewModel.checkUserAvailability()
    }

    private fun initViews(view: View) {
        twitchLoginButton = view.findViewById(R.id.twitchLoginButton)
    }

    private fun initListeners() {
        twitchLoginButton.setOnClickListener {
            startActivity(Intent(requireActivity(), TwitchLoginActivity::class.java))
        }
    }

    /* // No va la bool isLoggedIn
    private fun initObservers() {
        streamsViewModel.isLoggedIn.observe(viewLifecycleOwner) { isLoggedIn: Boolean ->
            if(isLoggedIn) {
                twitchLoginButton.visibility = View.GONE
            }
            else {
                twitchLoginButton.visibility = View.VISIBLE
            }
        }
    }
     */
}