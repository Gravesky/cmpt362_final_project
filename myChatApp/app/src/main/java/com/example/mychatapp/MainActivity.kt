package com.example.mychatapp

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import java.net.URI
import java.net.URISyntaxException


class MainActivity : AppCompatActivity() {
    companion object {
        const val TAG = "MainActivity:DEBUG:"
        const val myURI = "http://10.0.2.2:3000/"
    }

    private lateinit var mSocket: Socket

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG,"on create called.")

        // The following lines connects the Android app to the server.
        try {
            val uri  = URI.create(myURI)
            val options = IO.Options.builder()
                .setForceNew(false)
                .build()

            mSocket = IO.socket(uri,options)
        } catch (e: URISyntaxException) {
            Log.d(TAG,"Catching...")
            e.printStackTrace()
        }

        val connectButton = findViewById<Button>(R.id.connect_button)
        val disconnectButton = findViewById<Button>(R.id.disconnect_button)

        connectButton.setOnClickListener(){
            mSocket.connect()
        }

        disconnectButton.setOnClickListener(){
            mSocket.disconnect()
        }

        mSocket.on(Socket.EVENT_CONNECT, Emitter.Listener {
            Log.d(TAG,"event: socket id = ${mSocket.id()}, state = ${mSocket.connected()}")
        });

        mSocket.on(Socket.EVENT_DISCONNECT, Emitter.Listener {
            Log.d(TAG,"event: socket id = ${mSocket.id()}, state = ${mSocket.connected()}")// null
        })
    }
}