package com.example.mychatapp.data.UserMessages_Database

import androidx.annotation.NonNull
import androidx.room.*
import com.example.mychatapp.data.SingleMessage
import kotlinx.coroutines.flow.Flow

@Dao
interface UserMessagesDatabaseDao{
    /**
     * Add a new friend
     */
    @Insert
    suspend fun insertMessage(userMessages: UserMessages)

    /**
     * Update message history of an individual friend
     */
    @Query("UPDATE User_Messages_Table SET message_history_column = :messages WHERE account_id_column = :acc_id")
    suspend fun updateMessageHistory(acc_id: String, messages: ArrayList<SingleMessage>)

    @Entity
    data class FriendsInfoUpdate(

        @ColumnInfo(name = "account_id_column")
        var acc_id: String, //User can have their preferred account id, and they are unique in the chat app world

        @ColumnInfo(name = "name_column")
        var name: String,

        @ColumnInfo(name = "email_column")
        var email: String,

        @ColumnInfo(name = "phone_number_column")
        var phone: String,

        @ColumnInfo(name = "profile_pic_column")
        var profile_pic: String //This stores the URI of the profile picture
    )

    /**
     * Update an individual friends' info
     */
    @Update(entity = UserMessages::class)
    suspend fun updateFriendsInfo(obj: FriendsInfoUpdate)

    /**
     * Get an individual friend's information and message history
     */
    @Query("SELECT * FROM User_Messages_Table WHERE account_id_column = :acc_id")
    fun getUserMessage(acc_id: String): UserMessages

    /**
     * Get all friends
     */
    @Query("SELECT * FROM User_Messages_Table")
    fun getAllUserMessage(): Flow<List<UserMessages>>

    /**
     * Delete all friends
     */
    @Query("DELETE FROM User_Messages_Table")
    suspend fun deleteAll()

    /**
     * Delete an individual friend
     */
    @Query("DELETE FROM User_Messages_Table WHERE account_id_column = :acc_id")
    suspend fun deleteUserMessage(acc_id: String)
}
