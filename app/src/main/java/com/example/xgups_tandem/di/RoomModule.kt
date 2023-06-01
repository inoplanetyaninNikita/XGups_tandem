package com.example.xgups_tandem.di

import android.content.Context
import com.example.xgups_tandem.room.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object RoomModule {
    @Provides
    fun provideRoomService(@ApplicationContext appContext: Context): Room {
        return Room(appContext)
    }
}

class Room(val context : Context){
    val db = androidx.room.Room.databaseBuilder(context, AppDatabase::class.java, "database-name").build()
}