package com.example.mychatapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MessageViewModel: ViewModel() {
    private val _messageHistory = MutableLiveData<ArrayList<SingleMessage>>()
    val messageHistory: LiveData<ArrayList<SingleMessage>>
        get() {
            return _messageHistory
        }

    fun putNewMessage(nerMsg: SingleMessage){
        var tempArrayList = ArrayList<SingleMessage>()
        if (_messageHistory.value != null) {
            tempArrayList = _messageHistory.value!!
        }
        tempArrayList.add(nerMsg)
        _messageHistory.postValue(tempArrayList)
    }
}