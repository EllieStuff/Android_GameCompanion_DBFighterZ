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
import com.example.dragonballfigtherzcompanion.Constants.COLLECTION_NEWS
import com.example.dragonballfigtherzcompanion.Constants
import com.example.dragonballfigtherzcompanion.R
import com.example.dragonballfigtherzcompanion.adapter.NewsAdapter
import com.example.dragonballfigtherzcompanion.model.Chat
import com.example.dragonballfigtherzcompanion.model.News
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class NewsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var searchButton: Button
    private lateinit var optionsButton: Button
    private lateinit var  swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var firestore: FirebaseFirestore

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
        searchButton = view.findViewById(R.id.searchButton)
        optionsButton = view.findViewById(R.id.optionsButton)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState);
        // TODO: Init
        firestore = Firebase.firestore
        initViews(view)
        initRecyclerView()
    }

    private fun initRecyclerView(){
        // Layout Manager
        var layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager

        // Adapter
        newsAdapter = NewsAdapter(newsList = listOf(News("Chat 0"), News("Chat 1"), News("Chat 2"), News("Chat 3"),
                News("Chat 4"), News("Chat 5"), News("Chat 6"), News("Chat 7"), News("Chat 8"), News("Chat 9")))
        recyclerView.adapter = newsAdapter

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