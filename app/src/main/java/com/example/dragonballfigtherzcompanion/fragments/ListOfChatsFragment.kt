package com.example.dragonballfigtherzcompanion.fragments

import android.os.Bundle
import android.service.autofill.UserData
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.dragonballfighterzcompanion.model.User
import com.example.dragonballfigtherzcompanion.Constants
import com.example.dragonballfigtherzcompanion.Constants.COLLECTION_CHAT
import com.example.dragonballfigtherzcompanion.Constants.COLLECTION_USERS
import com.example.dragonballfigtherzcompanion.MainActivity
import com.example.dragonballfigtherzcompanion.R
import com.example.dragonballfigtherzcompanion.adapter.ListOfChatAdapter
import com.example.dragonballfigtherzcompanion.model.Chat
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_chat.*
import java.util.*

class ListOfChatsFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var newChatText: EditText
    private lateinit var newChatButton: Button
    private lateinit var  swipeRefreshLayout: SwipeRefreshLayout

    private lateinit var firestore: FirebaseFirestore

    private lateinit var listOfChatAdapter: ListOfChatAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_listofchats, container, false)
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
        newChatText = view.findViewById(R.id.newChatName)
        newChatButton = view.findViewById(R.id.newChatButton)
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)

    }

    private fun initRecyclerView(){
        // Layout Manager
        var layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager

        // Adapter
        /*chatAdapter = ChatAdapter{ chat->
            chatSelected(chat)
        }*/
        listOfChatAdapter = ListOfChatAdapter(chatList = emptyList(), activity = (activity as MainActivity))
        recyclerView.adapter = listOfChatAdapter
        getChats()

    }

    private fun initListeners(){
        newChatButton.setOnClickListener{
            newChat()
        }
        //Swipe to Refresh
        swipeRefreshLayout.setOnRefreshListener {
            getChats()
        }

        // Look for changes and refresh chats if needed
        Firebase.auth.currentUser?.uid?.let { userId: String ->
            firestore.collection(Constants.COLLECTION_CHAT)
                    .whereArrayContains("users", userId)
                    .addSnapshotListener { chats, error ->
                        if(error == null){
                            chats?.let{
                                getChats()
                            }
                        }
                    }
        }

    }

    private fun chatSelected(chat: Chat){
        //val
    }

    private fun newChat(){
        newChatButton.setOnClickListener {
            val otherUserEmail = newChatText.text.toString()
            // Validate
            if (otherUserEmail.isBlank()) return@setOnClickListener
            //showMessage("1");

            // Create Chat
            CreateChat(otherUserEmail)
        }
    }

    private fun CreateChat(userMail: String){
        //Get Chat Id
        val chatId = UUID.randomUUID().toString()

        // Get Chat Num (in order to create it's name)
        var chatNum: Int? = 0
        firestore.collection(Constants.COLLECTION_CHAT)
                .get()
                .addOnCompleteListener {
                    if(it.isSuccessful){
                        chatNum = it.result?.documents?.mapNotNull{ it.toObject(User::class.java) }?.size
                        //showMessage("2");
                    } else {
                        // TODO: Show Error
                        showMessage("Error on creating the name");
                    }
                }

        // Look for the other User
        firestore.collection(Constants.COLLECTION_USERS)
                .whereEqualTo("email", userMail)
                .get()
                .addOnCompleteListener {
                    if(it.isSuccessful){
                        val otherUser = it.result?.documents?.mapNotNull{ it.toObject(User::class.java) }?.getOrNull(0)
                        if (otherUser != null) {
                            val otherUserId = otherUser.userId

                            // Create New Chat
                            Firebase.auth.currentUser?.uid?.let { userId: String? ->
                                firestore.collection(Constants.COLLECTION_CHAT).document(chatId)
                                        .set(Chat(
                                                id = chatId,
                                                name = "Chat " + chatNum.toString(),
                                                users = listOf(userId, otherUserId),
                                                date = Date()
                                        ))

                            } ?: run {
                                //User NOT Available
                                //TODO: Show message
                                showMessage("Error on creating chat");
                            }
                        }
                    } else {
                        // TODO: Show Error
                        showMessage("Error on finding user");
                    }
                }

    }

    private fun getChats(){
        //TODO: Sort
        swipeRefreshLayout.isRefreshing = true

        firestore.collection(Constants.COLLECTION_CHAT)
                .orderBy("date", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener {
                    if(it.isSuccessful){
                        showMessage("Chats ordered");
                    } else {
                        // TODO: Show Error
                        showMessage("Error on ordering the chats");
                    }
                }

        Firebase.auth.currentUser?.uid?.let { userId: String ->
            firestore.collection(Constants.COLLECTION_CHAT)
                    .whereArrayContains("users", userId)
                    .get()
                    .addOnCompleteListener {
                        if(it.isSuccessful){
                            // Update UI
                            val chats: List<Chat> = it.result?.documents?.mapNotNull{ it.toObject(Chat::class.java) }.orEmpty()
                            listOfChatAdapter.chatList = chats
                            listOfChatAdapter.notifyDataSetChanged()
                        } else {
                            // TODO: Show Error
                            showMessage("Error Getting The Chats");
                        }
                        swipeRefreshLayout.isRefreshing = false
                    }

        } ?: run {
            //User NOT Available
            //TODO: Show message
            showMessage("Error Finding the userId");
        }


    }

    private fun showMessage(text: String?) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show()
    }

}