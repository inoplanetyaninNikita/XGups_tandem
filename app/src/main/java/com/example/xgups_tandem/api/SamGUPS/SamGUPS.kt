package com.example.xgups_tandem.api.SamGUPS

import com.example.xgups_tandem.BuildConfig
import com.google.gson.GsonBuilder
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface SamGUPS {

    data class Auth(val username: String,
                    val password: String)
    data class AuthResponse(val roleID : String,
                            val human : String,
                            val bookNumber : String,
                            val group : String,
                            val course : String)
    @POST("information/")
    suspend fun login(@Body auth : Auth): Response<String>

    companion object {

        private val converter: GsonConverterFactory = GsonConverterFactory.create(GsonBuilder().setLenient().create())

        /** SamGUPS Client */
        val API: SamGUPS by lazy {
            val okHttpClient = OkHttpClient.Builder().apply {
//                callTimeout(60, TimeUnit.SECONDS)
//                connectTimeout(20, TimeUnit.SECONDS)
//                readTimeout(20, TimeUnit.SECONDS)
//                writeTimeout(20, TimeUnit.SECONDS)
            }

            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor().apply {
                    setLevel(HttpLoggingInterceptor.Level.BODY)
                    okHttpClient.addInterceptor(this)
                }
            }
            val retrofit = Retrofit.Builder()
                .client(okHttpClient.build())
                .baseUrl("https://lid.samgups.ru/")
                .addConverterFactory(converter)
                .build()

            retrofit.create(SamGUPS::class.java)
        }
        fun convertToAuthResponse(response : String) : AuthResponse?
        {
            val moshi = Moshi.Builder().build()
            val adapter = moshi.adapter<Map<String, Any>>(
                Types.newParameterizedType(Map::class.java, String::class.java,
                    Object::class.java)
            )
            val yourMap =  adapter.fromJson(response)

            yourMap!!.mapValues {
                val aa = it.value
                val test = ((aa as ArrayList<Map<String,String>>)[0])

                return AuthResponse(
                    test["roleID"]!!,
                    test["human"]!!,
                    test["bookNumber"]!!,
                    test["group"]!!,
                    test["course"]!!)
            }
            return null
        }

    }



}