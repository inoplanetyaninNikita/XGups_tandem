package com.example.xgups_tandem.ui.help

data class Message(val itsMe : Boolean,
                   val username : String,
                   val text: String,
                   val url : String = "" )
