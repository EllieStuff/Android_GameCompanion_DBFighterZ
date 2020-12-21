package com.example.dragonballfigtherzcompanion

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.example.dragonballfigtherzcompanion.fragments.ChatFragment
import com.example.dragonballfigtherzcompanion.fragments.ListOfChatsFragment
import com.example.dragonballfigtherzcompanion.fragments.ProfileFragment
import com.example.dragonballfigtherzcompanion.fragments.NewsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

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
    companion object {
        val instance = MainActivity()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

        }

        //return@setOnNavigationItemSelectedListener true
        true
    }

    // Select Initial Tab
    bottomNavigationView.selectedItemId = R.id.newsTab

    }

    public fun showMessage(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }
}