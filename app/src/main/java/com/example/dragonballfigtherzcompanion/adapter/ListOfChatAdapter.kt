package com.example.dragonballfigtherzcompanion.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.example.dragonballfigtherzcompanion.MainActivity
import com.example.dragonballfigtherzcompanion.R
import com.example.dragonballfigtherzcompanion.fragments.ListOfChatsFragment
import com.example.dragonballfigtherzcompanion.model.Chat

class ListOfChatAdapter(var chatList: List<Chat>): RecyclerView.Adapter<ListOfChatAdapter.ListOfChatViewHolder>() {

    // Inflate view (xml layout) => ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListOfChatViewHolder {
        val chatView: View = LayoutInflater.from(parent.context).inflate(R.layout.item_listofchats, parent, false)

        return ListOfChatViewHolder(chatView)
    }

    // Update view for position
    override fun onBindViewHolder(holder: ListOfChatViewHolder, position: Int) {
        val chat = chatList[position]
        holder.chatNameTextView.text = chat.name
        //holder.lastMessageTextView.text = chat.username
        //TODO: Format Date

        holder.clickableListOfChatsLayout.setOnClickListener {
            /*
            val transaction: FragmentTransaction = MainActivity.instance.supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainer, ListOfChatsFragment())
            transaction.commit()
            */
            //MainActivity.instance.showMessage("in")

            val chut = chat
        }
    }

    // Total items
    override fun getItemCount(): Int {
        return chatList.count()
    }


    // Maps view xml => Kotlin
    inner class ListOfChatViewHolder(view: View): RecyclerView.ViewHolder(view){
        val chatNameTextView: TextView = view.findViewById(R.id.chatNameTextView);
        val lastMessageTextView: TextView = view.findViewById(R.id.lastMessageTextView);
        val clickableListOfChatsLayout: RelativeLayout = view.findViewById(R.id.clickableListOfChatsLayout);
    }

}