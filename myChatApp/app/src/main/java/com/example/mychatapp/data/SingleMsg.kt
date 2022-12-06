package com.example.mychatapp.data

import android.net.Uri
import java.sql.Timestamp

data class SingleMsg(
    val groupID : String? = null, //A group of size >= 2 users
    val message : String? = null, //The message that is being sent
    val speakerID : String? = null, //The ID of the speaker of the message
    val userName: String? = null, //user customized name
    val timestamp: Any? = null, //The time when this message was being sent
    val speakerPhotoUrl: String? = null, // The photo url of the speaker
    val imageUrl: String? = null, // The photo url of the message
)
