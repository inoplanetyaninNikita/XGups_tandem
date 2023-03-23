package com.example.xgups_tandem.api.SamGUPS

import com.example.xgups_tandem.BuildConfig
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

interface SamGUPS {

    data class Auth(val username: String,
                    val password: String)
    data class AuthResponse(val username: String,
                            val password: String,
                            val first_token_piece: String,
                            val second_token_piece: String,
    )

    @POST("information/")
    suspend fun login(@Body auth : Auth): Response<AuthResponse>

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

    }



}