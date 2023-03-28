package com.example.xgups_tandem.ui.schedule

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.xgups_tandem.api.SamGUPS.SamGUPS
import com.example.xgups_tandem.ui.schedule.dayadapter.DayModel
import com.example.xgups_tandem.api.SamGUPS.SamGUPS.ScheduleResponse
import java.time.LocalDateTime

class ScheduleViewModel : ViewModel() {
    val dateList : MutableLiveData<List<DayModel>> by lazy {
        MutableLiveData<List<DayModel>>()
    }
    val lessonList : MutableLiveData<List<Lesson>> by lazy {
        MutableLiveData<List<Lesson>>()
    }
    val name : MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val image : MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    private val lesson_list = mutableListOf<Lesson>()
    init {
        setLessonList()
        setDayList()
    }

    private fun setLessonList()
    {
        lesson_list.add(Lesson("nice","Москвичев","9319", LocalDateTime.now(),LocalDateTime.now()))
        lessonList.value = lesson_list
    }
    fun viewLessonsOnDay(day : ScheduleResponse.Week.Day)
    {
        lesson_list.clear()
        for (item in day.lessons)
        {
            lesson_list.add(Lesson(item.name,"Москвичев","9319", LocalDateTime.now(),LocalDateTime.now()))
        }
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

    data class Lesson(val lessonName : String,
                      val teacher : String,
                      val auditorium : String,
                      val startTime : LocalDateTime,
                      val endTime : LocalDateTime)
}