package com.example.mychatapp.data

import android.net.Uri
import java.sql.Timestamp

data class User(
    val userID : String? = null, //ID of the user
    val userName : String? = null, //Username of the user
    val userEmail : String? = null, //Email of the user
    val timestamp: Any? = null, //Time when this account was being created
    val friends : HashMap<String, Any>? = null, //Friends of this user, it contains an array of userID
    val groups: ArrayList<String>? = ArrayList(), //groups this user joined， it contains an array of groupID
    val photoUrl : String? = null
){
    fun toMap(): Map<String, Any?>{
        return mapOf(
            "userID" to userID,
            "UserName" to userName,
            "timestamp" to timestamp,
            "friends" to friends,
            "photoUrl" to photoUrl
        )
    }

}
