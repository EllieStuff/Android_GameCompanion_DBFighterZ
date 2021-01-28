package com.example.dragonballfigtherzcompanion.activity

import androidx.appcompat.app.AppCompatActivity
import com.example.dragonballfigtherzcompanion.R
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.example.dragonballfigtherzcompanion.fragments.*
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.material.bottomnavigation.BottomNavigationView

class DetailActivity : AppCompatActivity() {

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    internal fun loadNewsScreen(newId: String?){
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, NewNewsFragment(newId))
        transaction.commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_activity)

        // Find views
        val bottomNavigationView: BottomNavigationView =
                findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        // Get Fragment Container Reference
        val fragmentContainer: FrameLayout = findViewById(R.id.fragmentContainer)

        // Listen to Tabs Selected
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem: MenuItem ->

            when (menuItem.itemId) {
                R.id.chatTab -> {
                    //Add Chat Fragment
                    val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.fragmentContainer, ListOfChatsFragment())
                    transaction.commit()
                }
                R.id.newsTab -> {
                    //Add News Fragment
                    val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.fragmentContainer, NewsFragment())
                    transaction.commit()
                }
                R.id.userTab -> {
                    //Add Profile Fragment
                    val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.fragmentContainer, ProfileFragment())
                    transaction.commit()
                }
                R.id.twitchTab -> {
                    //Add Profile Fragment
                    val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.fragmentContainer, StreamsFragment())
                    transaction.commit()
                }

            }

            //return@setOnNavigationItemSelectedListener true
            true
        }

    }
}