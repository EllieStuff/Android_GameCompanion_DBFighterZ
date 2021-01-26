package com.example.dragonballfigtherzcompanion.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.dragonballfigtherzcompanion.Constants
import com.example.dragonballfigtherzcompanion.MainActivity
import com.example.dragonballfigtherzcompanion.R
import com.example.dragonballfigtherzcompanion.adapter.NewsAdapter
import com.example.dragonballfigtherzcompanion.adapter.NewNewsAdapter
import com.example.dragonballfigtherzcompanion.model.News
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.firestore.FirebaseFirestore

import com.google.firebase.database.DatabaseReference
import com.google.firebase.ktx.Firebase

import com.google.firebase.firestore.ktx.firestore


class NewsFragment : Fragment() {

    private lateinit var newsAdapter: NewsAdapter

    private lateinit var searchButton: Button
    private lateinit var optionsButton: Button

    private lateinit var  swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView

    private lateinit var firestore: FirebaseFirestore
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    private lateinit var dataBaseRef: DatabaseReference

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    private fun initViews(view: View) {
        recyclerView = view.findViewById(R.id.recyclerView)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState);
        // TODO: Init
        firestore = Firebase.firestore
        firebaseAnalytics = Firebase.analytics
        initViews(view)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        // Layout Manager
        var layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager

        var names = mutableListOf<String>()
        var victory = mutableListOf<String>()

        firestore.collection("news").get().addOnSuccessListener { result->
            for (document in result)
            {
                names.add(document.data["Name"].toString())
                victory.add(document.data["Victory"].toString())
            }

            // Adapter
            newsAdapter = NewsAdapter(
                    newsList = listOf(
                            News(names[0], victory[0]),
                            News(names[1], victory[1]),
                            News(names[2], victory[2]),
                            News(names[3], victory[3]),
                            News(names[4], victory[4]),
                            News(names[5], victory[5])
                    ), activity = (activity as MainActivity))
            recyclerView.adapter = newsAdapter

            firebaseAnalytics.logEvent("checkActivity", null)

        }.addOnFailureListener { exception ->
            // Error
            firebaseAnalytics.logEvent("failedToCheck", null)
        }


    }

    private fun getNews() {
        //TODO: Sort
        swipeRefreshLayout.isRefreshing = true
        firestore.collection(Constants.COLLECTION_NEWS)
                .get()

                .addOnCompleteListener {
                    if(it.isSuccessful){
                        // Update UI
                        val news: List<News> = it.result?.documents?.mapNotNull{ it.toObject(News::class.java) }.orEmpty()
                        newsAdapter.newsList = news
                        newsAdapter.notifyDataSetChanged()
                    } else {
                        // TODO: Show Error
                    }
                    swipeRefreshLayout.isRefreshing = false
                }
    }
}