package com.example.xgups_tandem.ui.help

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

import com.example.xgups_tandem.api.TestCreator.EasyTestCreator
import com.example.xgups_tandem.api.TestCreator.EasyTestCreator.Question
import com.example.xgups_tandem.api.TestCreator.EasyTestCreator.Answer
import com.example.xgups_tandem.api.TestCreator.EasyTestCreator.OpenAI

class HelpViewModel : ViewModel() {

    val messages : MutableLiveData<List<Message>> by lazy {
        MutableLiveData<List<Message>>() }
    val messageList = mutableListOf<Message>()

    private val script = OpenAI.OpenAIScript()

    private val scheduleAnswer = Answer("О Расписании") {
        addToMessageList(Message(
            false,
            "XGPT",
            "Вы можете посмотреть расписание на сайте.\nНажмите на кнопку, чтоб перейти на сайт.",
            "https://www.samgups.ru/raspisanie/raspisanie-studentov-ochnoy-i-ochno-zaochnoy-form/"))

    }
    private val marksAnswer = Answer("Об Оценках") {
        addToMessageList(Message(
            false,
            "XGPT",
            "Вы можете посмотреть оценки за сессии на сайте.\nНажмите на кнопку, чтоб перейти на сайт.",
            "https://www.samgups.ru/students/uspevaimost/"))
    }

    private val testQuest : Question = OpenAI.TestQuestion(listOf(marksAnswer, scheduleAnswer))

    init {
        messages.value = messageList
        script.startQuestion = testQuest
        testQuest.setOnNotCorrectAnswerListner =  {
            addToMessageList(Message(
                false,
                "XGPT",
                "Я тебя не понял :( ",))
        }
    }

    fun sendMessage(text : String) {
        addToMessageList(Message(true, "username", text))
        sendMessageToGPT(text)
    }

    private fun addToMessageList(message: Message){
        viewModelScope.launch(Dispatchers.Main) {
            messageList.add(message)
            messages.value = messageList
        }
    }

    private fun sendMessageToGPT(text : String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                testQuest.setOnCreateListner = {
                    it.textQuestion = text
                }
                script.start()
            }
            catch (ex : Exception){
                addToMessageList(Message(
                    false,
                    "XGPT",
                    "Непредвиденная ошибка, напишите попозже :("))
            }

        }
    }
}