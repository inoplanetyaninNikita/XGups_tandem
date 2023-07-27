package com.example.xgups_tandem.api.SamGUPS

import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.xgups_tandem.BuildConfig
import com.example.xgups_tandem.api.SamGUPS.SamGUPS.Companion.AESDemo.decrypt
import com.example.xgups_tandem.api.SamGUPS.SamGUPS.Companion.AESDemo.encrypt
import com.example.xgups_tandem.api.convertJsonToClass
import com.google.gson.GsonBuilder
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import java.io.UnsupportedEncodingException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.util.*
import java.util.concurrent.TimeUnit
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec


interface SamGUPS {

    data class AuthRequest(val username: String,
                           val password: String)
    @POST("information/")
    suspend fun login(@Body auth : AuthRequest): Response<String>   //Response<AuthResponse>
    //
     @Serializable
    data class AuthResponse(
        val roleID : String,
        val human : String,
        val bookNumber : String,
        val group : String,
        val course : String)

    //{'roleID': 'f17c36fd-ad94-4cc5-990e-fd23b8165e6c', 'human': 'Тороповский Никита Сергеевич', 'bookNumber': '1910-ЭЖД-082', 'group': 'ЭЖД-91', 'course': '3'}
    data class ScheduleRequest(val username: String)
    @POST("student/schedule/")
    suspend fun schedule(@Header ("Cookie") cookie: String,
                         @Body body : ScheduleRequest) : Response<List<List<String>>>
    class ScheduleResponse(response : List<List<String>>) {
        var firstWeek : Week? = null
        var secondWeek : Week? = null
        var thirdWeek : Week? = null

        init {
            var week = mutableListOf<List<String>>()
            try {
            for (i in 0..5)
                week.add(response[i])
            firstWeek = Week(week)
            } catch (ex : Exception){}

            week = mutableListOf()
            try {
                for (i in 6..11)
                    week.add(response[i])
                secondWeek = Week(week)
            } catch (ex : Exception){}


            week = mutableListOf()
            try {
            for (i in 12..17)
                week.add(response[i])
            thirdWeek = Week(week)
            } catch (ex : Exception){}

        }
        class Week(week : List<List<String>>) {
            val days : List<Day>
            fun monday() : Day =  days[0]
            fun tuesday() : Day =  days[1]
            fun wednesday() : Day =  days[2]
            fun thursday() : Day =  days[3]
            fun friday() : Day =  days[4]
            fun saturday() : Day =  days[5]

            init {
                val list = mutableListOf<Day>()
                for (i in 0..5)
                    list.add(Day(week[i]))
                days = list
            }
            class Day(lessons : List<String>) {
                var date : String = lessons[0]
                val localDate : LocalDate
                lateinit var lessons : List<Lesson>
                init {
                    val list = mutableListOf<Lesson>()

                    date = date.replace("января",",01")
                        .replace("февраля",",02")
                        .replace("марта",",03")
                        .replace("апреля",",04")
                        .replace("мая",",05")
                        .replace("июня",",06")
                        .replace("июля",",07")
                        .replace("августа",",08")
                        .replace("сентября",",09")
                        .replace("октября",",10")
                        .replace("ноября",",11")
                        .replace("декабря",",12")
                    localDate = LocalDate.parse("${LocalDate.now().year}-${date.split(",")[2]}-${date.split(",")[1].replace("  ","")}")

                    for (i in 1 until lessons.size) {
                        if(lessons[i].isNotEmpty())
                        {
                            val lesson = getLesson(i,lessons[i], localDate)
                            list.add(lesson)
                        }

                    }
                    this.lessons = list
                }
                fun getLesson(number : Int, text : String, localDate: LocalDate) : Lesson {
                    val words = text.split(" ")

                    val removeWordsInFuture = mutableListOf<Int>()
                    val teachers = mutableListOf<String>()
                    val auditorium = mutableListOf<String>()
                    val lessons = mutableListOf<String>()

                    for (i in words.indices) {
                        val word = words[i]
                        var dotCount = 0

                        for (charItem in word) {
                            if (charItem == '.') {
                                dotCount++
                            }
                        }

                        if (dotCount == 2) {
                            teachers.add(words[i - 1] + " " + words[i])
                            auditorium.add(words[i + 1])

                            removeWordsInFuture.add(i)
                            removeWordsInFuture.add(i - 1)
                            removeWordsInFuture.add(i + 1)
                        }
                    }

                    var lesson = ""
                    var lastRemoveIndex = -2

                    for (i in words.indices) {
                        if (lastRemoveIndex == i - 1 && !removeWordsInFuture.contains(i)) {
                            lessons.add(lesson)
                            lesson = ""
                        }
                        if (!removeWordsInFuture.contains(i)) {
                            lesson += words[i] + " "
                        } else {
                            lastRemoveIndex = i
                        }
                    }

                    val startTime : LocalTime
                    lessons.add(lesson)
                    when (number)
                    {
                        1 -> {
                            startTime =  LocalTime.parse("08:30")
                        }
                        2 -> {
                            startTime =  LocalTime.parse("10:15")
                        }
                        3 -> {
                            startTime =  LocalTime.parse("12:10")
                        }
                        4 -> {
                            startTime =  LocalTime.parse("13:55")
                        }
                        5 -> {
                            startTime =  LocalTime.parse("15:35")
                        }
                        6 -> {
                            startTime =  LocalTime.parse("17:15")
                        }
                        7 -> {
                            startTime =  LocalTime.parse("18:55")
                        }
                        else -> {
                            startTime =  LocalTime.parse("00-00")
                        }

                    }
                    return Lesson(lessons[0], auditorium[0], teachers[0], localDate.atTime(startTime), localDate.atTime(startTime.plusHours(1).plusMinutes(30)))
                }
            }
        }
        class Lesson(val name : String, val auditorium : String, val teacher : String, val timeStart: LocalDateTime, val timeFinish : LocalDateTime ) {
        }

        fun getDayByDate(localDate: LocalDate) : Week.Day?
        {
            var week: Week? = firstWeek
            if (week == null) return null
            for (day in week!!.days) {
                if(localDate.month == day.localDate.month &&
                    localDate.dayOfMonth == day.localDate.dayOfMonth) return day
            }

            week = secondWeek
            if (week == null) return null
            for (day in week!!.days) {
                if(localDate.month == day.localDate.month &&
                    localDate.dayOfMonth == day.localDate.dayOfMonth) return day
            }

            week = thirdWeek
            if (week == null) return null
            for (day in week!!.days) {
                if(localDate.month == day.localDate.month &&
                    localDate.dayOfMonth == day.localDate.dayOfMonth) return day
            }

            return null
        }
    }

    data class MarksRequest(val username: String,
                            val roleID: String)
    @POST("student/marks/")
    suspend fun marks(@Header ("Cookie") cookie: String,
                      @Body body : MarksRequest) : Response<String> //Response<List<MarkResponse>>
    @Parcelize
    data class MarkResponse(val documentName : String,
                            val name : String,
                            val mark : String) : Parcelable

    companion object {

        private val converter: GsonConverterFactory =
            GsonConverterFactory.create(GsonBuilder().setLenient().create())

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
      fun jsonToAuthResponse(jsonString: String): AuthResponse {
          return Json.decodeFromString(jsonString)
      }
        fun AESDecrypt(login: String, cryptoText: String): String? {
            return decrypt(cryptoText, getKeyForDecrypt(login))
        }

        //region Шифрование
        private fun getKeyForDecrypt(login: String): String {
            val date = LocalDate.now()
                .atStartOfDay()
                .toEpochSecond(ZoneOffset.UTC)

            val base64date = Base64.getEncoder()
                .encodeToString(
                    date.toString()
                        .toByteArray(charset("UTF-8"))
                )

            val key = encrypt(login, base64date)
            return key.toString()
        }

        private object AESDemo {
            private var secretKey: SecretKeySpec? = null
            private lateinit var key: ByteArray

            // set Key
            fun setKey(myKey: String) {
                var sha: MessageDigest? = null
                try {
                    key = myKey.toByteArray(charset("UTF-8"))
                    sha = MessageDigest.getInstance("SHA-256")
                    key = sha.digest(key)
                    key = Arrays.copyOf(key, 16)
                    secretKey = SecretKeySpec(key, "AES")
                } catch (e: NoSuchAlgorithmException) {
                    e.printStackTrace()
                } catch (e: UnsupportedEncodingException) {
                    e.printStackTrace()
                }
            }

            @SuppressLint("GetInstance")
            fun encrypt(strToEncrypt: String, secret: String): String? {
                try {
                    setKey(secret)
                    val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
                    cipher.init(Cipher.ENCRYPT_MODE, secretKey)
                    return Base64.getEncoder().encodeToString(
                        cipher.doFinal
                            (strToEncrypt.toByteArray(charset("UTF-8")))
                    )
                } catch (e: Exception) {

                    println("Error while encrypting: $e")
                }
                return null
            }

            // method to encrypt the secret text using key
            fun decrypt(strToDecrypt: String?, secret: String): String? {
                try {
                    setKey(secret)
                    val cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING")
                    cipher.init(Cipher.DECRYPT_MODE, secretKey)
                    return String(
                        cipher.doFinal(
                            Base64.getDecoder().decode(strToDecrypt)
                        )
                    )
                } catch (e: Exception) {
                    println("Error while decrypting: $e")
                }
                return null
            }


        }
        //endregion
    }
}