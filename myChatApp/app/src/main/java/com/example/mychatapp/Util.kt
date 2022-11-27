package com.example.mychatapp

import java.time.LocalDateTime
import java.util.Calendar

object Util {
    fun assemblyMessage(msg: String, time: Calendar, sender: String): SingleMessage {
        return SingleMessage(msg,time,sender)
    }

    fun getTimeNow(): Calendar {
        //ref: https://www.baeldung.com/kotlin/current-date-time

        return Calendar.getInstance()
    }
}