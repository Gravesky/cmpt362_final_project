package com.example.mychatapp.data.ChatHistory_Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.mychatapp.data.Converters
import com.example.mychatapp.data.UserMessages_Database.UserMessagesDatabase

@Database(entities = [ChatHistory::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class ChatHistoryDatabase : RoomDatabase(){
    abstract val chatHistoryDatabaseDao : ChatHistoryDatabaseDao

    companion object{
        private var INSTANCE: ChatHistoryDatabase? = null

        fun getInstance(context: Context) : ChatHistoryDatabase {
            synchronized(this){
                var instance = INSTANCE
                if(instance == null){
                    instance = Room.databaseBuilder(context.applicationContext,
                        ChatHistoryDatabase::class.java, "Chat_Messages_Table").build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}