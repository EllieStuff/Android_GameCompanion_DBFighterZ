package com.example.dragonballfigtherzcompanion.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dragonballfigtherzcompanion.activity.DetailActivity
import com.example.dragonballfigtherzcompanion.fragments.NewsFragment
import com.example.dragonballfigtherzcompanion.fragments.NewNewsFragment
import com.example.dragonballfigtherzcompanion.R
import com.example.dragonballfigtherzcompanion.model.News
import android.util.Log

class NewNewsAdapter(var newsList: List<News>): RecyclerView.Adapter<NewNewsAdapter.NewsViewHolder>() {

    // Inflate view (xml layout) => ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val newsView: View = LayoutInflater.from(parent.context).inflate(R.layout.item_news_extended, parent, false)

        //Log.d("HOLDER", "NO")

        return NewsViewHolder(newsView)
    }

    // Update view for position
    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val news = newsList[position]

        holder.usernameTextView.text = news.user_name
        holder.victoryTextView.text = news.victory
        holder.favCharTextView.text = "Fav. Char: " + news.fav_char
        holder.rankTextView.text = "Rank: " + news.rank
        holder.victoryRateTextView.text = "Victory Rate: " + news.victory_rate.toString() + "%"
        holder.rankingTextView.text = "Ranking: " + news.ranking.toString()
        holder.playTimeTextView.text = "Play Time: " + news.play_time.toString() + "h"
        holder.maxComboTextView.text = "Max. Combo: " + news.max_combo.toString()

        /*holder.clickableListOfNews.setOnClickListener {
            Log.d("si", "clique")
        }*/
    }

    // Total items
    override fun getItemCount(): Int {
        return newsList.count()
    }


    // Maps view xml => Kotlin
    inner class NewsViewHolder(view: View): RecyclerView.ViewHolder(view) {
        //val clickableListOfNews: LinearLayout = view.findViewById(R.id.clickableListOfNews);
        val usernameTextView: TextView = view.findViewById(R.id.usernameTextView);
        val victoryTextView: TextView = view.findViewById(R.id.victoryTextView);
        val favCharTextView: TextView = view.findViewById(R.id.favCharTextView);
        val rankTextView: TextView = view.findViewById(R.id.rankTextView);
        val victoryRateTextView: TextView = view.findViewById(R.id.victoryRateTextView);
        val rankingTextView: TextView = view.findViewById(R.id.rankingTextView);
        val playTimeTextView: TextView = view.findViewById(R.id.playTimeTextView);
        val maxComboTextView: TextView = view.findViewById(R.id.maxComboTextView);

    }
}