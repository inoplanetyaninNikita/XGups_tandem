package com.example.xgups_tandem.ui.grades

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GradesViewModel : ViewModel() {
    val image : MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
}