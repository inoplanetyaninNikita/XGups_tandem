package com.example.xgups_tandem

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.xgups_tandem.api.SamGUPS.SamGUPS

class MainViewModel : ViewModel() {
    val schedule : MutableLiveData<SamGUPS.ScheduleResponse> by lazy {
        MutableLiveData<SamGUPS.ScheduleResponse>()
    }
    val user : MutableLiveData<UserData> by lazy {
        MutableLiveData<UserData>()
    }
}
data class UserData(val secondName: String,
                    val firstName: String,
                    val thirdName: String,
                    val bookNumber: String,
                    val group: String,
                    val roleID : String,
                    val course : String)