package com.example.mychatapp

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class MyListAdapter(private val context: Context, private var messageList: ArrayList<SingleMessage>):
    BaseAdapter() {
    override fun getCount(): Int {
        return messageList.size
    }

    override fun getItem(position: Int): Any {
        return messageList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = View.inflate(context, R.layout.layout_adapter,null)

        val userNameTextView = view.findViewById<TextView>(R.id.user_name)
        val chatRoomTextView = view.findViewById<TextView>(R.id.room_info)
        val msgContentTextView = view.findViewById<TextView>(R.id.message_content)

        userNameTextView.text =  messageList[position].sender
        chatRoomTextView.text = "n/a" //TODO: add chat room info
        msgContentTextView.text = messageList[position].message

        return view
    }

    fun replace(newMessageList: ArrayList<SingleMessage>){
        messageList = newMessageList
    }
}