package com.example.xgups_tandem.api.ADFS

import com.example.xgups_tandem.BuildConfig
import com.example.xgups_tandem.MainActivity
import com.example.xgups_tandem.api.SamGUPS.SamGUPS
import com.example.xgups_tandem.api.convertJsonToClass
import com.example.xgups_tandem.ui.login.LoginViewModel
import com.example.xgups_tandem.ui.schedule.ScheduleFragment
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.HashMap

/** Подключение к [ADFS],
 * нужно для авторизации пользователя в систему. В приложении оно нужно для шифрования данных.
 * Чтобы отправить данные,нужно создать хэшмапу с помощью [getHashMapForLogin] и кинуть ее в [login].
 * Если авторизация прошла успешно, то получаем [ADFSTokenResponse], а в нем мы можем
 * раскодировать данные из [ADFSTokenResponse.accessToken] с помощью [ADFSTokenResponse.getADFSUser]
 * @author Nikita Toropovsky
 * @sample LoginViewModel.loginToADFS
 * @since 23.03.2023
 * @see SamGUPS.API
 * */
interface ADFS {

    /**Ответ сервера со статус кодом *200* при [ADFS.login]. */
    data class ADFSTokenResponse(
        @SerializedName("access_token") val accessToken: String,
        @SerializedName("token_type") val tokenType: String,
        @SerializedName("expires_in") val expiresIn: String,
        @SerializedName("resource") val resource: String,
        @SerializedName("refresh_token") val refreshToken: String,
        @SerializedName("refresh_token_expires_in") val refreshTokenExpiresIn: String,
        @SerializedName("scope") val scope: String,
        @SerializedName("id_token") val idToken: String,
    )
    {
        /** Расшифровать [ADFSTokenResponse.accessToken] из [Base64].*/
        fun getADFSUser() : ADFSUser
        {
            val get = accessToken.split('.')[1]
            val decodedString = String(Base64.getUrlDecoder().decode(get))
            return decodedString.convertJsonToClass<ADFSUser>()
        }
    }

    /**Данные пользователя из [ADFSTokenResponse.accessToken]. */
    data class ADFSUser(val aud: String,
                        val iss: String,
                        val iat: String,
                        val nbf: String,
                        val exp: String,
                        val email: String,
                        val unique_name: String,
                        val family_name: String,
                        val apptype: String,
                        val appid: String,
                        val authmethod: String,
                        val ver: String,
                        val scp: String )

    @POST("token")
    @FormUrlEncoded
    /** Логин к ADFS
     * @param fields Хэшмапу можно получить из [ADFS.getHashMapForLogin]*/
    suspend fun login(@FieldMap fields: HashMap<String, String>): Response<ADFSTokenResponse>

    companion object {

        private val converter: GsonConverterFactory = GsonConverterFactory.create(GsonBuilder().setLenient().create())

        /** ADFS Client */
        val API: ADFS by lazy {
            val okHttpClient = OkHttpClient.Builder().apply {
                callTimeout(60, TimeUnit.SECONDS)
                connectTimeout(20, TimeUnit.SECONDS)
                readTimeout(20, TimeUnit.SECONDS)
                writeTimeout(20, TimeUnit.SECONDS)
            }
            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor().apply {
                    setLevel(HttpLoggingInterceptor.Level.BODY)
                    okHttpClient.addInterceptor(this)
                }
            }
            val retrofit = Retrofit.Builder()
                .client(okHttpClient.build())
                .baseUrl("https://adfs.samgups.ru/adfs/oauth2/")
                .addConverterFactory(converter)
                .build()

            retrofit.create(ADFS::class.java)
        }

        /** Получить хэшмапу с данными для авторизации [ADFS.login]*/
        fun getHashMapForLogin(email: String, password : String) : HashMap<String, String>
        {
            val fields: HashMap<String, String> = HashMap()

            fields["username"] = email
            fields["password"] = password

            fields["grant_type"] = "password"
            fields["scope"] = "openid email profile"
            fields["client_id"] = "106c2bdd-1a43-4be9-9ad3-1018aae09c82"
            fields["client_secret"] = "4kxvuCx5vR5KEfbGUxFldTCYHeScbgJsWIw_hK3B"

            return fields
        }

    }

}

