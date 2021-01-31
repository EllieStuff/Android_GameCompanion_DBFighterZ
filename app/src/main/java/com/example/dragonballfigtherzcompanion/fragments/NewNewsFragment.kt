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
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.dragonballfigtherzcompanion.Constants
import com.example.dragonballfigtherzcompanion.R
import com.example.dragonballfigtherzcompanion.activity.DetailActivity
import com.example.dragonballfigtherzcompanion.adapter.NewNewsAdapter
import com.example.dragonballfigtherzcompanion.model.News
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

import com.google.firebase.firestore.ktx.firestore


class NewNewsFragment(val userName: String, val victory: String, val fav_char: String, val rank: String, val victory_rate: String, val ranking: String, val play_time: String, val max_combo: String) : Fragment() {

    private lateinit var  swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var newNewsAdapter: NewNewsAdapter

    private lateinit var firestore: FirebaseFirestore
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    //private lateinit var newNewsAdapter: NewNewsAdapter
    private lateinit var newsList: List<News>

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // ULTIMO
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        // PRIMERO

        var tmp_v = inflater.inflate(R.layout.fragment_extended_news, container, false)

        newsList = listOf( News(userName, victory, rank, fav_char, 1, 2, 3, 4) )
        recyclerView.adapter = NewNewsAdapter(newsList)

        return tmp_v
    }

    private fun initViews(view: View) {
        recyclerView = view.findViewById(R.id.recyclerViewDetails)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState);

        // SEGUNDO

        initViews(view)
        initRecyclerView()

        /// LLEGA HASTA AQUI
    }

    private fun onCreateViewHolder() {
        //recyclerView.adapter = newNewsAdapter
    }

    private fun initRecyclerView() {
        var layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager
        //recyclerView.adapter = newNewsAdapter
    }


}