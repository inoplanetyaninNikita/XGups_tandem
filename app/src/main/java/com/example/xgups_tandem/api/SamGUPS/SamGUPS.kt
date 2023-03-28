package com.example.xgups_tandem.api.SamGUPS

import com.example.xgups_tandem.BuildConfig
import com.example.xgups_tandem.api.convertJsonToClass
import com.google.gson.GsonBuilder
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

interface SamGUPS {
    /**Отправляем данные на сервер для авторизации
     * @param username email.
     * @param password пароль.
     * */
    data class AuthRequest(val username: String,
                           val password: String)
    /** Тип нужен, чтобы привести данные с [login] в нормальный вид. Нужно воспользоваться [convertToAuthResponse]. */
    data class AuthResponse(val roleID : String,
                            val human : String,
                            val bookNumber : String,
                            val group : String,
                            val course : String)
    @POST("information/")
    /** Чтобы привести [Response] к определенному типу, нужно прогнать его через [convertToAuthResponse]. */
    suspend fun login(@Body auth : AuthRequest): Response<String>
    data class ScheduleRequest(val group: String)
    class ScheduleResponse(response : List<List<String>>) {
        val even : Week
        val notEven : Week
        init {
            var listEven = mutableListOf<List<String>>()
            for (i in 0..5)
                listEven.add(response[i])
            even = Week(listEven)

            var listNotEven = mutableListOf<List<String>>()
            for (i in 6..11)
                listNotEven.add(response[i])
            notEven = Week(listNotEven)
        }
        class Week(week : List<List<String>>) {
            val days : List<Day>
            init {
                val list = mutableListOf<Day>()
                for (i in 0..5)
                    list.add(Day(week[i]))
                days = list
            }
            class Day(lessons : List<String>) {
                val date : String = lessons[0]
                lateinit var lessons : List<Lesson>
                init {
                    try {
                        val list = mutableListOf<Lesson>()
                        for (i in 1..lessons.size - 1)
                            list.add(Lesson(lessons[i]))
                        this.lessons = list
                    }
                    catch (ex : java.lang.Exception)
                    {

                    }
                }
            }
        }
        class Lesson(fullName : String) {
            val name : String = fullName
        }
    }
    @POST("student/schedule/")
    suspend fun schedule(@Body group: ScheduleRequest) : Response<List<List<String>>>
    companion object {

        private val converter: GsonConverterFactory = GsonConverterFactory.create(GsonBuilder().setLenient().create())

        /** SamGUPS Client */
        val API: SamGUPS by lazy {
            val okHttpClient = OkHttpClient.Builder().apply {
                callTimeout(60, TimeUnit.SECONDS)
                connectTimeout(60, TimeUnit.SECONDS)
                readTimeout(60, TimeUnit.SECONDS)
                writeTimeout(60, TimeUnit.SECONDS)
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
        /** Переводит [String] к типу [AuthResponse], сделано так, потому что работаю с динамическим ключом в JSON, а [String.convertJsonToClass] выбивает в этом случае [Exception].*/
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