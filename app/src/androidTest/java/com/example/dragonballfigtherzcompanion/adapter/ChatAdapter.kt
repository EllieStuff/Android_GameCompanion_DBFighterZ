package com.example.dragonballfigtherzcompanion.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dragonballfigtherzcompanion.R
import com.example.dragonballfigtherzcompanion.model.Chat

class ChatAdapter(var chatList: List<Chat>): RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    // Inflate view (xml layout) => ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val chatView: View = LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent, false)

        return ChatViewHolder(chatView)
    }

    // Total items
    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.messageTextView.text = chatList[position].message
    }

    // Update view for position
    override fun getItemCount(): Int {
        return chatList.count()
    }


    // Maps view xml => Kotlin
    inner class ChatViewHolder(view: View): RecyclerView.ViewHolder(view){
        val messageTextView: TextView = view.findViewById(R.id.messageTextView);
    }

}