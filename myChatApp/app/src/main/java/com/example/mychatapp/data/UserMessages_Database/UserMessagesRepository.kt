package com.example.mychatapp.data.UserMessages_Database

import com.example.mychatapp.data.SingleMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class UserMessagesRepository(private val userMessagesDatabaseDao: UserMessagesDatabaseDao) {

    val allUserMessages: Flow<List<UserMessages>> = userMessagesDatabaseDao.getAllUserMessage()

    fun insert(userMessages: UserMessages){
        CoroutineScope(IO).launch {
            userMessagesDatabaseDao.insertMessage(userMessages)
        }
    }

    fun updateMessageHistory(acc_id: String, messages: ArrayList<SingleMessage>){
        CoroutineScope(IO).launch {
            userMessagesDatabaseDao.updateMessageHistory(acc_id, messages)
        }
    }

    fun updateFriendsInfo(userMessages: UserMessagesDatabaseDao.FriendsInfoUpdate){
        CoroutineScope(IO).launch{
            userMessagesDatabaseDao.updateFriendsInfo(userMessages)
        }
    }

    fun getUserMessage(acc_id: String): UserMessages{
        return userMessagesDatabaseDao.getUserMessage(acc_id)
    }

    fun delete(acc_id: String){
        CoroutineScope(IO).launch {
            userMessagesDatabaseDao.deleteUserMessage(acc_id)
        }
    }

    fun deleteAll(){
        CoroutineScope(IO).launch {
            userMessagesDatabaseDao.deleteAll()
        }
    }

}