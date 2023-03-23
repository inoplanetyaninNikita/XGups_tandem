package com.example.xgups_tandem.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.xgups_tandem.api.ADFS.ADFS
import com.example.xgups_tandem.api.SamGUPS.SamGUPS
import com.example.xgups_tandem.api.convertJsonToClass
import kotlinx.coroutines.launch
import java.util.Base64.getUrlDecoder
import java.util.regex.Pattern
import kotlin.math.log

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

        //Коннект с АДФС
        viewModelScope.launch {
            val api = ADFS.API
            val login = loginSuccessADFS

            try {
                var response = api.login(ADFS.getHashMapForLogin(email, password))
                if(response.isSuccessful)
                {
                    firstName.value = response.body()?.getADFSUser()?.unique_name
                    secondName.value = response.body()?.getADFSUser()?.unique_name
                }
                login.value = response.isSuccessful

            } catch (Ex : java.lang.Exception)
            {
                //TODO : Надо сделать заглушку при получении таймаута
            }
        }

        //Коннект с СамГУПСом
        viewModelScope.launch {
            val api = SamGUPS.API
            val login = loginSuccessSamGUPS

            try {
                var response = api.login(SamGUPS.Auth(email, password))
                login.value = response.isSuccessful
            } catch (Ex : java.lang.Exception)
            {
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