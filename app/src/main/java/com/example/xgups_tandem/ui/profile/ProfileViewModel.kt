package com.example.xgups_tandem.ui.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProfileViewModel : ViewModel() {

    //Данные о чуваке
    val name : MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val group : MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val image : MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

}