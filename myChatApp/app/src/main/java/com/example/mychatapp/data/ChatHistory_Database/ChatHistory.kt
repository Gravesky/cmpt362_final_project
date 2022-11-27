package com.example.mychatapp.data.ChatHistory_Database

import android.icu.util.Calendar
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

//This table should store user's chat histories with his/her friends
//聊天记录列表数据库（比如说： 微信主页面）

@Entity(tableName = "Chat_Messages_Table")
data class ChatHistory (
    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "account_id_column")
    var acc_id: String = "", //Should store the friend's acc_id

    @ColumnInfo(name = "last_chat_column")
    var last_chat_time: Calendar = Calendar.getInstance(),

    @ColumnInfo(name = "last_message_time")
    var last_message: String = "",

    @ColumnInfo(name = "last_sender")
    var last_sender: String = "" //Should store the sender's name

)