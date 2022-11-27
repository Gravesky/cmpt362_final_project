package com.example.mychatapp.data.ChatHistory_Database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatHistoryDatabaseDao {
    /**
     * Add a new chat
     */
    @Insert
    suspend fun insertChat(chatHistory: ChatHistory)

    /**
     * Get all chat histories with descending time order: newest -> oldest
     */
    @Query("SELECT * FROM Chat_Messages_Table ORDER BY last_message_time DESC")
    fun getAllChatHistory(): Flow<List<ChatHistory>>

    /**
     * Delete all chat histories
     */
    @Query("DELETE FROM Chat_Messages_Table")
    suspend fun deleteAll()

    /**
     * Delete an individual chat history
     */
    @Query("DELETE FROM Chat_Messages_Table WHERE account_id_column = :acc_id")
    suspend fun deleteChatHistory(acc_id: String)
}