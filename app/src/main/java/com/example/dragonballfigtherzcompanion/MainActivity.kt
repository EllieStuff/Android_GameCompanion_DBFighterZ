package com.example.dragonballfigtherzcompanion

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView: BottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val testText: TextView = findViewById(R.id.testTextView)

        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem: MenuItem ->
            when(menuItem.itemId){  //Es un 'switch'
                R.id.newsTab->{
                    // TODO: News
                    testText.text = "NEWS"
                }

                R.id.chatTab->{
                    // TODO: Chat
                    testText.text = "CHAT"
                }

                R.id.activityTab->{
                    // TODO: Recent Activity
                    testText.text = "ACTIVITY"
                }

                R.id.homeTab->{
                    // TODO: Home
                    testText.text = "HOME"
                }
            }

            true
        }

        // Select Initial Tab
        bottomNavigationView.selectedItemId = R.id.newsTab

    }
}