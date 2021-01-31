package com.example.dragonballfigtherzcompanion.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dragonballfigtherzcompanion.R
import com.example.dragonballfigtherzcompanion.activity.DetailActivity
import com.example.dragonballfigtherzcompanion.adapter.NewNewsAdapter
import com.example.dragonballfigtherzcompanion.model.News
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.FirebaseFirestore

import com.google.firebase.firestore.ktx.firestore


class NewNewsFragment(
        val userName: String,
        val victory: String,
        val fav_char: String,
        val rank: String,
        val victory_rate: String,
        val ranking: String,
        val play_time: String,
        val max_combo: String)
    : Fragment() {

    private lateinit var recyclerView: RecyclerView

    private lateinit var newNewsAdapter: NewNewsAdapter

    private lateinit var newsList: List<News>

    private lateinit var firestore: FirebaseFirestore

    /*override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }*/

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        // PRIMERO

        var tmp_v = inflater.inflate(R.layout.fragment_extended_news, container, false)

        return tmp_v
    }

    private fun initViews(view: View) {
        recyclerView = view.findViewById(R.id.recyclerViewDetails)
        //Log.d("FRAGMENT", "YES")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // SEGUNDO
        super.onViewCreated(view, savedInstanceState);
        firestore = Firebase.firestore
        initViews(view)
        initRecyclerView()

    }

    private fun initRecyclerView() {
        var layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager

        newsList = listOf( News(userName, victory, rank, fav_char, 1, 2, 3, 4) )
        newNewsAdapter = NewNewsAdapter(newsList)

        recyclerView.adapter = newNewsAdapter
    }

}