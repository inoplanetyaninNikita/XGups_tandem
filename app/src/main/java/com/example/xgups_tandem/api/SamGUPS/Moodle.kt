package com.example.xgups_tandem.api.SamGUPS

import android.os.Parcelable
import com.example.xgups_tandem.BuildConfig
import com.google.gson.GsonBuilder
import kotlinx.parcelize.Parcelize
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface Moodle {

    @GET("login/token.php")
    suspend fun login(@Query("username") username: String,
                      @Query("password") password: String,
                      @Query("service") service: String = "moodle_mobile_app"): Response<Token>

    data class Token(
        val privatetoken: String,
        val token: String
    )


    @GET("webservice/rest/server.php")
    suspend fun getUserInfo(
        @Query("wstoken") token: String = Moodle.token,
        @Query("wsfunction") function: String = "core_webservice_get_site_info",
        @Query("moodlewsrestformat") format: String = "json"
    ): Response<UserInfo>

    data class UserInfo(
        val advancedfeatures: List<Advancedfeature>,
        val downloadfiles: Int,
        val firstname: String,
        val fullname: String,
        val functions: List<Function>,
        val lang: String,
        val lastname: String,
        val mobilecssurl: String,
        val release: String,
        val sitecalendartype: String,
        val siteid: Int,
        val sitename: String,
        val siteurl: String,
        val theme: String,
        val uploadfiles: Int,
        val usercalendartype: String,
        val usercanmanageownfiles: Boolean,
        val userhomepage: Int,
        val userid: Int,
        val userissiteadmin: Boolean,
        val usermaxuploadfilesize: Int,
        val username: String,
        val userpictureurl: String,
        val userprivateaccesskey: String,
        val userquota: Int,
        val version: String
    )
    data class Function(
        val name: String,
        val version: String
    )
    data class Advancedfeature(
        val name: String,
        val value: Int
    )

    @GET("webservice/rest/server.php")
    suspend fun getAllCourses(
        @Query("wstoken") token: String = Moodle.token,
        @Query("wsfunction") function: String = "core_enrol_get_users_courses",
        @Query("moodlewsrestformat") format: String = "json",
        @Query("userid") userid: Int = userID
    ): Response<List<Course>>

    data class Course(
        val category: Int,
        val completed: Boolean,
        val completionhascriteria: Boolean,
        val completionusertracked: Boolean,
        val displayname: String,
        val enablecompletion: Boolean,
        val enddate: Int,
        val enrolledusercount: Int,
        val format: String,
        val fullname: String,
        val hidden: Boolean,
        val id: Int,
        val idnumber: String,
        val isfavourite: Boolean,
        val lang: String,
        val lastaccess: Int,
        val marker: Int,
        val overviewfiles: List<Overviewfile>,
        val progress: Double,
        val shortname: String,
        val showactivitydates: Boolean,
        val showcompletionconditions: Boolean,
        val showgrades: Boolean,
        val startdate: Int,
        val summary: String,
        val summaryformat: Int,
        val visible: Int
    )
    data class Overviewfile(
        val filename: String,
        val filepath: String,
        val filesize: Int,
        val fileurl: String,
        val mimetype: String,
        val timemodified: Int
    )

    @GET("webservice/rest/server.php")
    suspend fun getDataOnCourse(
        @Query("wstoken") token: String = Moodle.token,
        @Query("wsfunction") function: String = "core_course_get_contents",
        @Query("moodlewsrestformat") format: String = "json",
        @Query("courseid") courseid: Int
    ): Response<List<CourseElement>>

    data class CourseElement(
        val hiddenbynumsections: Int,
        val id: Int,
        val modules: List<ElementModule>,
        val name: String,
        val section: Int,
        val summary: String,
        val summaryformat: Int,
        val uservisible: Boolean,
        val visible: Int
    )
    data class ElementModule(
        val afterlink: Any,
        val availabilityinfo: String,
        val completion: Int,
        val contents: List<Content>,
        val contentsinfo: Contentsinfo,
        val contextid: Int,
        val customdata: String,
        val dates: List<Date>,
        val description: String,
        val id: Int,
        val indent: Int,
        val instance: Int,
        val modicon: String,
        val modname: String,
        val modplural: String,
        val name: String,
        val noviewlink: Boolean,
        val onclick: String,
        val url: String,
        val uservisible: Boolean,
        val visible: Int,
        val visibleoncoursepage: Int
    )
    data class Date(
        val label: String,
        val timestamp: Int
    )
    data class Contentsinfo(
        val filescount: Int,
        val filessize: Int,
        val lastmodified: Int,
        val mimetypes: List<String>,
        val repositorytype: String
    )

    @Parcelize data class Content(
        val author: String,
        val filename: String,
        val filepath: String,
        val filesize: Int,
        val fileurl: String,
        val isexternalfile: Boolean,
        val license: String,
        val mimetype: String,
        val sortorder: Int,
        val timecreated: Int,
        val timemodified: Int,
        val type: String,
        val userid: Int
    ) : Parcelable

    companion object {

        private val converter: GsonConverterFactory =
            GsonConverterFactory.create(GsonBuilder().setLenient().create())

        var token = ""
        var userID = -1

        /** Moodle Client */
        val API: Moodle by lazy {
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
                .baseUrl("https://lms.samgups.ru/")
                .addConverterFactory(converter)
                .build()

            retrofit.create(Moodle::class.java)
        }
    }

}