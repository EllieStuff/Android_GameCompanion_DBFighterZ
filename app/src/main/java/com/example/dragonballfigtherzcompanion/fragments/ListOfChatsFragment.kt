package com.example.dragonballfigtherzcompanion.fragments

import android.os.Bundle
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
import com.example.dragonballfigtherzcompanion.activity.MainActivity
import com.example.dragonballfigtherzcompanion.R
import com.example.dragonballfigtherzcompanion.adapter.ListOfChatAdapter
import com.example.dragonballfigtherzcompanion.model.Chat
import com.example.dragonballfigtherzcompanion.model.Message
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
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
        listOfChatAdapter = ListOfChatAdapter(chatList = emptyList(), activity = (activity as MainActivity))
        recyclerView.adapter = listOfChatAdapter
        getChats()
        //lookForNotReadedMessages()

    }

    private fun initListeners(){
        newChatListener()
        
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
                                lookForNotReadedMessages()
                            }
                        }
                    }


            /*firestore.collection(Constants.COLLECTION_CHAT)
                    .whereArrayContains("users", userId)
                    .get()
                    .addOnCompleteListener {
                        if(it.isSuccessful){
                            val userChats: List<Chat> = it.result?.documents?.mapNotNull { it.toObject(Chat::class.java) }.orEmpty()
                            for(chats in userChats){
                                if(chats != null){
                                    firestore.collection(Constants.COLLECTION_MESSAGES)
                                            .whereArrayContains("chatId", chats.id)
                                            .addSnapshotListener { messages, error ->
                                                if(error == null){
                                                    messages?.let{
                                                        lookForNotReadedMessages()
                                                    }
                                                }
                                            }

                                }
                                else {

                                }
                            }

                        } else {

                        }
                    }*/
        }

    }

    private fun newChatListener(){
        newChatButton.setOnClickListener {
            val otherUserEmail = newChatText.text.toString()
            // Validate
            if (otherUserEmail.isBlank()) return@setOnClickListener
            //showMessage("1");

            // Create Chat
            CreateChat(otherUserEmail) // ESTO ES
        }
    }

    private fun CreateChat(userMail: String){
        //Get Chat Id
        val chatId = UUID.randomUUID().toString()

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
                            Firebase.auth.currentUser?.uid?.let { userId: String ->
                                firestore.collection(Constants.COLLECTION_USERS).document(userId).get().addOnCompleteListener {
                                    val user = it.result?.toObject(User::class.java)?.let { user: User ->
                                        val listOfUsers = listOf(user.userId, otherUser.userId)
                                        firestore.collection(Constants.COLLECTION_CHAT)
                                                .whereEqualTo("users", listOfUsers)
                                                .get()
                                                .addOnCompleteListener {
                                                    if(it.isSuccessful){
                                                        val thisChat = it.result?.documents?.mapNotNull{ it.toObject(Chat::class.java) }?.getOrNull(0)
                                                        if(thisChat != null){
                                                            showMessage(thisChat.name + " already exists")
                                                            firestore.collection(Constants.COLLECTION_CHAT).document(thisChat.id)
                                                                    .set(Chat(
                                                                            id = thisChat.id,
                                                                            name = thisChat.name,
                                                                            users = thisChat.users,
                                                                            date = Date()
                                                                    ))

                                                            getChats()
                                                        }
                                                        else if(user.userId == otherUser.userId){
                                                            firestore.collection(Constants.COLLECTION_CHAT).document(chatId)
                                                                    .set(Chat(
                                                                            id = chatId,
                                                                            name = "Blog de notas",
                                                                            users = listOfUsers,
                                                                            date = Date()
                                                                    ))
                                                        }
                                                        else {
                                                            val chatName = "Chat de: " + user.username + "  &&  " + otherUser.username
                                                            firestore.collection(Constants.COLLECTION_CHAT).document(chatId)
                                                                    .set(Chat(
                                                                            id = chatId,
                                                                            name = chatName,
                                                                            users = listOfUsers,
                                                                            date = Date()
                                                                    ))
                                                        }

                                                    }
                                                    else {

                                                    }
                                                }

                                    }
                                }

                            } ?: run {
                                //User NOT Available
                                //TODO: Show message
                                showMessage("Error on creating chat");
                            }
                        }
                        else{
                            // TODO: Show Error
                            showMessage("Error 2 on finding user");
                        }
                    } else {
                        // TODO: Show Error
                        showMessage("Error 1 on finding user");
                    }
                }

    }

    private fun getChats(){
        //TODO: Sort
        swipeRefreshLayout.isRefreshing = true

        Firebase.auth.currentUser?.uid?.let { userId: String ->
            firestore.collection(Constants.COLLECTION_CHAT)
                    .whereArrayContains("users", userId)
                    .get()
                    .addOnCompleteListener {
                        if(it.isSuccessful){
                            // Update UI
                            var chats: List<Chat> = it.result?.documents?.mapNotNull{ it.toObject(Chat::class.java) }.orEmpty()
                            chats = chats.sortedWith(compareByDescending{it.date})
                            listOfChatAdapter.chatList = chats
                            listOfChatAdapter.notifyDataSetChanged()
                            recyclerView.adapter = listOfChatAdapter
                        } else {
                            // TODO: Show Error
                            showMessage("Error Getting The Chats");
                        }
                        swipeRefreshLayout.isRefreshing = false
                    }

        } ?: run {
            //User NOT Available
            //TODO: Show message
            showMessage("You must log in to chat with friends");
        }


    }

    private fun showMessage(text: String?) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show()
    }

    private fun lookForNotReadedMessages(){

        Firebase.auth.currentUser?.uid?.let { userId: String ->
            firestore.collection(Constants.COLLECTION_CHAT)
                    .whereArrayContains("users", userId)
                    .get()
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            // Update UI
                            var userChats: List<Chat> = it.result?.documents?.mapNotNull { it.toObject(Chat::class.java) }.orEmpty()
                            userChats = userChats.sortedWith(compareByDescending{it.date})
                            for(i in userChats.indices){
                                if(it != null){
                                    firestore.collection(Constants.COLLECTION_MESSAGES)
                                            .whereEqualTo("chatId", userChats.get(i).id)
                                            .get()
                                            .addOnCompleteListener {
                                                if(it.isSuccessful) {
                                                    val chatMessages: List<Message> = it.result?.documents?.mapNotNull { it.toObject(Message::class.java) }.orEmpty()
                                                    var messagesToRead: Int = 0
                                                    for(message in chatMessages){
                                                        //showMessage( messagesToRead.toString())
                                                        if(message != null){
                                                            //showMessage( messagesToRead.toString())
                                                            if(message.from != userId && message.readed == false){
                                                                messagesToRead++
                                                                //showMessage( messagesToRead.toString())
                                                            }
                                                        }
                                                    }

                                                    //userChats.get(i).messagesToRead = messagesToRead

                                                    listOfChatAdapter.chatList.get(i).messagesToRead = messagesToRead
                                                    listOfChatAdapter.notifyDataSetChanged()
                                                    recyclerView.adapter = listOfChatAdapter

                                                } else {

                                                }
                                            }

                                }
                                else {

                                }
                            }

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