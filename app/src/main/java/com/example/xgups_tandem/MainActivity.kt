package com.example.xgups_tandem

import android.annotation.SuppressLint
import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.xgups_tandem.databinding.ActivityMainBinding
import com.example.xgups_tandem.di.PushNotification
import com.example.xgups_tandem.di.PushNotificationData
import com.example.xgups_tandem.di.Room
import com.example.xgups_tandem.ui.login.LoginFragmentDirections
import com.example.xgups_tandem.ui.schedule.ScheduleFragment
import com.example.xgups_tandem.ui.schedule.ScheduleFragmentDirections
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val navController by lazy {
        (supportFragmentManager.findFragmentById(R.id.containerMain) as NavHostFragment).navController
    }

    lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel>()

    @Inject lateinit var push : PushNotification
    @Inject lateinit var room : Room

    @SuppressLint("UnspecifiedImmutableFlag")
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        FirebaseMessaging.getInstance().token.addOnCompleteListener{
            if(!it.isSuccessful){
                return@addOnCompleteListener
            }
            Log.e("TOKEN", it.result)
        }
        val pushBroadcast = object : BroadcastReceiver(){
            override fun onReceive(p0: Context?, p1: Intent?) {
                TODO("Not yet implemented")
            }
        }
        //registerReceiver(pushBroadcast)

        //region firestore
//        val firestore : FirebaseFirestore = FirebaseFirestore.getInstance()
//        val users : HashMap<String, Any> = HashMap()
//        users.put("test","kek")
//        firestore.collection("users").add(users)
        //endregion
        //region mypush
//        push.show(PushNotificationData("Пара", "13:54"))
        //endregion
        //region DAO TEST

        val userDao = room.db.userDao()
        lifecycleScope.launch(Dispatchers.IO){
//             userDao.insertAll(User(0,"Toropovsky", "Nikita"))
        }
        lifecycleScope.launch(Dispatchers.IO){
//            val users: List<User> = userDao.getAll()
//            Log.d("fq",users[0].firstName!!)
        }

        //endregion


//        navHostFragment.parentFragment

        val context = binding.root.context

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)


        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            val navHostFragment = supportFragmentManager.findFragmentById(R.id.containerMain) as NavHostFragment

//            bottomNavigation.selectedItemId = R.id .bottom_navigation_profile

//            app:navGraph="@navigation/loginnav"

            when (item.itemId) {
                R.id .bottom_navigation_profile-> {
                    navHostFragment.navController.setGraph(R.navigation.profilenav)
                    true
                }
                R.id .bottom_navigation_schedule -> {
                    navHostFragment.navController.setGraph(R.navigation.schedulenav)
                    true
                }
                else -> false
            }
        }
    }

    fun visibleBottomNav(isVisible : Boolean){
        try {
            if(isVisible)
                binding.bottomNavigation.visibility = View.VISIBLE
            else
                binding.bottomNavigation.visibility = View.GONE
        }
        catch (ex : Exception) {
            return
        }
    }
}
