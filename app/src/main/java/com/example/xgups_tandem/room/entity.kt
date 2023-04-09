package com.example.xgups_tandem.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "first_name") val firstName: String?,
    @ColumnInfo(name = "last_name") val lastName: String?
)

@Entity
data class Auth(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "roleID")val roleID : String,
    @ColumnInfo(name = "name") val human : String,
    @ColumnInfo(name = "book_number") val bookNumber : String,
    @ColumnInfo(name = "group") val group : String,
    @ColumnInfo(name = "course") val course : String)