package com.example.mychatapp

//This class contains the information of a single message

import java.util.*

class SingleMessage(Message: String, Time : Calendar, Sender : String) {
    val message : String = Message //stores the message
    val time : Calendar = Time //stores the time when this message was sent
    val sender : String = Sender //stores the acc_id of the sender of this message
    //sender is essential because a chat is among two people!

}