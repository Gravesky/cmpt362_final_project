package com.example.mychatapp.data.UserMessages_Database

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewmodel.CreationExtras
import java.lang.IllegalArgumentException

class UserMessagesViewModel(private val repository: UserMessagesRepository): ViewModel() {

    var allUserMessagesLiveData: LiveData<List<UserMessages>> = repository.allUserMessages.asLiveData()

    fun insert(userMessages: UserMessages){
        repository.insert(userMessages)
    }

    fun deleteAll(){
        val userList = allUserMessagesLiveData.value
        if (userList != null && userList.size > 0)
            repository.deleteAll()
    }


}

class UserMessagesViewModelFactory(private val repository: UserMessagesRepository) : ViewModelProvider.Factory{
    override fun<T: ViewModel> create(modelClass: Class<T>,extras: CreationExtras) : T{
        if(modelClass.isAssignableFrom(UserMessagesViewModel::class.java))
            return UserMessagesViewModel(repository) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}