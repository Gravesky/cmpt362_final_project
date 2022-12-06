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
import com.example.mychatapp.Util
import com.example.mychatapp.data.SingleChat
import com.example.mychatapp.data.SingleMsg
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase


class MyChatsListAdapter(private val context: Context, private var list: ArrayList<SingleChat>): BaseAdapter() {
    companion object {
        const val TAG = "MyChatsListAdapter:DEBUG:"
        const val MSG_DISPLAY_CAP = 28 // String limit for displaying latest message in the chat history fragment.
    }

    //Firebase variables
    private lateinit var db: FirebaseDatabase

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
//        Log.d(TAG,"getView")

        db = Firebase.database
//        db.useEmulator("10.0.2.2", 9000)

        val view = View.inflate(context, R.layout.chat,null)
        val imageViewPortrait = view.findViewById<ImageView>(R.id.chatImageView)
        val textViewName = view.findViewById<TextView>(R.id.messengerTextView)
        val latestMessage = view.findViewById<TextView>(R.id.latestMsgTextView)
        val messageTime = view.findViewById<TextView>(R.id.messageTime)

        try {
            db.reference.child(MainActivity.USER_CHILD).child(list[position].listenerId.toString()).child("userName").get().addOnSuccessListener {
                textViewName.text = it.value.toString()
            }
        }catch (e:Exception) {
            textViewName.text = list[position].userName
        }
        // update chat portrait
        db.reference.child(MainActivity.USER_CHILD).child(list[position].listenerId.toString()).child("photoUrl").get().addOnSuccessListener {
            if (it != null) {
                if (it.value != null && it.value != "null") {
                    Log.d(TAG,"Got the photo URL for this listener: $it")
                    Util.loadImageIntoView(imageViewPortrait,it.value.toString())
                }
            }
            else {
                Log.d(TAG,"Nothing found! This listener does not have portrait photo.")
            }
        }

        /*TODO: 创建一个onDataChange来更新lastmessage
                1. 用list的groupid访问message的database
                2. onDataChange message里面相应的groupid
        */

        val groupLatestMsgRef = db.reference.child(MainActivity.GROUP_CHILD).child(list[position].groupId!!)

        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
//                Log.d(TAG, "onChildAdded:" + dataSnapshot.key + " : " + dataSnapshot.value)
                if (dataSnapshot.key!! == "latestMSG"){
                    var msg: String = ""
                    var name: String = ""
                    for (item in dataSnapshot.children) {
//                        Log.d(TAG,"$item")
                        if (item.key == "message") {
                            msg = item.value.toString()
                        }
                        if (item.key == "userName") {
                            name = item.value.toString()
                        }
                        if (item.key == "timestamp") {
                            messageTime.text = Util.getDateTime(item.value as Long)
                        }
                    }
                    val formatStr = "[$name] : $msg"
                    latestMessage.text = capString(MSG_DISPLAY_CAP,formatStr)
                }
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
//                Log.d(TAG, "onChildChanged: ${dataSnapshot.key}")
                if (dataSnapshot.key == "latestMSG"){
                    var msg: String = ""
                    var name: String = ""
                    for (item in dataSnapshot.children) {
                        Log.d(TAG,"$item")
                        if (item.key == "message") {
                            msg = item.value.toString()
                        }
                        if (item.key == "userName") {
                            name = item.value.toString()
                        }
                        if (item.key == "timestamp") {
                            messageTime.text = Util.getDateTime(item.value as Long)
                        }
                    }
                    val formatStr = "[$name] : $msg"
                    latestMessage.text = capString(MSG_DISPLAY_CAP,formatStr)
                }
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.key!!)
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d(TAG, "onChildMoved:" + dataSnapshot.key!!)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "onCancelled", databaseError.toException())
            }
        }
        groupLatestMsgRef.addChildEventListener(childEventListener)
//        val messageRef = db.reference.child(MainActivity.MESSAGES_CHILD).child(list[position].groupId!!)
//        val messageListener = object : ChildEventListener{
//            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
//                val message = snapshot.getValue<SingleMsg>()
//                Log.d(TAG,"New message from ${message?.userName} : ${message?.message}")
//                val formatStr = "[${message?.userName}]: ${message?.message}"
//                latestMessage.text = formatStr
//            }
//
//            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
////                TODO("Not yet implemented")
//            }
//
//            override fun onChildRemoved(snapshot: DataSnapshot) {
//                //TODO: 这个功能是给用户本地删除信息用的：
//                //      1. 找到该message group最后的child
//                //      2. 设置latestMessage
//
//                val message = snapshot.getValue<SingleMsg>()
//
//                latestMessage.text = message?.message
//            }
//
//            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
////                TODO("Not yet implemented")
//            }
//
//            override fun onCancelled(error: DatabaseError) {
////                TODO("Not yet implemented")
//            }
//
//        }
//
//        messageRef.addChildEventListener(messageListener)

        return view
    }

    fun replace(newChatList: ArrayList<SingleChat>){
        Log.d(TAG,"replace called.")
        list = newChatList
    }

    private fun capString(capSize: Int, input: String): String {
        return if (input.length > capSize) {
            input.substring(0,capSize)
        } else {
            input
        }
    }

}