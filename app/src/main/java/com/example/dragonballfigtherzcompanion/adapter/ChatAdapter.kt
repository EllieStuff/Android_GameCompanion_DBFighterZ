package com.example.dragonballfigtherzcompanion.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dragonballfigtherzcompanion.Constants
import com.example.dragonballfigtherzcompanion.R
import com.example.dragonballfigtherzcompanion.model.Chat
import com.example.dragonballfigtherzcompanion.model.Message
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ChatAdapter(var messageList: List<Message>): RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    // Inflate view (xml layout) => ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val chatView: View = LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent, false)

        return ChatViewHolder(chatView)
    }

    // Update view for position
    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val message = messageList[position]

        Firebase.auth.currentUser?.uid?.let { userId: String ->
            if(message.from == userId){
                holder.cardViewFromOther.visibility = View.GONE

                holder.usernameTextViewFromThis.text = message.username //TODO: Pillar el nom en contres de la id
                holder.messageTextViewFromThis.text = message.text
                //holder.messageDateTextViewFromThis.text = message.date.toString()
            }
            else{
                holder.cardViewFromThis.visibility = View.GONE

                holder.usernameTextViewFromOther.text = message.username //TODO: Pillar el nom en contres de la id
                holder.messageTextViewFromOther.text = message.text
                //holder.messageDateTextViewFromOther.text = message.date.toString()
            }

        } ?: run {
            // Error
        }


        /*
        holder.messageTextView.text = chat.message
        holder.usernameTextView.text = chat.username
        //TODO: Format Date
        holder.dateTextView.text = chat.sentAt.toString()
         */
    }

    // Total items
    override fun getItemCount(): Int {
        return messageList.count()
    }


    // Maps view xml => Kotlin
    inner class ChatViewHolder(view: View): RecyclerView.ViewHolder(view){
        // This
        val cardViewFromThis: androidx.cardview.widget.CardView = view.findViewById(R.id.cardViewFromThis)
        val usernameTextViewFromThis: TextView = view.findViewById(R.id.usernameTextViewFromThis)
        val messageTextViewFromThis: TextView = view.findViewById(R.id.messageTextViewFromThis)
        //val messageDateTextViewFromThis: TextView = view.findViewById(R.id.messageDateTextViewFromThis)

        // Other
        val cardViewFromOther: androidx.cardview.widget.CardView = view.findViewById(R.id.cardViewFromOther)
        val usernameTextViewFromOther: TextView = view.findViewById(R.id.usernameTextViewFromOther)
        val messageTextViewFromOther: TextView = view.findViewById(R.id.messageTextViewFromOther)
        //val messageDateTextViewFromOther: TextView = view.findViewById(R.id.messageDateTextViewFromOther)
    }

}