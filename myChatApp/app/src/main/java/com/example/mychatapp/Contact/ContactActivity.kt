package com.example.mychatapp.Contact

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.example.mychatapp.Chat.ChatActivity
import com.example.mychatapp.MainActivity
import com.example.mychatapp.R
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ContactActivity : AppCompatActivity() {
    companion object {
        const val TAG = "ContactActivity:DEBUG:"
    }
    private lateinit var thisUserId:String
    private lateinit var db: FirebaseDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)

        thisUserId = intent.getStringExtra(ContactsFragment.KEY_USER_ID).toString()
        Log.d(TAG,"OnCreate called - with uid = $thisUserId")
        val startChatButton = findViewById<Button>(R.id.startChatButton)
        val userNameText = findViewById<TextView>(R.id.userName)

        // Get user data from db
        db = Firebase.database
        db.useEmulator("10.0.2.2",9000)

        // Set onSuccess callback for retrieving use data
        db.reference.child(MainActivity.USER_CHILD).child(thisUserId).get().addOnSuccessListener {
//            Log.d(TAG,"retrieved user info = $it")
            for (child in it.children) {
                when (child.key) {
                    "userName" -> {
                        Log.d(TAG,"This child is ${child.key}")
                        userNameText.text = child.value.toString()
                    }
                    else -> {
                        Log.d(TAG,"This child is ${child.key}")
                    }
                }
            }
        }

        startChatButton.setOnClickListener{
            Log.d(TAG,"chat button clicked")
            val chatIntent = Intent(this,ChatActivity::class.java)
            chatIntent.action = ChatActivity.NEW_CHAT_ACTION
            chatIntent.putExtra(ChatActivity.TARGET_UID_KEY,thisUserId) // put the uid of the person we want to chat with into intent
            startActivity(chatIntent)
            finish() // Call finish because we no longer need this activity
        }
    }
}