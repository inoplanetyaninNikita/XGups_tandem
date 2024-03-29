package com.example.xgups_tandem.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.xgups_tandem.MainViewModel
import com.example.xgups_tandem.UserData
import com.example.xgups_tandem.api.ADFS.ADFS
import com.example.xgups_tandem.api.SamGUPS.Moodle
import com.example.xgups_tandem.api.SamGUPS.SamGUPS
import com.example.xgups_tandem.api.convertJsonToClass
import com.example.xgups_tandem.api.convertJsonToClassGSON
import com.example.xgups_tandem.utils.ManagerUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.reflect.Modifier
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    val managerUtils: ManagerUtils
): ViewModel() {

    lateinit var mainViewModel: MainViewModel

    //region UI Fragment

    fun login(email: String, password: String) {
        if(!validateEmail(email) && !password.isEmpty()) return
        //Коннект с АДФС
        viewModelScope.launch {
            loginToADFS(email,password)
        }

        //Коннект с СамГУПСом
        viewModelScope.launch {
            loginToGups(email, password)
        }
    }
    fun canPressToButton(login : String, password: String) : Boolean {
        if (validateEmail(login) && password.isNotEmpty()) return true
        return false
    }
    private fun validateEmail(email: String): Boolean {
        val emailValidator = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
                "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,64})$"
        val emailPattern: Pattern = Pattern.compile(emailValidator)
        val matcher = emailPattern.matcher(email)
        return matcher.matches()
    }
    //endregion
    //region Login

    private val loginSuccessADFS : MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    val loginSuccessSamGUPS : MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    val dataUser :  MutableLiveData<UserData> by lazy {
        MutableLiveData<UserData>()
    }

    private suspend fun loginToADFS(email: String, password: String) {
        val api = ADFS.API // Клиент АДФС
        val login = loginSuccessADFS //Переменная в которую кинем результаты логина

        try {
            val response = api.login(ADFS.getHashMapForLogin(email, password))
            login.value = response.isSuccessful
        } catch (Ex : java.lang.Exception) {
            login.value = false
        }
    }
    private suspend fun loginToGups(email: String, password: String) {
        val api = SamGUPS.API
        val apiMoodle = Moodle.API

        val login = loginSuccessSamGUPS
        val response = api.login(SamGUPS.AuthRequest(email,password))


        try {
            val response = api.login(SamGUPS.AuthRequest(email,password))
            if(response.isSuccessful) {

                val aes = SamGUPS.AESDecrypt(email,response.body()!!)
                val auth = aes?.convertJsonToClass<SamGUPS.AuthResponse>()!!
                dataUser.value = UserData(auth.human.split(' ')[0],
                    auth.human.split(' ')[1],
                    auth.human.split(' ')[2],
                    auth.bookNumber,
                    auth.group,
                    auth.roleID,
                    auth.course
                )
                val cookie = response.headers()["Set-Cookie"]!!


                schedule(cookie,email)
                marks(cookie,email,auth.roleID);

                login.value = response.isSuccessful

                val loginMoodle = apiMoodle.login(email.split("@")[0],password)
                Moodle.token = loginMoodle.body()!!.token
                val getUser = apiMoodle.getUserInfo()

                Moodle.userID = getUser.body()!!.userid
                mainViewModel.courses.value = apiMoodle.getAllCourses().body()!!

            }
       }
    catch (Ex : java.lang.Exception) {
           login.value = false
        }
    }
    //endregion
    //region Sucsess login and load data
    val schedule : MutableLiveData<SamGUPS.ScheduleResponse> by lazy {
        MutableLiveData<SamGUPS.ScheduleResponse>()
    }
    val marks : MutableLiveData<List<SamGUPS.MarkResponse>> by lazy {
        MutableLiveData<List<SamGUPS.MarkResponse>>()
    }

    private suspend fun schedule(cookie : String, username: String) {
        val api = SamGUPS.API
        val response = api.schedule(cookie, SamGUPS.ScheduleRequest(username))
        if(response.isSuccessful)
            schedule.value = SamGUPS.ScheduleResponse(response.body()!!)

    }
    private suspend fun marks(cookie: String, username: String, roleID: String) {
        val api = SamGUPS.API
        val response = api.marks(cookie, SamGUPS.MarksRequest(username,roleID))
        if(response.isSuccessful)
        {
            val aes = SamGUPS.AESDecrypt(username,response.body()!!)
            mainViewModel.marks.value = aes?.convertJsonToClassGSON<List<SamGUPS.MarkResponse>>()!!
        }

    }
    //endregion

}