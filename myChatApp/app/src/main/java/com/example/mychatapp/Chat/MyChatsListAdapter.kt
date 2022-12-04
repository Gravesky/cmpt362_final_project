package com.example.mychatapp.Chat

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.mychatapp.MainActivity
import com.example.mychatapp.R
import com.example.mychatapp.data.SingleChat
import com.example.mychatapp.data.SingleMsg
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase


class MyChatsListAdapter(private val context: Context, private var list: ArrayList<SingleChat>): BaseAdapter() {

    //Firebase variables
    private lateinit var db: FirebaseDatabase


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
        val latestMessage = view.findViewById<TextView>(R.id.latestMsgTextView)

        textViewName.text = list[position].userName

        /*TODO: 创建一个onDataChange来更新lastmessage
                1. 用list的groupid访问message的database
                2. onDataChange message里面相应的groupid
        */
        db = Firebase.database
        db.useEmulator("10.0.2.2", 9000)

        val messageRef = db.reference.child(MainActivity.MESSAGES_CHILD).child(list[position].groupId!!)

        val messageListener = object : ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val message = snapshot.getValue<SingleMsg>()

                latestMessage.text = message?.message
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                //TODO: 这个功能是给用户本地删除信息用的：
                //      1. 找到该message group最后的child
                //      2. 设置latestMessage

                val message = snapshot.getValue<SingleMsg>()

                latestMessage.text = message?.message
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        }

        messageRef.addChildEventListener(messageListener)

        return view
    }

    fun replace(newChatList: ArrayList<SingleChat>){
        Log.d(TAG,"replace called.")
        list = newChatList
    }

}