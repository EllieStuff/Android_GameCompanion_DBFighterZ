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

    // Update view for position
    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chat = chatList[position]
        holder.messageTextView.text = chat.message
        holder.usernameTextView.text = chat.username
        //TODO: Format Date
        holder.dateTextView.text = chat.sentAt.toString()
    }

    // Total items
    override fun getItemCount(): Int {
        return chatList.count()
    }


    // Maps view xml => Kotlin
    inner class ChatViewHolder(view: View): RecyclerView.ViewHolder(view){
        val messageTextView: TextView = view.findViewById(R.id.messageTextView);
        val usernameTextView: TextView = view.findViewById(R.id.usernameTextView);
        val dateTextView: TextView = view.findViewById(R.id.messageDateTextView);
    }

}