package com.example.xgups_tandem.ui.schedule.lessonAdapter

import java.time.LocalDateTime

data class LessonModel(val lessonName : String,
                       val teacher : String,
                       val auditorium : String,
                       val startTime : LocalDateTime,
                       val endTime : LocalDateTime
)
