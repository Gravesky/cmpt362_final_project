package com.example.mychatapp.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.mychatapp.Chat.ChatActivity
import com.example.mychatapp.Chat.ChatHistoryFragment
import com.example.mychatapp.MainActivity
import com.example.mychatapp.R
import com.example.mychatapp.data.Group
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class Notification : Service() {

    private var CHANNEL_ID = "Message Notification Channel"
    private var CHANNEL_NAME = "Message Notification"
    private lateinit var notificationManager: NotificationManager
    private val NOTIFICATION_ID = 100

    // Firebase instance variables
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase

    private lateinit var groups : ArrayList<String>

    override fun onCreate(){
        super.onCreate()

        //Create the notification channel
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= 26) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }

        auth = Firebase.auth
        db = Firebase.database
        //db.useEmulator("10.0.2.2", 9000)
        val user = auth.currentUser
        val groupRef = db.reference.child(MainActivity.USER_CHILD).child(user?.uid!!).child(MainActivity.GROUP_CHILD)

        groups = ArrayList()

        //Listener for the current user's associated group
        val messageListener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                //Get the current user's associated group
                val chats = snapshot.children

                //For every group, we want to listen for new messages and push notification
                for(group in chats){
//                    val lastMsgRef = db.reference.child(MainActivity.GROUP_CHILD).child(group.value as String)
                    groups.add(group.value as String)
                }

                settingListeners(groups)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Notification:DEBUG:","$error")
            }

        }

        groupRef.addValueEventListener(messageListener)

    }

    private fun settingListeners(groupList : ArrayList<String>){
        for(group in groupList){
            val groupRef = db.reference.child(MainActivity.GROUP_CHILD).child(group).child("latestMSG")

            groupRef.addValueEventListener(object : ValueEventListener{

                var isThisFirst = true
                val groupId = group

                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.value != null) {
                        val message = snapshot.value as HashMap<String, Any>

                        if (!isThisFirst) { //User isn't JUST logged in
                            if (groupId != ChatActivity.groupId) { //If the groupId of the new message is not equal to the current groupId of the chatActivity
                                displayNotification(
                                    groupId as String,
                                    message["speakerId"] as String,
                                    message["userName"] as String,
                                    message["message"] as String
                                )
                            }
                        }
                    }
                    isThisFirst = false
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("Notification:DEBUG:","$error")
                }

            })
        }
    }


    private fun displayNotification(groupId : String, senderUid : String,senderName : String, message : String){
        val intent = Intent(this, ChatActivity::class.java)
        intent.putExtra(ChatHistoryFragment.KEY_GROUP_ID, groupId)
        intent.action = ChatActivity.EXIST_CHAT_ACTION
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent = PendingIntent.getActivities(
            this,0, arrayOf(intent), PendingIntent.FLAG_IMMUTABLE
        )
        //TODO: pass the groupid to the pendingIntent


        //TODO: Get the URL of the speaker
        //      1. download the bitmap file through the url
        //      2. put setLargeIcon(bitmap) to notification builder
        //val userRef = db.reference.child(MainActivity.USER_CHILD).child(senderUid).child("photoUrl")

        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.sfu_logo)
            .setContentTitle("SFUChat ")
            .setContentText("${senderName}: $message")
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        val notification = notificationBuilder.build()
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }
}