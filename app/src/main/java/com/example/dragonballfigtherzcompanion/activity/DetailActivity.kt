package com.example.dragonballfigtherzcompanion.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.example.dragonballfigtherzcompanion.R
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.dragonballfigtherzcompanion.adapter.NewNewsAdapter
import com.example.dragonballfigtherzcompanion.fragments.*
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.material.bottomnavigation.BottomNavigationView

class DetailActivity(/*val newId: String?*/) : AppCompatActivity() {

    private lateinit var newNewsAdapter: NewNewsAdapter

    private lateinit var  swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView

    override fun onStart()
    {
        super.onStart()
        initListeners()
    }

    override fun onResume()
    {
        super.onResume()
        initListeners()
    }

    override fun onDestroy()
    {
        super.onDestroy()
    }

    private fun initViews(view: View)
    {
        recyclerView = view.findViewById(R.id.recyclerView)
    }

    private fun initListeners() {
        /*recyclerView.setOnClickListener() {
            startActivity(Intent(this, DetailActivity::class.java))
        }*/
    }

    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_extended_news)
        initViews(this.recyclerView)


        // Get Fragment Container Reference
        val fragmentContainer: FrameLayout = findViewById(R.id.fragmentContainer)
    }*/

    internal fun loadNewsScreen(newId: String?){
        /*val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, NewNewsFragment(newId))
        transaction.commit()*/

        /*val intent = Intent(this, DetailActivity::class.java)
        startActivity(intent)*/
    }
}