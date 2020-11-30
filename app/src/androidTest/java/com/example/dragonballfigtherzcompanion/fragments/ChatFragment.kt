package com.example.dragonballfighterzcompanion

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dragonballfigtherzcompanion.Constants.COLLECTION_CHAT
import com.example.dragonballfigtherzcompanion.R
import com.example.dragonballfigtherzcompanion.adapter.ChatAdapter
import com.example.dragonballfigtherzcompanion.model.Chat
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_chat.*

class ChatFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var messageEditText: EditText
    private lateinit var sendButton: Button

    private lateinit var firestore: FirebaseFirestore

    private lateinit var chatAdapter: ChatAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState);
        // TODO: Init
        // Init Views
        initViews(view)

        // Init RecyclerView
        initRecyclerView()

        // Init Listeners
        initListeners()

    }

    private fun initViews(view: View){
        recyclerView = view.findViewById(R.id.recyclerView)
        messageEditText = view.findViewById(R.id.messageEditText)
        sendButton = view.findViewById((R.id.sendButton))

    }

    private fun initRecyclerView(){
        // Layout Manager
        var layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager

        // Adapter
        chatAdapter = ChatAdapter(chatList = listOf("Chat 0", "Chat 1", "Chat 2", "Chat 3", "Chat 4", "Chat 5", "Chat 6", "Chat 7", "Chat 8", "Chat 9"))
        recyclerView.adapter = chatAdapter

    }

    private fun initListeners(){
        sendButton.setOnClickListener{
            val message = messageEditText.text.toString()
            // Validate
            if(message.isBlank()) return@setOnClickListener
            // Create Chat Message
            val chat = Chat(message = message)
            // Save in Firestrore
            firestore
                    .collection(COLLECTION_CHAT)
                    .add(chat)
                    .addOnCompleteListener{
                        if(it.isSuccessful){
                            Log.i("Chat", "Success uploading message $message")
                        }
                        else{
                            Log.i("Chat", "Error uploading message $message")
                        }
                    }
        }
    }

    private fun getChats(){
        firestore.collection(COLLECTION_CHAT)
                .get()
                .addOnCompleteListener {
                    if(it.isSuccessful){
                        // Update UI
                        val chats: List<Chat> = it.result?.documents?.mapNotNull{ it.toObject(Chat::class.java) }.orEmpty()
                        chatAdapter.chatList = chats
                        chatAdapter.notifyDataSetChanged()
                    } else {
                        // TODO: Show Error
                    }
                }
    }

}