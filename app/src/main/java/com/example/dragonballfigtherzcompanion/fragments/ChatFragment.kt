package com.example.dragonballfigtherzcompanion.fragments

import android.os.Bundle
import android.service.autofill.UserData
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.dragonballfighterzcompanion.model.User
import com.example.dragonballfigtherzcompanion.Constants.COLLECTION_CHAT
import com.example.dragonballfigtherzcompanion.Constants.COLLECTION_USERS
import com.example.dragonballfigtherzcompanion.MainActivity
import com.example.dragonballfigtherzcompanion.R
import com.example.dragonballfigtherzcompanion.adapter.ChatAdapter
import com.example.dragonballfigtherzcompanion.model.Chat
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_chat.*
import java.util.*

class ChatFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var messageEditText: EditText
    private lateinit var sendButton: Button
    private lateinit var  swipeRefreshLayout: SwipeRefreshLayout

    private lateinit var firestore: FirebaseFirestore

    private lateinit var chatAdapter: ChatAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)

        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState);
        // Init firestore
        firestore = Firebase.firestore
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
        sendButton = view.findViewById(R.id.sendButton)
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)

    }

    private fun initRecyclerView(){
        // Layout Manager
        var layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager

        // Adapter
        chatAdapter = ChatAdapter(chatList = emptyList())
        recyclerView.adapter = chatAdapter

        getChats()
    }

    private fun initListeners(){
        sendButton.setOnClickListener{
            val message = messageEditText.text.toString()
            // Validate
            if(message.isBlank()) return@setOnClickListener
            //Send Message
            sendMessage(message)
            messageEditText.setText("")
        }
        //Swipe to Refresh
        swipeRefreshLayout.setOnRefreshListener {
            getChats()
        }
    }

    private fun sendMessage(message: String)
    {
        //0 - Get user id
        Firebase.auth.currentUser?.uid?.let { userId: String ->
            //User Available
            //1 - Get User Object
            /// UserData.getUserName()

            firestore
                .collection(COLLECTION_USERS)
                .document(userId)
                .get()
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        val user = it.result?.toObject(User::class.java)?.let { user: User ->
                            //2 - Create Chat Message
                            val chat = Chat(
                                    /*
                                userId = Firebase.auth.currentUser?.uid,
                                message = message,
                                sentAt = Date().time,
                                isSent = false,
                                imageUrl = null,
                                username = user.username,
                                avatarUrl = null,   //TODO: Support User Avatar
                                     */
                            )
                            //3 - Save in Firestrore
                            firestore
                                .collection(COLLECTION_CHAT)
                                .add(chat)
                                .addOnCompleteListener{
                                    if(it.isSuccessful){
                                        Log.i("Chat", "Success uploading message $message")
                                        //Update chats
                                        getChats()
                                    }
                                    else{
                                        Log.w("Chat", "Error uploading message $message")
                                        //TODO: Show Error
                                    }
                                }
                        } ?: run {
                            //TODO: Show Error
                        }
                    } else {
                        //TODO: Show Error
                    }
                }

        } ?: run {
            //User NOT Available
            //TODO: Show message

        }

    }
    private fun getChats(){
        //TODO: Sort
        swipeRefreshLayout.isRefreshing = true
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
                    swipeRefreshLayout.isRefreshing = false
                }
    }

}