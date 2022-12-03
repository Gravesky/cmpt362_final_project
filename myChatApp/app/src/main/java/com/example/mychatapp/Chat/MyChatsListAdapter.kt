package com.example.mychatapp.Chat

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.mychatapp.R
import com.example.mychatapp.data.SingleChat

class MyChatsListAdapter(private val context: Context, private var list: ArrayList<SingleChat>): BaseAdapter() {
    companion object {
        const val TAG = "MyChatsListAdapter:DEBUG:"
    }
    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
        Log.d(TAG,"getView")
        val view = View.inflate(context, R.layout.chat,null)
        val imageViewPortrait = view.findViewById<ImageView>(R.id.chatImageView)
        val textViewName = view.findViewById<TextView>(R.id.messengerTextView)

        textViewName.text = list[position].userName

        /*TODO: 创建一个onDataChange来更新lastmessage
                1. 用list的groupid访问message的database
                2. onDataChange message里面相应的groupid
        */

        return view
    }

    fun replace(newChatList: ArrayList<SingleChat>){
        Log.d(TAG,"replace called.")
        list = newChatList
    }

}