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
import com.example.dragonballfigtherzcompanion.Constants
import com.example.dragonballfigtherzcompanion.Constants.COLLECTION_CHAT
import com.example.dragonballfigtherzcompanion.Constants.COLLECTION_MESSAGES
import com.example.dragonballfigtherzcompanion.Constants.COLLECTION_USERS
import com.example.dragonballfigtherzcompanion.MainActivity
import com.example.dragonballfigtherzcompanion.R
import com.example.dragonballfigtherzcompanion.adapter.ChatAdapter
import com.example.dragonballfigtherzcompanion.model.Chat
import com.example.dragonballfigtherzcompanion.model.Message
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_chat.*
import java.util.*

class ChatFragment(val chatId: String) : Fragment() {

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
        recyclerView = view.findViewById(R.id.recyclerViewInChat)
        messageEditText = view.findViewById(R.id.messageEditText)
        sendButton = view.findViewById(R.id.sendButton)
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayoutInChat)

    }

    private fun initRecyclerView(){
        // Layout Manager
        var layoutManager = LinearLayoutManager(activity)
        layoutManager.reverseLayout = true
        recyclerView.layoutManager = layoutManager

        // Adapter
        chatAdapter = ChatAdapter(messageList = emptyList())
        recyclerView.adapter = chatAdapter

        getMessages()
        checkForNotReadedMessages()
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
            getMessages()
        }

        // Look for changes and refresh chats if needed
        Firebase.auth.currentUser?.uid?.let { userId: String ->
            firestore.collection(Constants.COLLECTION_MESSAGES)
                    .addSnapshotListener { messages, error ->
                        if(error == null){
                            messages?.let{
                                getMessages()
                            }
                        }
                    }
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
                            val message = Message(
                                    text = message,
                                    from = userId,
                                    username = user.username,
                                    date = Date(),
                                    messageId = UUID.randomUUID().toString(),
                                    chatId = chatId,
                                    readed = false
                            )
                            //3 - Save in Firestrore
                            firestore
                                .collection(COLLECTION_MESSAGES).document(message.messageId)
                                .set(message)
                                .addOnCompleteListener{
                                    if(it.isSuccessful){
                                        Log.i("Chat", "Success uploading message $message")
                                        //Update chats
                                        getMessages()
                                    }
                                    else{
                                        Log.w("Chat", "Error uploading message $message")
                                        //TODO: Show Error
                                    }
                                }

                            //Notify Chat
                            //val chat: Chat = firestore.collection(Constants.COLLECTION_CHAT).document(message.chatId).get() as Chat
                            firestore.collection(Constants.COLLECTION_CHAT).whereEqualTo("id", message.chatId).get()
                                    .addOnCompleteListener {
                                        if(it.isSuccessful){
                                            val chat: Chat? = it.result?.documents?.mapNotNull{ it.toObject(Chat::class.java) }?.getOrNull(0)
                                            if(chat != null) {
                                                firestore.collection(Constants.COLLECTION_CHAT).document(chat.id)
                                                        .set(Chat(
                                                                id = chat.id,
                                                                name = chat.name,
                                                                users = chat.users,
                                                                date = Date()
                                                        ))
                                            } else {

                                            }
                                        } else {

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
    private fun getMessages(){
        //TODO: Sort
        swipeRefreshLayout.isRefreshing = true
        firestore.collection(COLLECTION_MESSAGES)
                .whereEqualTo("chatId", chatId)
                .get()
                .addOnCompleteListener {
                    if(it.isSuccessful){
                        // Update UI
                        var messages: List<Message> = it.result?.documents?.mapNotNull{ it.toObject(Message::class.java) }.orEmpty()
                        messages = messages.sortedWith(compareByDescending{it.date})
                        chatAdapter.messageList = messages
                        chatAdapter.notifyDataSetChanged()
                        recyclerView.adapter = chatAdapter
                    } else {
                        // TODO: Show Error
                    }
                    swipeRefreshLayout.isRefreshing = false
                }
    }

    private fun checkForNotReadedMessages(){

        Firebase.auth.currentUser?.uid?.let { userId: String ->
            firestore.collection(COLLECTION_MESSAGES)
                    .whereEqualTo("chatId", chatId)
                    .get()
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            // Update UI
                            val messages: List<Message> = it.result?.documents?.mapNotNull { it.toObject(Message::class.java) }.orEmpty()
                            //var actualizedMessages: List<Message> = emptyList()
                            messages.forEach{
                                if(it != null){
                                    if(it.from != userId && it.readed == false){
                                        firestore.collection(COLLECTION_MESSAGES).document(it.messageId)
                                                .set(Message(
                                                        text = it.text,
                                                        from = it.from,
                                                        username = it.username,
                                                        date = it.date,
                                                        messageId = it.messageId,
                                                        chatId = it.chatId,
                                                        readed = true
                                                ))
                                    }
                                }
                                else {

                                }
                            }
                            /*
                            for(i in messages.indices){
                                if(i != null){
                                    if(messages.get(i).from != userId && messages.get(i).readed == false){

                                        firestore.collection(COLLECTION_MESSAGES).document(it.messageId)
                                                .set(Message(
                                                        text = it.text,
                                                        from = it.from,
                                                        username = it.username,
                                                        date = it.date,
                                                        messageId = it.messageId,
                                                        chatId = it.chatId,
                                                        readed = true
                                                ))

                                    }
                                }
                                else {

                                }
                            }*/

                        } else {
                            // TODO: Show Error
                        }
                        swipeRefreshLayout.isRefreshing = false
                    }
        } ?: run {
            (activity as MainActivity).showMessage("Couldn't do it")
        }

    }

}