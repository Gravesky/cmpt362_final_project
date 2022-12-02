package com.example.mychatapp.data

data class Group(
    val groupID : String? = null, //ID of the group
    val members : ArrayList<String>? = ArrayList(), //A group of size >= 2 users
    val latestMSG : String? = null, //the latest message in this group
    val timestamp: Any? = null //Server timestamp of the update
)
