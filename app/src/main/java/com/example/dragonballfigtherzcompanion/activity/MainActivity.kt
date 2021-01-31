package com.example.dragonballfigtherzcompanion.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.example.dragonballfigtherzcompanion.R
import com.example.dragonballfigtherzcompanion.fragments.*
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    //var onSubMenu = false;

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    // Singleton de MainActivity
    /*companion object {
        private lateinit var instance: MainActivity

        val managerInstance: MainActivity
            get() {
                if (instance == null) {
                    instance = MainActivity()
                }

                return instance
            }
    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Find views
        val bottomNavigationView: BottomNavigationView =
            findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        // Get Fragment Container Reference

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

        // Select Initial Tab
        bottomNavigationView.selectedItemId = R.id.newsTab


        // Init AdMob
        MobileAds.initialize(this)
        // Load Ad
        val adView = findViewById<AdView>(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)

    }

    internal fun loadChatScreen(chatId: String){
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, ChatFragment(chatId))
        transaction.commit()
    }

    /*internal fun loadNewsScreen(newId: String?){
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, NewNewsFragment(newId))
        transaction.commit()
    }*/

    public fun showMessage(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }
}