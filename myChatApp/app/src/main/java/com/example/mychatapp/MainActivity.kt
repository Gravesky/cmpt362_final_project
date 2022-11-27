package com.example.mychatapp

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONObject
import java.net.URI
import java.net.URISyntaxException


class MainActivity : AppCompatActivity() {
    companion object {
        const val TAG = "MainActivity:DEBUG:"
        const val myURI = "http://10.0.2.2:3000/" // Local host in android emulator
    }

    private lateinit var mSocket: Socket
    private lateinit var arrayList: java.util.ArrayList<SingleMessage>
    private lateinit var myListAdapter: MyListAdapter
    private lateinit var myMessageViewModel: MessageViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "on create called.")

        // The following lines connects the Android app to the server.
        try {
            val uri = URI.create(myURI)
            val options = IO.Options.builder()
                .setForceNew(false)
                .build()

            mSocket = IO.socket(uri, options)
        } catch (e: URISyntaxException) {
            Log.d(TAG, "Catching...")
            e.printStackTrace()
        }

        val connectButton = findViewById<Button>(R.id.connect_button)
        val disconnectButton = findViewById<Button>(R.id.disconnect_button)
        val sendButton = findViewById<Button>(R.id.send_button)
        val userInputEditText = findViewById<EditText>(R.id.user_input)
        val messageListView = findViewById<ListView>(R.id.chat_content_list)
        val sessionIdTextView = findViewById<TextView>(R.id.session_id)

        connectButton.setOnClickListener() {
            mSocket.connect()
        }

        disconnectButton.setOnClickListener() {
            mSocket.disconnect()
        }

        sendButton.setOnClickListener() {
            val userMsg = userInputEditText.text.toString()
            if (userMsg.isNotEmpty()) {
                myMessageViewModel.putNewMessage(Util.assemblyMessage(userMsg,Util.getTimeNow(),mSocket.id().toString()))
                val msgBody = JSONObject()
                msgBody.put("user_name", mSocket.id())
                msgBody.put("msg_content",userMsg)
                mSocket.emit(Globals.NEW_MESSAGE_EVENT, msgBody)
                userInputEditText.text.clear()
            }
        }

        // listview adapters
        arrayList = ArrayList()
        myListAdapter = MyListAdapter(this, arrayList)
        messageListView.adapter = myListAdapter

        // initial view model
        myMessageViewModel = ViewModelProvider(this)[MessageViewModel::class.java]
        myMessageViewModel.messageHistory.observe(this) {
            myListAdapter.replace(it)
            myListAdapter.notifyDataSetChanged()
        }

        mSocket.on(Socket.EVENT_CONNECT, Emitter.Listener {
            Log.d(TAG, "event: socket id = ${mSocket.id()}, state = ${mSocket.connected()}")
            val sessionInfo: String = "socket id = ${mSocket.id()}"
            sessionIdTextView.text = sessionInfo
        });

        mSocket.on(Socket.EVENT_DISCONNECT, Emitter.Listener {
            Log.d(TAG, "event: socket id = ${mSocket.id()}, state = ${mSocket.connected()}")// null
        })

        mSocket.on(Globals.RECV_MESSAGE_EVENT, Emitter.Listener {
            val receivedMsgBody = JSONObject(it[0].toString())
            val receivedMsg = Util.assemblyMessage(receivedMsgBody.getString("msg_content"), Util.getTimeNow(), receivedMsgBody.getString("user_name"))
            myMessageViewModel.putNewMessage(receivedMsg)
        })
    }
}