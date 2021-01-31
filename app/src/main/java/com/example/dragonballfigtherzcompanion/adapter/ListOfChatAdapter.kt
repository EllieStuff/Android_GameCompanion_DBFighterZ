package com.example.dragonballfigtherzcompanion.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dragonballfigtherzcompanion.activity.MainActivity
import com.example.dragonballfigtherzcompanion.R
import com.example.dragonballfigtherzcompanion.model.Chat

class ListOfChatAdapter(var chatList: List<Chat>, var activity: MainActivity): RecyclerView.Adapter<ListOfChatAdapter.ListOfChatViewHolder>() {
    // Inflate view (xml layout) => ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListOfChatViewHolder {
        val chatView: View = LayoutInflater.from(parent.context).inflate(R.layout.item_listofchats, parent, false)

        return ListOfChatViewHolder(chatView)
    }

    // Update view for position
    override fun onBindViewHolder(holder: ListOfChatViewHolder, position: Int) {
        val chat = chatList[position]
        holder.chatNameTextView.text = chat.name
        if(chat.messagesToRead == 0)
            holder.notReadedMessagesTextView.text = ""
        else
            holder.notReadedMessagesTextView.text = chat.messagesToRead.toString()
        //holder.lastMessageTextView.text = chat.username
        //TODO: Format Date

        holder.clickableListOfChatsLayout.setOnClickListener {

            /*
            Firebase.firestore.collection(Constants.COLLECTION_CHAT)
                    .document(chat.id)
                    .set(Chat(
                            id = chat.id,
                            name = chat.name,
                            users = chat.users,
                            date = Date()
                    ))
            */

            activity.loadChatScreen(chat.id) // AQUI ES
        }
    }

    // Total items
    override fun getItemCount(): Int {
        return chatList.count()
    }


    // Maps view xml => Kotlin
    inner class ListOfChatViewHolder(view: View): RecyclerView.ViewHolder(view){
        val chatNameTextView: TextView = view.findViewById(R.id.chatNameTextView);
        //val lastMessageTextView: TextView = view.findViewById(R.id.lastMessageTextView);
        val notReadedMessagesTextView: TextView = view.findViewById(R.id.notReadedMessagesTextView);
        val clickableListOfChatsLayout: LinearLayout = view.findViewById(R.id.clickableListOfChatsLayout);
    }

}