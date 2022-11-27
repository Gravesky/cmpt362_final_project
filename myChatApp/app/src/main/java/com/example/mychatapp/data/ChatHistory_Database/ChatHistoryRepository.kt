package com.example.mychatapp.data.ChatHistory_Database

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ChatHistoryRepository(private  val chatHistoryDatabaseDao: ChatHistoryDatabaseDao) {

    val allChatHistory: Flow<List<ChatHistory>> = chatHistoryDatabaseDao.getAllChatHistory()

    /**
     * Add a new chat
     */
    fun insert(chatHistory: ChatHistory){
        CoroutineScope(IO).launch {
            chatHistoryDatabaseDao.insertChat(chatHistory)
        }
    }

    /**
     * Delete all chat histories
     */
    fun deleteAll(){
        CoroutineScope(IO).launch {
            chatHistoryDatabaseDao.deleteAll()
        }
    }

    /**
     * Delete an individual chat history
     */
    fun delete(acc_id: String){
        CoroutineScope(IO).launch{
            chatHistoryDatabaseDao.deleteChatHistory(acc_id)
        }
    }



}