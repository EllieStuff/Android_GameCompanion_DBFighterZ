package com.example.dragonballfigtherzcompanion.activity

import androidx.appcompat.app.AppCompatActivity
import com.example.dragonballfigtherzcompanion.R
import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.example.dragonballfigtherzcompanion.adapter.NewNewsAdapter
import com.example.dragonballfigtherzcompanion.fragments.NewNewsFragment
import com.example.dragonballfigtherzcompanion.model.News

class DetailActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var newNewsAdapter: NewNewsAdapter
    private lateinit var newsList: List<News>

    public lateinit var userName: String
    public lateinit var victory: String
    public lateinit var fav_char: String
    public lateinit var rank: String
    public lateinit var victory_rate: String
    public lateinit var ranking: String
    public lateinit var play_time: String
    public lateinit var max_combo: String

    /*override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
    }*/

    override fun onCreate (savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_extended_news)

        val what: RecyclerView = findViewById<RecyclerView>(R.id.recyclerViewDetails)


        userName = getIntent().getStringExtra("userName").toString();
        victory = getIntent().getStringExtra("victory").toString();
        rank = getIntent().getStringExtra("fav_char").toString();
        fav_char = getIntent().getStringExtra("rank").toString();
        //ranking = getIntent().getStringExtra("victory_rate").toString();
        //victory_rate = getIntent().getStringExtra("ranking").toString();
        //play_time = getIntent().getStringExtra("play_time").toString();
        //max_combo = getIntent().getStringExtra("max_combo").toString();

        //Log.d("USERNAME", userName);
        //Log.d("VICTORY", victory);
        //Log.d("RANK", rank);
        //Log.d("FAV_CHAR", fav_char);
        //Log.d("RANKING", ranking);
        //Log.d("VICTORY", victory_rate);
        //Log.d("PLAY_TIME", play_time);
        //Log.d("COMBO", max_combo);

        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.detailsContainer, NewNewsFragment(userName, victory, rank, fav_char, "ranking", "victory_rate", "play_time", "max_combo")) // NewNewsFragment(userName)
        transaction.commit()

    }
}