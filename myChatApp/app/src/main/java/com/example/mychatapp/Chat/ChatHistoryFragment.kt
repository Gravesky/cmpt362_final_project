package com.example.mychatapp.Chat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mychatapp.Contact.MyContactsListAdapter
import com.example.mychatapp.MainActivity
import com.example.mychatapp.R
import com.example.mychatapp.auth.SignInActivity
import com.example.mychatapp.data.ChatHistoryAdapter
import com.example.mychatapp.data.ChatHistory_Database.*
import com.example.mychatapp.data.SingleChat
import com.example.mychatapp.data.SingleMsg
import com.example.mychatapp.data.UserMessages_Database.*
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class  ChatHistoryFragment: Fragment() {
    companion object {
        const val TAG = "ChatHistFrag:DEBUG:"
        const val MESSAGES_CHILD = "messages"
        const val KEY_GROUP_ID = "groupId"
    }

    //Firebase variables
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase
    private lateinit var myListView: ListView
    private lateinit var myAdapter: MyChatsListAdapter
    private lateinit var arrayList: ArrayList<SingleChat>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_chat, container, false)

        auth = Firebase.auth
        if (auth.currentUser != null) {
            Log.d(TAG,"This fragment has a user authenticated: ${auth.currentUser!!.uid}")
        }

        // Initialize Realtime Database
        db = Firebase.database
        db.useEmulator("10.0.2.2",9000)
        val userRef = db.reference.child(MainActivity.USER_CHILD).child(auth.currentUser!!.uid).child("friends")

        myListView = view.findViewById(R.id.chatHistoryList)
        arrayList = ArrayList()
        myAdapter = MyChatsListAdapter(requireActivity(),arrayList)
        myListView.adapter = myAdapter

        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val chatList = ArrayList<SingleChat>()
                for (postSnapshot in dataSnapshot.children) {
                    val chatMap = postSnapshot.value as HashMap<*, *>
                    if (chatMap["groupId"].toString() != "n/a") {
                        val newChat = SingleChat(
                            chatMap["groupId"].toString(),
                            chatMap["userName"].toString()
                        )
                        chatList.add(newChat)
                    }

                }
                Log.d(TAG, "onDataChange -> $chatList")
                myAdapter.replace(chatList)
                myAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                // ...
            }
        })

        myListView.setOnItemClickListener { adapterView, view, i, l ->
            Log.d(TAG,"index at  $i clicked.")
            val thisChat = myAdapter.getItem(i) as SingleChat
            val startChatIntent = Intent(requireActivity(),ChatActivity::class.java)
            startChatIntent.action = ChatActivity.EXIST_CHAT_ACTION
            startChatIntent.putExtra(KEY_GROUP_ID,thisChat.groupId)
            startActivity(startChatIntent)
        }

        Log.d(TAG, "onCreate -> finished")
        return view
    }

    override fun onPause() {
        Log.d(TAG,"OnPause called")
        super.onPause()
    }

    override fun onResume() {
        Log.d(TAG,"onResume called")
        super.onResume()
        myAdapter.notifyDataSetChanged()
        // ref: https://stackoverflow.com/questions/71665925/error-inconsistency-detected-invalid-view-holder-adapter-position-myviewholder

    }

}
