package com.example.xgups_tandem.services

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.xgups_tandem.R
import com.example.xgups_tandem.api.convertJsonToClass
import com.example.xgups_tandem.di.PushNotificationData

class NotificationAlarmManager : BroadcastReceiver() {

    private val channelId = "xgups.apps.notifications"
    private lateinit var notificationManager: NotificationManager

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return

        //region Intent

        val notifyClass = PushNotificationData::class.java
        val notificationID =  intent.getIntExtra("${notifyClass.name}ID",0)
        val notification = intent.getStringExtra(notifyClass.name)?.convertJsonToClass<PushNotificationData>()  ?: return

        //endregion

        //region Builder

        //region Init

        var builder = NotificationCompat.Builder(context, channelId)
            .setContentTitle(notification.title)
            .setContentText(notification.text)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        //endregion

        //region Small Icon

        if (notification.smallIcon != null) builder =  builder.setSmallIcon(notification.smallIcon)
        else builder = builder.setSmallIcon(R.drawable.ic_launcher_foreground)

        //endregion
        //region Progress bar

        if(notification.progressBar != null) builder = builder.setProgress(notification.progressBar.max,
            notification.progressBar.progress, notification.progressBar.intermidate)

        //endregion
        //region Silent

        builder = builder.setSilent(notification.silent)

        //endregion

        //endregion

        notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.notify(notificationID, builder.build())

    }
}
