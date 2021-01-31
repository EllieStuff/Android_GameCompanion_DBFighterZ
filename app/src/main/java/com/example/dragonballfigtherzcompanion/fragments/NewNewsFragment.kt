package com.example.dragonballfigtherzcompanion.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.dragonballfigtherzcompanion.Constants
import com.example.dragonballfigtherzcompanion.R
import com.example.dragonballfigtherzcompanion.activity.DetailActivity
import com.example.dragonballfigtherzcompanion.adapter.NewNewsAdapter
import com.example.dragonballfigtherzcompanion.model.News
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

import com.google.firebase.firestore.ktx.firestore


class NewNewsFragment(val userName: String?, var newNewsAdapter: NewNewsAdapter) : Fragment() {

    private lateinit var  swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView

    private lateinit var firestore: FirebaseFirestore
    private lateinit var firebaseAnalytics: FirebaseAnalytics


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_extended_news, container, false)
    }

    private fun initViews(view: View) {
        recyclerView = view.findViewById(R.id.recyclerViewDetails)
        var layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState);

        firestore = Firebase.firestore
        firebaseAnalytics = Firebase.analytics

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
            if(userName=="Antonio") {
                newNewsAdapter = NewNewsAdapter(
                        newsList = listOf(
                                News(names[0], victory[0],  rank[0], fav_char[0], ranking[0], victory_rate[0], play_time[0], max_combo[0])
                        ))
            }
            else if(userName=="Francis") {
                newNewsAdapter = NewNewsAdapter(
                        newsList = listOf(
                                News(names[1], victory[1], rank[1], fav_char[1], ranking[1], victory_rate[1], play_time[1], max_combo[1])
                        ))
            }
            else if(userName=="Josefa") {
                newNewsAdapter = NewNewsAdapter(
                        newsList = listOf(
                                News(names[2], victory[2], rank[2], fav_char[2], ranking[2], victory_rate[2], play_time[2], max_combo[2])
                        ))
            }
            else if(userName=="Jose") {
                newNewsAdapter = NewNewsAdapter(
                        newsList = listOf(
                                News(names[3], victory[3], rank[3], fav_char[3], ranking[3], victory_rate[3], play_time[3], max_combo[3])
                        ))
            }
            else if(userName=="Pepe") {
                newNewsAdapter = NewNewsAdapter(
                        newsList = listOf(
                                News(names[4], victory[4], rank[4], fav_char[4], ranking[4], victory_rate[4], play_time[4], max_combo[4])
                        ))
            }
            else if(userName=="Viktor") {
                newNewsAdapter = NewNewsAdapter(
                        newsList = listOf(
                                News(names[5], victory[5], rank[5], fav_char[5], ranking[5], victory_rate[5], play_time[5], max_combo[5])
                        ))
            }

            Log.d("TAG", userName.toString())

            firebaseAnalytics.logEvent("checkActivity", null)

        }.addOnFailureListener { exception ->
            // Error
            firebaseAnalytics.logEvent("failedToCheck", null)
        }

        initViews(view)
        initRecyclerView()

        /// LLEGA HASTA AQUI
    }

    fun startActivity(un: String) {
        val intent = Intent(activity, DetailActivity()::class.java)
        intent.putExtra("userName", un);
        startActivity(intent)
    }

    private fun initRecyclerView() {

        Log.d("a", "iniciando recycler")

        recyclerView.adapter = newNewsAdapter
    }


}