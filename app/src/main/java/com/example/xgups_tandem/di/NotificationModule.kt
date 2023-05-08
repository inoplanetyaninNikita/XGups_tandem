package com.example.xgups_tandem.di

import android.app.*
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.example.xgups_tandem.api.convertClassToJson
import com.example.xgups_tandem.services.NotificationAlarmManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.*


@Module
@InstallIn(SingletonComponent::class)
object PushNotificationModule {
    @Provides
    fun provideAnalyticsService(@ApplicationContext appContext: Context): PushNotification {
        return PushNotification(appContext)
    }
}

class PushNotification(val context : Context){

    private val notificationManager = context.getSystemService(NotificationManager::class.java)
    fun show(notification: PushNotificationData, triggerTime : Int = 0, id : Int = 0){

        val alarmManager = context.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager

        //region Intent

        val notifyClass = PushNotificationData::class.java
        val intent = Intent(context, NotificationAlarmManager::class.java)
        intent.putExtra("${notifyClass}ID", id)
        intent.putExtra(notifyClass.name, notification.convertClassToJson())


        //endregion

        val pendingIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_MUTABLE)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + triggerTime * 1000, pendingIntent)
    }

    fun cancelALL() {
        notificationManager.cancelAll()
    }
}

data class PushNotificationData(
    val title: String = "",
    val text: String = "",
    val smallIcon: Int? = null,
    val progressBar: ProgressBar? = null,
    val silent: Boolean = false)
{
    data class ProgressBar(val max : Int = 0, val progress: Int = 0, val intermidate: Boolean = true)
}

