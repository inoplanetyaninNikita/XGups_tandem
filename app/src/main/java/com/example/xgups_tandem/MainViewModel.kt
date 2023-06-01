package com.example.xgups_tandem

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.room.*
import com.example.xgups_tandem.api.SamGUPS.SamGUPS
import com.example.xgups_tandem.di.PushNotification
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel  @Inject constructor() : ViewModel() {

    val schedule : MutableLiveData<SamGUPS.ScheduleResponse> by lazy {
        MutableLiveData<SamGUPS.ScheduleResponse>()
    }
    val user : MutableLiveData<UserData> by lazy {
        MutableLiveData<UserData>()
    }
    val marks : MutableLiveData<List<SamGUPS.MarkResponse>> by lazy {
        MutableLiveData<List<SamGUPS.MarkResponse>>()
    }
    val login : MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
}

data class UserData(val secondName: String,
                    val firstName: String,
                    val thirdName: String,
                    val bookNumber: String,
                    val group: String,
                    val roleID : String,
                    val course : String)