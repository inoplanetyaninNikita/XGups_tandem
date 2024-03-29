package com.example.xgups_tandem.ui.schedule

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.xgups_tandem.ui.schedule.dayadapter.DayModel
import com.example.xgups_tandem.api.SamGUPS.SamGUPS.ScheduleResponse
import com.example.xgups_tandem.ui.schedule.lessonAdapter.LessonModel
import java.time.LocalDate
import java.time.LocalDateTime

class ScheduleViewModel : ViewModel() {

    //region Profile

    val name : MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val group : MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val image : MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    //endregion
    //region DayAdapter

    val dateList : MutableLiveData<List<DayModel>> by lazy {
        MutableLiveData<List<DayModel>>()
    }
    private lateinit var today: DayModel

    fun getToday() : DayModel = today
    private fun setDayList() {
        val list = mutableListOf<DayModel>()
        val firstDay = LocalDateTime.now().minusDays(10)
        repeat(40) {

            val day = DayModel(firstDay.plusDays(it.toLong()))
            if(day.localDate.dayOfYear == LocalDate.now().dayOfYear) today = day
            list.add(day)
        }

        dateList.value = list
    }
    //endregion
    //region LessonAdapter - отображение пар

    val lessonList : MutableLiveData<List<LessonModel>> by lazy {
        MutableLiveData<List<LessonModel>>()
    }
    private val lessonListValue = mutableListOf<LessonModel>()

    fun viewLessonsOnDay(day : ScheduleResponse.Week.Day) {
        lessonListValue.clear()

        for (item in day.lessons) {
            if (item.name.isEmpty()) continue
            lessonListValue.add(LessonModel(item.name,item.teacher,item.auditorium, item.timeStart, item.timeFinish))
        }

        lessonList.value = lessonListValue
    }
    fun clear() {
        lessonListValue.clear()
        lessonList.value = lessonListValue
    }
    //endregion

    init {
        setDayList()
        lessonList.value = lessonListValue
    }

}