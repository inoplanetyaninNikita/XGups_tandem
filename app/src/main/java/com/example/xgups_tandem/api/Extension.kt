package com.example.xgups_tandem.api

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

/** Конвертировать из JSON строки в любой [T]-класс. */
inline fun <reified T> String.convertJsonToClass() : T
{
    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
    val jsonAdapter: JsonAdapter<T> = moshi.adapter(T::class.java)
    return  jsonAdapter.fromJson(this)!!
}

/** Конвертировать из [Any]-класса в строчку JSON. */
inline fun Any.convertClassToJson(): String
{
    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
    val jsonAdapter: JsonAdapter<Any> = moshi.adapter(Any::class.java)
    return jsonAdapter.toJson(this)
}
