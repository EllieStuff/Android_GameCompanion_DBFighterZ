package com.example.dragonballfigtherzcompanion.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dragonballfigtherzcompanion.R
import com.example.dragonballfigtherzcompanion.model.News

class NewNewsAdapter(var newsList: List<News>): RecyclerView.Adapter<NewNewsAdapter.NewNewsViewHolder>() {

    // Inflate view (xml layout) => ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewNewsViewHolder {
        val nNewsView: View = LayoutInflater.from(parent.context).inflate(R.layout.item_news_extended, parent, false)
        return NewNewsViewHolder(nNewsView)
    }

    // Update view for position
    override fun onBindViewHolder(holder: NewNewsViewHolder, position: Int) {
        val news = newsList[position]
        holder.usernameTextView.text = news.user_name
        holder.rankTextView.text = "Rank: " + news.rank
        holder.favCharTextView.text = "Fav. Char: " + news.fav_char
        holder.victoryTextView.text = news.victory
        holder.victoryRateTextView.text = "Victory Rate: " + news.victory_rate.toString() + "%"
        holder.rankingTextView.text = "Ranking: " + news.ranking.toString()
        holder.playTimeTextView.text = "Play Time: " + news.play_time.toString() + "h"
        holder.maxComboTextView.text = "Max. Combo: " + news.max_combo.toString()
    }

    // Total items
    override fun getItemCount(): Int {
        return newsList.count()
    }


    // Maps view xml => Kotlin
    inner class NewNewsViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val usernameTextView: TextView = view.findViewById(R.id.usernameTextView);
        val rankTextView: TextView = view.findViewById(R.id.rankTextView);
        val favCharTextView: TextView = view.findViewById(R.id.favCharTextView);
        val victoryTextView: TextView = view.findViewById(R.id.victoryTextView);
        val rankingTextView: TextView = view.findViewById(R.id.rankingTextView);
        val victoryRateTextView: TextView = view.findViewById(R.id.victoryRateTextView);
        val playTimeTextView: TextView = view.findViewById(R.id.playTimeTextView);
        val maxComboTextView: TextView = view.findViewById(R.id.maxComboTextView);
    }
}