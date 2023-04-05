package com.example.xgups_tandem

import android.app.Application
import androidx.room.Room.databaseBuilder
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class App: Application() {
    override fun onCreate() {
        super.onCreate()
    }
}