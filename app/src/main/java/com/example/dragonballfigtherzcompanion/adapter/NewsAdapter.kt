package com.example.dragonballfigtherzcompanion.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dragonballfigtherzcompanion.activity.DetailActivity
import com.example.dragonballfigtherzcompanion.activity.MainActivity
import com.example.dragonballfigtherzcompanion.fragments.NewsFragment
import com.example.dragonballfigtherzcompanion.fragments.NewNewsFragment
import com.example.dragonballfigtherzcompanion.R
import com.example.dragonballfigtherzcompanion.model.News
import android.util.Log

class NewsAdapter(var newsList: List<News>, var activity: DetailActivity, var fragment: NewsFragment): RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    // Inflate view (xml layout) => ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val newsView: View = LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)

        return NewsViewHolder(newsView)
    }

    // Update view for position
    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val news = newsList[position]


        holder.usernameTextView.text = news.user_name
        holder.victoryTextView.text = news.victory

        holder.clickableListOfNews.setOnClickListener {
            //activity.loadNewsScreen(news.user_name) // FUNCION QUE ABRE EL
            Log.d("name", news.user_name.toString()) // AQUI DETECTO QUE BOTON PULSO
            fragment.startActivity()
        }
    }

    // Total items
    override fun getItemCount(): Int {
        return newsList.count()
    }


    // Maps view xml => Kotlin
    inner class NewsViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val usernameTextView: TextView = view.findViewById(R.id.usernameTextView);
        val victoryTextView: TextView = view.findViewById(R.id.victoryTextView);
        val clickableListOfNews: LinearLayout = view.findViewById(R.id.clickableListOfNews);
    }

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