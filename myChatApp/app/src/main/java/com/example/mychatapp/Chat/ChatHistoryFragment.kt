package com.example.mychatapp.Chat

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.example.mychatapp.R
import com.example.mychatapp.data.ChatHistory_Database.*
import com.example.mychatapp.data.UserMessages_Database.*

class  ChatHistoryFragment: Fragment() {

    //Database variables
    private lateinit var database: ChatHistoryDatabase
    private lateinit var databaseDao: ChatHistoryDatabaseDao
    private lateinit var repository: ChatHistoryRepository
    private lateinit var viewModelFactory: ChatHistoryViewModelFactory
    private lateinit var chatHistoryViewModel: ChatHistoryViewModel

    private lateinit var userMessageDataBase: UserMessagesDatabase
    private lateinit var userMessageDataBaseDao: UserMessagesDatabaseDao
    private lateinit var userMessageDataBaseRepository: UserMessagesRepository
    private lateinit var userViewModelFactory: UserMessagesViewModelFactory
    private lateinit var userMessageDataBaseViewModel: UserMessagesViewModel


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

        //Initialize UserMessage database
        userMessageDataBase = UserMessagesDatabase.getInstance(requireActivity())
        userMessageDataBaseDao = userMessageDataBase.userMessagesDatabaseDao
        userMessageDataBaseRepository = UserMessagesRepository(userMessageDataBaseDao)
        userViewModelFactory = UserMessagesViewModelFactory(userMessageDataBaseRepository)
        userMessageDataBaseViewModel = ViewModelProvider(this,userViewModelFactory).get(UserMessagesViewModel::class.java)

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
            var chatHistory:ChatHistory = arrayAdapter.getItem(position) as ChatHistory
            var userID = chatHistory.acc_id
            //为了在此界面显示每个人名字下面的那一句聊天记录，在这里先访问一次
            //usermessage数据库

            //text是最后一条聊天记录
            var message = userMessageDataBaseDao.getUserMessage(userID)
            //var text = message.message_history.last() as String
            val intent = Intent(view.context,ChatActivity::class.java)
            intent.putExtra("userID",userID)
            //intent.putExtra("lastText",text)
            startActivity(intent)
        }

        return view
    }

}