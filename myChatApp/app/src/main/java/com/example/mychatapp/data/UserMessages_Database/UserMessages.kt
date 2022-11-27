package com.example.mychatapp.data.UserMessages_Database

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mychatapp.data.SingleMessage
import kotlin.collections.ArrayList


//This table stores all user's friends' information and message histories between user and his/her friends
//通讯录数据库

@Entity(tableName = "User_Messages_Table")
data class UserMessages(
    /*@PrimaryKey(autoGenerate = true)
    var id: Long = 0L,*/

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "account_id_column")
    var acc_id: String = "", //User can have their preferred account id, and they are unique in the chat app world

    @ColumnInfo(name = "message_history_column")
    var message_history: ArrayList<SingleMessage> = ArrayList(), //This Column contains all messages between two users

    @ColumnInfo(name = "name_column")
    var name: String = "",

    @ColumnInfo(name = "email_column")
    var email: String = "",

    @ColumnInfo(name = "phone_number_column")
    var phone: String = "",

    @ColumnInfo(name = "profile_pic_column")
    var profile_pic: String = "" //This stores the URI of the profile picture
)
