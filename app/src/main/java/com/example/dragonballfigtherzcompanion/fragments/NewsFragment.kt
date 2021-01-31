package com.example.dragonballfigtherzcompanion.fragments

import android.content.Intent
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
import com.example.dragonballfigtherzcompanion.activity.MainActivity
import com.example.dragonballfigtherzcompanion.activity.DetailActivity
import com.example.dragonballfigtherzcompanion.R
import com.example.dragonballfigtherzcompanion.adapter.NewsAdapter
import com.example.dragonballfigtherzcompanion.fragments.NewNewsFragment
import com.example.dragonballfigtherzcompanion.model.News
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

import com.google.firebase.firestore.ktx.firestore


class NewsFragment() : Fragment() {

    private lateinit var newsAdapter: NewsAdapter

    private lateinit var  swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView

    private lateinit var firestore: FirebaseFirestore
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    private fun initViews(view: View) {
        recyclerView = view.findViewById(R.id.recyclerViewNews)
        //Log.d("FRAGMENT", "YES")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState);
        // TODO: Init
        firestore = Firebase.firestore
        firebaseAnalytics = Firebase.analytics
        initViews(view)
        initRecyclerView()
    }

    private fun initRecyclerView() {

        // Layout Manager
        var layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager

        var names        = mutableListOf<String>()
        var rank           = mutableListOf<String>()
        var fav_char       = mutableListOf<String>()
        var victory       = mutableListOf<String>()
        var ranking       = mutableListOf<Int>()
        var victory_rate    = mutableListOf<Int>()
        var play_time     = mutableListOf<Int>()
        var max_combo       = mutableListOf<Int>()

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
            newsAdapter = NewsAdapter(
                    newsList = listOf(
                            News(names[0], victory[0], fav_char[0], rank[0], victory_rate[0], ranking[0], play_time[0], max_combo[0]),
                            News(names[1], victory[1], fav_char[1], rank[1], victory_rate[1], ranking[1], play_time[1], max_combo[1]),
                            News(names[2], victory[2], fav_char[2], rank[2], victory_rate[2], ranking[2], play_time[2], max_combo[2]),
                            News(names[3], victory[3], fav_char[3], rank[3], victory_rate[3], ranking[3], play_time[3], max_combo[3]),
                            News(names[4], victory[4], fav_char[4], rank[4], victory_rate[4], ranking[4], play_time[4], max_combo[4]),
                            News(names[5], victory[5], fav_char[5], rank[5], victory_rate[5], ranking[5], play_time[5], max_combo[5])
                    ), DetailActivity() /*("lol")*/, this) // newNewsFragment

            recyclerView.adapter = newsAdapter

            firebaseAnalytics.logEvent("checkActivity", null)

        }.addOnFailureListener { exception ->
            // Error
            firebaseAnalytics.logEvent("failedToCheck", null)
        }

        /*recyclerView.setOnClickListener {
            Log.d("TAG", "CLICKEDDD")
        }*/
    }

    fun startActivity(userName: String, victory: String, fav_char: String, rank: String, victory_rate: Int?, ranking: Int?, play_time: Int?, max_combo: Int?) {
        val intent = Intent(activity, DetailActivity()::class.java)
        intent.putExtra("userName", userName);
        intent.putExtra("victory", victory)
        intent.putExtra("fav_char", fav_char)
        intent.putExtra("rank", rank)
        //intent.putExtra("victory_rate", victory_rate)
        //intent.putExtra("ranking", ranking)
        //intent.putExtra("play_time", play_time)
        //intent.putExtra("max_combo", max_combo)
        startActivity(intent)
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