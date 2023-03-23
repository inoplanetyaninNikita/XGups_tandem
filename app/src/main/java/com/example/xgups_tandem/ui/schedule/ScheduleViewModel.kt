package com.example.xgups_tandem.ui.schedule

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.xgups_tandem.ui.schedule.dayadapter.DayModel
import java.time.LocalDateTime

class ScheduleViewModel : ViewModel() {
    val dateList : MutableLiveData<List<DayModel>> by lazy {
        MutableLiveData<List<DayModel>>()
    }
    val lessonList : MutableLiveData<List<Lesson>> by lazy {
        MutableLiveData<List<Lesson>>()
    }

    init {
        setLessonList()
        setDayList()
    }

    private fun setLessonList()
    {
        val list = mutableListOf<Lesson>()
        list.add(Lesson("nice","Москвичев","9319", LocalDateTime.now(),LocalDateTime.now()))
        lessonList.value = list
    }

    private fun setDayList()
    {
        val list = mutableListOf<DayModel>()
        val firstDay = LocalDateTime.now().minusDays(3)

        repeat(40) {
            list.add(DayModel(firstDay.plusDays(it.toLong())))
        }
        dateList.value = list
    }

    data class Lesson(val lessonName : String,
                      val teacher : String,
                      val auditorium : String,
                      val startTime : LocalDateTime,
                      val endTime : LocalDateTime)
}