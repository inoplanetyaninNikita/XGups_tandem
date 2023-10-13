package com.example.xgups_tandem.ui.schedule.dayadapter

import java.time.LocalDateTime

data class DayModel(val localDate: LocalDateTime,
                    var status: DayStatus = DayStatus.HOLIDAY)

enum class DayStatus{
    HOLIDAY,
    TODAY,
    SELECT,
    NONE,
    NOHOLIDAY
}
