package com.example.mychatapp.Chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.mychatapp.R
import com.example.mychatapp.data.ChatHistory_Database.*

class ChatHistoryFragment: Fragment() {

    //Database variables
    private lateinit var database: ChatHistoryDatabase
    private lateinit var databaseDao: ChatHistoryDatabaseDao
    private lateinit var repository: ChatHistoryRepository
    private lateinit var viewModelFactory: ChatHistoryViewModelFactory
    private lateinit var chatHistoryViewModel: ChatHistoryViewModel

    //ListView variables
    private lateinit var listView: ListView
    private lateinit var arrayList:ArrayList<ChatHistory>
    private lateinit var arrayAdapter:MyChatHistoryListAdapter

    private lateinit var view1: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_chat, container, false)

        //Initialize database variables
        database = ChatHistoryDatabase.getInstance(requireActivity())
        databaseDao = database.chatHistoryDatabaseDao
        repository = ChatHistoryRepository(databaseDao)
        viewModelFactory = ChatHistoryViewModelFactory(repository)
        chatHistoryViewModel = ViewModelProvider(this, viewModelFactory).get(ChatHistoryViewModel::class.java)

        //Initialize ListView variables
        listView = view.findViewById(R.id.chat_list)
        arrayList = ArrayList()
        arrayAdapter = MyChatHistoryListAdapter(requireActivity(), arrayList)
        listView.adapter = arrayAdapter

        //Refresh the listview when new message receives(new update on the table)
        chatHistoryViewModel.allChatHistoryLiveData.observe(requireActivity(), Observer{
            arrayAdapter.replace(it)
            arrayAdapter.notifyDataSetChanged()
            arrayList = it as ArrayList<ChatHistory>
            listView.adapter = arrayAdapter
        })

        //Listener for listview
        listView.setOnItemClickListener { parent, newview, position, id ->
            /*
            TODO: 1. get the acc_id of the clicked chat
                  2. access UserMessages database to get all messages between the user and this friend
                  3. open the ChatActivity
             */
        }

        return view
    }

}