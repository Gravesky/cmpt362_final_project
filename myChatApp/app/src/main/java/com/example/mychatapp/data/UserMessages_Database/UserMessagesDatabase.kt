package com.example.mychatapp.data.UserMessages_Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.mychatapp.data.Converters

@Database(entities = [UserMessages::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class UserMessagesDatabase : RoomDatabase() {
    abstract val userMessagesDatabaseDao: UserMessagesDatabaseDao

    companion object{
        @Volatile
        private var INSTANCE: UserMessagesDatabase? = null

        fun getInstance(context: Context) : UserMessagesDatabase{
            synchronized(this){
                var instance = INSTANCE
                if(instance == null){
                    instance = Room.databaseBuilder(context.applicationContext,
                        UserMessagesDatabase::class.java, "User_Messages_Table").build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}