package com.example.dragonballfigtherzcompanion.activity

import androidx.appcompat.app.AppCompatActivity
import com.example.dragonballfigtherzcompanion.R
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.dragonballfigtherzcompanion.adapter.NewNewsAdapter

class DetailActivity() : AppCompatActivity() { // val userName: String?

    private lateinit var newNewsAdapter: NewNewsAdapter

    private lateinit var  swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView

    override fun onCreate (savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_news_extended)
        //Init Views
        initViews()
        //Init listeners
        initListeners()
    }

    private fun initViews()
    {
    }

    private fun initListeners()
    {
    }

    private fun initViews(view: View)
    {
        recyclerView = view.findViewById(R.id.recyclerView)
    }
}