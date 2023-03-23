package com.example.xgups_tandem.ui.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.xgups_tandem.api.ADFS.ADFS
import com.example.xgups_tandem.api.SamGUPS.SamGUPS
import com.example.xgups_tandem.api.convertJsonToClass
import com.google.gson.internal.LinkedHashTreeMap
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.launch
import java.lang.reflect.Type
import java.util.regex.Pattern

class LoginViewModel : ViewModel() {
    val loginSuccessADFS : MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    val firstName : MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val secondName : MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val loginSuccessSamGUPS : MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    fun login(email: String, password: String) {

        if(!validateEmail(email) && !password.isEmpty()) return

        //Коннект с АДФС
        viewModelScope.launch {
            val api = ADFS.API
            val login = loginSuccessADFS

            try {
                var response = api.login(ADFS.getHashMapForLogin(email, password))
                if(response.isSuccessful) {
                    firstName.value = response.body()?.getADFSUser()?.unique_name
                    secondName.value = response.body()?.getADFSUser()?.family_name
                }

                login.value = response.isSuccessful

            } catch (Ex : java.lang.Exception) {
                //TODO : Надо сделать заглушку при получении таймаута
            }
        }

        //Коннект с СамГУПСом
        viewModelScope.launch {
            val api = SamGUPS.API
            val login = loginSuccessSamGUPS

            val test = "{\n" +
                    "    \"78567\": [\n" +
                    "        {\n" +
                    "            \"roleID\": \"f17c36fd-ad94-4cc5-990e-fd23b8165e6c\",\n" +
                    "            \"human\": \"Тороповский Никита Сергеевич\",\n" +
                    "            \"bookNumber\": \"1910-ЭЖД-082\",\n" +
                    "            \"group\": \"ЭЖД-91\",\n" +
                    "            \"course\": \"3\"\n" +
                    "        }\n" +
                    "    ]\n" +
                    "}"

            val moshi = Moshi.Builder().build()
            val adapter = moshi.adapter<Map<String, Any>>(
                Types.newParameterizedType(Map::class.java, String::class.java,
                    Object::class.java)
            )
            val yourMap =  adapter.fromJson(test)

            val a = yourMap?.values

            val aa = SamGUPS.convertToAuthResponse(test)
            var book = aa?.bookNumber
            println(aa?.bookNumber)
            try {
//                var response = api.login(SamGUPS.Auth(email, password))
//                login.value = response.isSuccessful
//                if(response.isSuccessful)
//                {
//                    val a = response.body()?.login
//                    val test = a?.get("roleID")!!
//                }
            } catch (Ex : java.lang.Exception) {
                //TODO : Надо сделать заглушку при получении таймаута
            }
        }
    }

    private val emailValidator = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
            "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,64})$"
    private val emailPattern: Pattern = Pattern.compile(emailValidator)
    fun validateEmail(email: String): Boolean {
        val matcher = emailPattern.matcher(email)
        return matcher.matches()
    }

}