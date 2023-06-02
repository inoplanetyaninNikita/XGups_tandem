package com.example.xgups_tandem.services

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.messaging.ktx.remoteMessage
import javax.inject.Inject

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class PushFirebase : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("Gwegwe", "Refreshed token: $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val intent = Intent("PUSH_EVENT")
        message.data.forEach{
            intent.putExtra(it.key, it.value)
        }
        sendBroadcast(intent)
    }

}