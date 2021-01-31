package com.example.dragonballfigtherzcompanion.activity

import androidx.appcompat.app.AppCompatActivity
import com.example.dragonballfigtherzcompanion.R
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dragonballfighterzcompanion.ActivityFragment
import com.example.dragonballfigtherzcompanion.adapter.NewNewsAdapter
import com.example.dragonballfigtherzcompanion.fragments.ListOfChatsFragment
import com.example.dragonballfigtherzcompanion.fragments.NewsFragment
import com.example.dragonballfigtherzcompanion.fragments.NewNewsFragment
import com.example.dragonballfigtherzcompanion.fragments.ProfileFragment
import com.example.dragonballfigtherzcompanion.model.News

class DetailActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    public lateinit var userName: String

    private lateinit var usernameTextView: TextView

    private lateinit var newsList: List<News>

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onCreate (savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_extended_news)

        userName = getIntent().getStringExtra("userName").toString();

        newsList = listOf( News(userName, "You Win", "-", "You", 0, 1, 0, 1) )

        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.detailsContainer, NewNewsFragment(userName)) // NewNewsFragment(userName)
        transaction.commit()
    }

    private fun initViews(view: View)
    {
        recyclerView = view.findViewById(R.id.recyclerView)
    }

    private fun initListeners()
    { }
}