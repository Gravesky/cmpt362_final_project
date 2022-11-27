package com.example.mychatapp.data.ChatHistory_Database

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.example.mychatapp.data.UserMessages_Database.UserMessagesRepository
import com.example.mychatapp.data.UserMessages_Database.UserMessagesViewModel
import java.lang.IllegalArgumentException

class ChatHistoryViewModel(private val repository: ChatHistoryRepository): ViewModel() {

    var allChatHistoryLiveData: LiveData<List<ChatHistory>> = repository.allChatHistory.asLiveData()

    fun insert(chatHistory: ChatHistory){
        repository.insert(chatHistory)
    }

    fun deleteAll(){
        val userList = allChatHistoryLiveData.value
        if (userList != null && userList.size > 0)
            repository.deleteAll()
    }


}

class ChatHistoryViewModelFactory(private val repository: ChatHistoryRepository) : ViewModelProvider.Factory{
    override fun<T: ViewModel> create(modelClass: Class<T>) : T{
        if(modelClass.isAssignableFrom(ChatHistoryViewModel::class.java))
            return ChatHistoryViewModel(repository) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}