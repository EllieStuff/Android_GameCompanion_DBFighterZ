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
import com.example.dragonballfigtherzcompanion.R
import com.example.dragonballfigtherzcompanion.adapter.NewsAdapter
import com.example.dragonballfigtherzcompanion.model.News
import com.google.firebase.firestore.FirebaseFirestore

import com.google.firebase.database.DatabaseReference
import com.google.firebase.ktx.Firebase
//import com.google.firebase.referencecode.database.R

import com.google.firebase.firestore.ktx.firestore


class NewsFragment : Fragment() {

    private lateinit var newsAdapter: NewsAdapter

    private lateinit var searchButton: Button
    private lateinit var optionsButton: Button

    private lateinit var  swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView

    private lateinit var firestore: FirebaseFirestore

    private lateinit var dataBaseRef: DatabaseReference
    //private lateinit var dataBase: FirebaseDatabase

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    private fun initViews(view: View) {
        recyclerView = view.findViewById(R.id.recyclerView)
        //searchButton = view.findViewById(R.id.searchButton)
        //optionsButton = view.findViewById(R.id.optionsButton)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState);
        // TODO: Init
        firestore = Firebase.firestore
        initViews(view)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        // Layout Manager
        var layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager

        //val database_ = Firebase.database
        //dataBaseRef = database_.getReference().child("Tomodachi")

        var names = mutableListOf<String>()
        var rank = mutableListOf<String>()
        var fav_char = mutableListOf<String>()
        var victory = mutableListOf<String>()
        var ranking = mutableListOf<Int>()
        var victory_rate = mutableListOf<Int>()
        var play_time = mutableListOf<Int>()
        var max_combo = mutableListOf<Int>()

        firestore.collection("news").get().addOnSuccessListener { result->
            for (document in result)
            {
                names.add(document.data["Name"].toString())
                rank.add(document.data["Rank"].toString())
                fav_char.add(document.data["Fav_Char"].toString())
                victory.add(document.data["Victory"].toString())
                ranking.add(document.data["Ranking"].toString().toInt())
                victory_rate.add(document.data["Victory_Rate"].toString().toInt())
                play_time.add(document.data["Play_Time"].toString().toInt())
                max_combo.add(document.data["Max_Combo"].toString().toInt())
            }

            // Adapter
            newsAdapter = NewsAdapter(newsList = listOf(News(names[0], rank[0], fav_char[0], victory[0], ranking[0]), News(names[1]), News(names[2]), News(names[3]), News(names[4]), News(names[5])))
            recyclerView.adapter = newsAdapter

        }.addOnFailureListener { exception ->
            // Error
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