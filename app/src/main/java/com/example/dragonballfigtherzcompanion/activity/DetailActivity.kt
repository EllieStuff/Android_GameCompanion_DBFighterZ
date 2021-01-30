package com.example.dragonballfigtherzcompanion.activity

import androidx.appcompat.app.AppCompatActivity
import com.example.dragonballfigtherzcompanion.R
import android.os.Bundle
import android.view.View
import android.util.Log
import android.view.ViewParent
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.dragonballfigtherzcompanion.adapter.NewNewsAdapter
import com.example.dragonballfigtherzcompanion.adapter.NewsAdapter
import com.example.dragonballfigtherzcompanion.model.News
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class DetailActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    public lateinit var userName: String

    private lateinit var usernameTextView: TextView

    override fun onCreate (savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_extended_news)
        initViews()
        initListeners()
        initRecyclerView()

        userName = getIntent().getStringExtra("userName").toString();

    }

    private fun initViews()
    {
        //usernameTextView = findViewById<TextView>(R.id.usernameTextView)
    }

    private fun initListeners()
    { }

    private fun initRecyclerView() {
        // Layout Manager
        var layoutManager = LinearLayoutManager(this)
        //recyclerView.layoutManager = layoutManager
    }

    private fun initViews(view: View)
    {
        recyclerView = view.findViewById(R.id.recyclerView)
    }
}