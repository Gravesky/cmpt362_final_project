package com.example.mychatapp.Chat

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.mychatapp.R
import com.example.mychatapp.data.ChatHistory_Database.ChatHistory

class MyChatHistoryListAdapter (private val context: Context, private var chatList: List<ChatHistory>):BaseAdapter() {
    override fun getCount(): Int {
        return chatList.size
    }

    override fun getItem(position: Int): Any {
        return chatList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = View.inflate(context, R.layout.adapter_chat_list_layout,null)

        //TODO: fill in the listview

        return view
    }

    fun replace(newChatList: List<ChatHistory>?) {
        if (newChatList != null) {
            chatList = newChatList
        }
    }
}