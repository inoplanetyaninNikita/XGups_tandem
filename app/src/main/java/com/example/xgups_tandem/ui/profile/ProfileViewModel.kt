package com.example.xgups_tandem.ui.profile

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

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