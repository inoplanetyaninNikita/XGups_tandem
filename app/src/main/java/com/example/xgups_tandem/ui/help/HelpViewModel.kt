package com.example.xgups_tandem.ui.help

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HelpViewModel : ViewModel() {

    val messages : MutableLiveData<List<Message>> by lazy {
        MutableLiveData<List<Message>>() }
    val messageList = mutableListOf<Message>()

    init {
        messages.value = messageList
    }
    fun sendMessage(text : String) {
        messageList.add(Message(true, "username", text))
        messages.value = messageList
    }
}