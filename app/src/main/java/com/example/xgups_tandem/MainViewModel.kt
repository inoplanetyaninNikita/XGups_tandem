package com.example.xgups_tandem

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.xgups_tandem.api.SamGUPS.SamGUPS

class MainViewModel : ViewModel() {
    val schedule : MutableLiveData<SamGUPS.ScheduleResponse> by lazy {
        MutableLiveData<SamGUPS.ScheduleResponse>()
    }
}