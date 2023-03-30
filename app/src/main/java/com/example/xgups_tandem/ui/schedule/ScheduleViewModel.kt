package com.example.xgups_tandem.ui.schedule

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.xgups_tandem.ui.schedule.dayadapter.DayModel
import com.example.xgups_tandem.api.SamGUPS.SamGUPS.ScheduleResponse
import com.example.xgups_tandem.ui.schedule.lessonAdapter.LessonModel
import java.time.LocalDateTime

class ScheduleViewModel : ViewModel() {
    val dateList : MutableLiveData<List<DayModel>> by lazy {
        MutableLiveData<List<DayModel>>()
    }
    val lessonList : MutableLiveData<List<LessonModel>> by lazy {
        MutableLiveData<List<LessonModel>>()
    }
    val name : MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val image : MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    private val lesson_list = mutableListOf<LessonModel>()
    init {
        setLessonList()
        setDayList()
    }

    private fun setLessonList()
    {
        lesson_list.add(LessonModel("nice","Москвичев","9319", LocalDateTime.now(),LocalDateTime.now()))
        lessonList.value = lesson_list
    }
    fun viewLessonsOnDay(day : ScheduleResponse.Week.Day)
    {
        lesson_list.clear()
        for (item in day.lessons)
        {
            if (item.name.isEmpty()) continue
            lesson_list.add(LessonModel(item.name,"Москвичев","9319", LocalDateTime.now(),LocalDateTime.now()))
        }
        lessonList.value = lesson_list
    }
    fun clear()
    {
        lesson_list.clear()
        lessonList.value = lesson_list
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


}