package com.example.mychatapp.data

import android.icu.util.Calendar
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.mychatapp.data.SingleMessage
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    //Converter functions for message_history
    @TypeConverter
    fun fromUserMessagesArrayList(value: ArrayList<SingleMessage>):String{
        return Gson().toJson(value).toString()
    }

    @TypeConverter
    fun toUserMessagesArrayList(value : String): ArrayList<SingleMessage>{
        val listType = object : TypeToken<ArrayList<SingleMessage>>(){}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromTimestamp(value: Long): Calendar {
        val myCalendar: Calendar = Calendar.getInstance()
        myCalendar.timeInMillis = value*1000L
        return myCalendar
    }

    @TypeConverter
    fun calenderToTimestamp(myCalendar: Calendar): Long {
        return myCalendar.timeInMillis/1000L
    }

}